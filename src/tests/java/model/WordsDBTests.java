package model;

import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import model.utils.FilePaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordsDBTests {
    private enum EVENT {
        ADDED_USED_WORD,
        FAILED_TO_ADD_USED_WORD,
        ADDED_NEW_WORD_TO_DICTIONARY,
        FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY
    }

    private final List<EVENT> _events = new ArrayList<>();
    private final List<EVENT> _expectedEvents = new ArrayList<>();

    private class EventsListener implements WordsDBListener {

        @Override
        public void addedUsedWord(WordsDBEvent event) {
            _events.add(EVENT.ADDED_USED_WORD);
        }

        @Override
        public void failedToAddUsedWord(WordsDBEvent event) {
            _events.add(EVENT.FAILED_TO_ADD_USED_WORD);
        }

        @Override
        public void addedNewWordToDictionary(WordsDBEvent event) {
            _events.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);
        }

        @Override
        public void failedToAddNewWordToDictionary(WordsDBEvent event) {
            _events.add(EVENT.FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY);
        }
    }

    private WordsDB _wordsDB;

    public WordsDBTests() {
    }

    @BeforeEach
    public void testSetup() {
        _events.clear();
        _expectedEvents.clear();

        List<String> dictionary = new ArrayList<>();
        dictionary.add("привет");
        dictionary.add("ПокА");
        dictionary.add("КАК");
        dictionary.add("ДеЛиШкИ");
        _wordsDB = new WordsDB(dictionary);
        _wordsDB.addWordsDBListener(new EventsListener());
    }

    @Test
    public void test_containsInDictionary_WordInLowerCaseContain() {
        String word = "привет";

        assertTrue(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_containsInDictionary_WordInUpperCaseContain() {
        String word = "ПРИВЕТ";

        assertTrue(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_containsInDictionary_WordInDifferentCasesContain() {
        String word = "ПрИвеТ";

        assertTrue(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_containsInDictionary_NotContain() {
        String word = "бананчик";

        assertFalse(_wordsDB.containsInDictionary(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_containsInDictionary_Null() {
        assertThrows(IllegalArgumentException.class, () -> _wordsDB.containsInDictionary(null));
    }

    @Test
    public void test_containsInUsedWord_WordInLowerCaseContain() {
        String word = "привет";
        Player player = new Player("Player 1", new Alphabet(FilePaths.ALPHABET_FILE_PATH), _wordsDB, new GameField(5, 5));
        _wordsDB.addToUsedWords(word, player);

        _expectedEvents.add(EVENT.ADDED_USED_WORD);

        assertTrue(_wordsDB.containsInUsedWords(word));
        assertEquals(_expectedEvents,_events);
    }

    @Test
    public void test_containsInUsedWord_WordInUpperCaseContain() {
        String word = "ПРИВЕТ";
        Player player = new Player("Player 1", new Alphabet(FilePaths.ALPHABET_FILE_PATH), _wordsDB, new GameField(5, 5));
        _wordsDB.addToUsedWords(word, player);

        _expectedEvents.add(EVENT.ADDED_USED_WORD);

        assertTrue(_wordsDB.containsInUsedWords(word));
        assertEquals(_expectedEvents,_events);
    }

    @Test
    public void test_containsInUsedWords_WordInDifferentCasesContain() {
        String word = "ПриВЕТ";
        Player player = new Player("Player 1", new Alphabet(FilePaths.ALPHABET_FILE_PATH), _wordsDB, new GameField(5, 5));
        _wordsDB.addToUsedWords(word, player);

        _expectedEvents.add(EVENT.ADDED_USED_WORD);

        assertTrue(_wordsDB.containsInUsedWords(word));
        assertEquals(_expectedEvents,_events);
    }

    @Test
    public void test_containsInUsedWords_NotContain() {
        String word = "бананчик";

        assertFalse(_wordsDB.containsInUsedWords(word));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_containsInUsedWords_Null() {
        assertThrows(IllegalArgumentException.class, () -> _wordsDB.containsInUsedWords(null));
    }

    @Test
    public void test_randomWord_WordOfSpecifiedLengthIsAvailable() {
        int length = 3;
        String expectedResult = "как";

        assertEquals(expectedResult, _wordsDB.randomWord(length));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_randomWord_WordOfSpecifiedLengthIsNotAvailable() {
        int length = 10;
        String expectedResult = "делишки";

        assertEquals(expectedResult, _wordsDB.randomWord(length));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_randomWord_LengthIsOne() {
        int length = 1;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.randomWord(length));
    }

    @Test
    public void test_randomWord_LengthIsZeroValue() {
        int length = 0;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.randomWord(length));
    }

    @Test
    public void test_randomWord_LengthIsNegativeValue() {
        int length = -1;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.randomWord(length));
    }

    @Test
    public void test_addToDictionary_NewWord() {
        String newWord = "змея";

        _expectedEvents.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);

        assertTrue(_wordsDB.addToDictionary(newWord));
        assertTrue(_wordsDB.containsInDictionary(newWord));
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addToDictionary_KnownWord() {
        String newWord = "привет";

        _expectedEvents.add(EVENT.FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY);

        assertTrue(_wordsDB.containsInDictionary(newWord));
        assertFalse(_wordsDB.addToDictionary(newWord));
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addToDictionary_Null() {
        String newWord = null;

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.addToDictionary(newWord));
    }

    @Test
    public void test_addToUsedWords_KnownAndNotUsedWord() {
        String word = "привет";
        Player player = new Player("Player 1", new Alphabet(FilePaths.ALPHABET_FILE_PATH), _wordsDB, new GameField(5, 5));

        _expectedEvents.add(EVENT.ADDED_USED_WORD);

        assertTrue(_wordsDB.addToUsedWords(word, player));
        assertTrue(_wordsDB.containsInUsedWords(word));
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addToUsedWords_KnownAndUsedWord() {
        String word = "привет";
        Player player = new Player("Player 1", new Alphabet(FilePaths.ALPHABET_FILE_PATH), _wordsDB, new GameField(5, 5));
        _wordsDB.addToUsedWords(word, player);

        _expectedEvents.add(EVENT.ADDED_USED_WORD);
        _expectedEvents.add(EVENT.FAILED_TO_ADD_USED_WORD);

        assertTrue(_wordsDB.containsInUsedWords(word));
        assertFalse(_wordsDB.addToUsedWords(word, player));
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addToUsedWords_UnknownWord() {
        String word = "банан";
        Player player = new Player("Player 1", new Alphabet(FilePaths.ALPHABET_FILE_PATH), _wordsDB, new GameField(5, 5));

        _expectedEvents.add(EVENT.FAILED_TO_ADD_USED_WORD);

        assertFalse(_wordsDB.containsInUsedWords(word));
        assertFalse(_wordsDB.addToUsedWords(word, player));
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addToUsedWords_NullWord() {
        Player player = new Player("Player 1", new Alphabet(FilePaths.ALPHABET_FILE_PATH), _wordsDB, new GameField(5, 5));

        assertThrows(IllegalArgumentException.class, () -> _wordsDB.addToUsedWords(null, player));
    }

    @Test
    public void test_addToUsedWords_NullPlayer() {
        String word = "ДеЛиШкИ";
        Player player = null;

        _expectedEvents.add(EVENT.ADDED_USED_WORD);

        assertTrue(_wordsDB.addToUsedWords(word, player));
        assertTrue(_wordsDB.containsInUsedWords(word));
        assertEquals(_expectedEvents, _events);
    }
}
