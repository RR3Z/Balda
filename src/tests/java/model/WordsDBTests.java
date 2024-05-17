package model;

import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import model.utils.DataFilePaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.utils.GameWidgetUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordsDBTests {
    private GameModel _gameModel;
    private WordsDB _wordsDB;
    private enum EVENT {
        ADDED_TO_USED_WORDS,
        ADDED_NEW_WORD_TO_DICTIONARY,
        WORD_ALREADY_USED,
        WORD_NOT_ALLOWED
    }
    private List<EVENT> _events;

    private class EventsListener implements WordsDBListener {
        @Override
        public void addedToUsedWords(WordsDBEvent event) {
            _events.add(EVENT.ADDED_TO_USED_WORDS);
        }

        @Override
        public void addedNewWordToDictionary(WordsDBEvent event) {
            _events.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);
        }

        @Override
        public void wordAlreadyUsed(WordsDBEvent event) {
            _events.add(EVENT.WORD_ALREADY_USED);
        }

        @Override
        public void wordNotAllowed(WordsDBEvent event) {
            _events.add(EVENT.WORD_NOT_ALLOWED);
        }
    }

    @BeforeEach
    public void testSetup() {
        _events = new ArrayList<>();

        _gameModel = new GameModel(5, 5);

        _wordsDB = _gameModel.wordsDB();
        _wordsDB.addWordsDBListener(new EventsListener());
    }

    @Test
    public void containsInDictionary_DictionaryContainsWordInLowerCase() {
        String word = "привет";

        assertTrue(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInDictionary_DictionaryNotContainsWordInUpperCase() {
        String word = "ПРИВЕТ";

        assertFalse(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInDictionary_DictionaryNotContainsWordInDifferentCases() {
        String word = "ПрИвеТ";

        assertFalse(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInDictionary_DictionaryDoNotContainWord() {
        String word = "бананчик";

        assertFalse(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInDictionary_NullObject() {
        assertThrows(IllegalArgumentException.class, () -> _wordsDB.containsInDictionary(null));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInUsedWord_UsedWordsContainsWord() {
        String word = "привет";

        assertFalse(_wordsDB.containsInUsedWords(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInUsedWord_UsedWordsDoNotContainWordInUppercase() {
        String word = "ПРИВЕТ";

        assertFalse(_wordsDB.containsInUsedWords(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInUsedWords_UsedWordsDoNotContainWordInDifferentCases() {
        String word = "пРиВеТ";

        assertFalse(_wordsDB.containsInUsedWords(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInUsedWords_UnknownWord() {
        String word = "бананчик";

        assertFalse(_wordsDB.containsInUsedWords(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void containsInUsedWords_NullObject() {
        assertThrows(IllegalArgumentException.class, () -> _wordsDB.containsInUsedWords(null));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void randomWord_WordOfSpecifiedLengthIsAvailable() {
        int length = 3;

        assertEquals(length, _wordsDB.randomWord(length).length());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void randomWord_WordOfSpecifiedLengthIsNotAvailable() {
        int length = 10;

        assertEquals(length, _wordsDB.randomWord(length).length());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void randomWord_LengthIsOne() {
        int length = 1;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.randomWord(length));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void randomWord_LengthIsZero() {
        int length = 0;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.randomWord(length));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void randomWord_LengthIsNegativeValue() {
        int length = -1;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.randomWord(length));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void addToDictionary_NewWord() {
        String newWord = "волнистая";

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);

        assertTrue(_wordsDB.addToDictionary(newWord, _gameModel.activePlayer()));
        assertTrue(_wordsDB.containsInDictionary(newWord));
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void addToDictionary_KnownWord() {
        String newWord = "привет";

        assertTrue(_wordsDB.containsInDictionary(newWord));
        assertFalse(_wordsDB.addToDictionary(newWord, _gameModel.activePlayer()));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void addToDictionary_NullObject() {
        String newWord = null;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.addToDictionary(newWord, _gameModel.activePlayer()));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void addToUsedWords_KnownAndNotUsedWord() {
        String word = "привет";

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.ADDED_TO_USED_WORDS);

        assertTrue(_wordsDB.addToUsedWords(word, _gameModel.activePlayer()));
        assertTrue(_wordsDB.containsInUsedWords(word));
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void addToUsedWords_KnownAndUsedWord() {
        String word = "привет";
        _wordsDB.addToUsedWords(word, _gameModel.activePlayer());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.ADDED_TO_USED_WORDS);
        expectedEvents.add(EVENT.WORD_ALREADY_USED);

        assertTrue(_wordsDB.containsInUsedWords(word));
        assertFalse(_wordsDB.addToUsedWords(word, _gameModel.activePlayer()));
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void addToUsedWords_UnknownWord() {
        String word = "популярный";

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.WORD_NOT_ALLOWED);

        assertFalse(_wordsDB.containsInDictionary(word));
        assertFalse(_wordsDB.containsInUsedWords(word));
        assertFalse(_wordsDB.addToUsedWords(word, _gameModel.activePlayer()));
        assertFalse(_wordsDB.containsInUsedWords(word));
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void addToUsedWords_SameWordTwoTimesWithDifferentPlayers() {
        _gameModel.startGame();

        String word = "игра";

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.ADDED_TO_USED_WORDS);
        expectedEvents.add(EVENT.ADDED_TO_USED_WORDS);
        expectedEvents.add(EVENT.WORD_ALREADY_USED);

        assertTrue(_wordsDB.containsInDictionary(word));
        assertFalse(_wordsDB.containsInUsedWords(word));

        assertTrue(_wordsDB.addToUsedWords(word, _gameModel.activePlayer()));
        assertTrue(_wordsDB.containsInUsedWords(word));

        _gameModel.activePlayer().skipTurn();
        assertFalse(_wordsDB.addToUsedWords(word, _gameModel.activePlayer()));
        assertTrue(_wordsDB.containsInUsedWords(word));

        assertEquals(expectedEvents, _events);
    }

    @Test
    public void addToUsedWords_SameWordTwoTimesWithSamePlayer() {
        _gameModel.startGame();

        String word = "игра";

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.ADDED_TO_USED_WORDS);
        expectedEvents.add(EVENT.ADDED_TO_USED_WORDS);
        expectedEvents.add(EVENT.WORD_ALREADY_USED);

        assertTrue(_wordsDB.containsInDictionary(word));
        assertFalse(_wordsDB.containsInUsedWords(word));

        assertTrue(_wordsDB.addToUsedWords(word, _gameModel.activePlayer()));
        assertTrue(_wordsDB.containsInUsedWords(word));
        
        assertFalse(_wordsDB.addToUsedWords(word, _gameModel.activePlayer()));
        assertTrue(_wordsDB.containsInUsedWords(word));

        assertEquals(expectedEvents, _events);
    }
}
