package model;

import model.enums.Direction;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import model.utils.FilePaths;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
    private enum EVENT {
        SKIPPED_TURN,
        ADDED_NEW_WORD_TO_DICTIONARY,
        FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY,
        CHOSE_CELL,
        CHOSE_WRONG_CELL,
        PLACED_LETTER,
        WORD_DOES_NOT_CONTAIN_CHANGEABLE_CELL,
        SUBMITTED_WORD,
        FAILED_TO_SUBMIT_WORD,
        FINISHED_TURN,
        CANCELED_ACTION_ON_FIELD
    }

    private final List<EVENT> _events = new ArrayList<>();
    private final List<EVENT> _expectedEvents = new ArrayList<>();

    private class EventsListener implements PlayerActionListener {

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.SKIPPED_TURN);
        }

        @Override
        public void addedNewWordToDictionary(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);
        }

        @Override
        public void failedToAddNewWordToDictionary(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY);
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHOSE_CELL);
        }

        @Override
        public void choseWrongCell(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHOSE_WRONG_CELL);
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.PLACED_LETTER);
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.WORD_DOES_NOT_CONTAIN_CHANGEABLE_CELL);
        }

        @Override
        public void submittedWord(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.SUBMITTED_WORD);
        }

        @Override
        public void failedToSubmitWord(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.FAILED_TO_SUBMIT_WORD);
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.FINISHED_TURN);
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CANCELED_ACTION_ON_FIELD);
        }
    }

    private Player _player;
    private Alphabet _alphabet;
    private WordsDB _wordsDB;
    private GameField _field;

    @BeforeEach
    public void testSetup() {
        // Clear lists of events
        _events.clear();
        _expectedEvents.clear();

        // Setup alphabet
        _alphabet = new Alphabet(FilePaths.ALPHABET_FILE_PATH);

        // Setup wordsDB
        List<String> words = new ArrayList<>();
        words.add("суп");
        words.add("привет");
        words.add("а");
        words.add("с");
        _wordsDB = new WordsDB(words);
        _wordsDB.addToUsedWords("а", null);

        // Setup field
        _field = new GameField(5,5);
        _field.placeWord("пус", 3, Direction.RIGHT);

        // Setup player
        _player = new Player("Player", _alphabet,_wordsDB,_field);
        _player.addPlayerActionListener(new EventsListener());
    }

    @Test
    public void test_startTurn_CanStartTurn() {
        _player.startTurn();

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_startTurn_CanNotStartTurn_AlreadyStartedTurn() {
        _player.startTurn();

        assertThrows(IllegalArgumentException.class, () -> _player.startTurn());
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_skipTurn_CanSkipTurn_InCorrectState() {
        _player.startTurn();
        _player.skipTurn();

        _expectedEvents.add(EVENT.SKIPPED_TURN);

        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_skipTurn_CanNotSkipTurn_DoesNotStartTurn() {
        assertThrows(IllegalArgumentException.class, () -> _player.skipTurn());
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_skipTurn_CanNotSkipTurn_AlreadySkippedTurn() {
        _player.startTurn();
        _player.skipTurn();

        _expectedEvents.add(EVENT.SKIPPED_TURN);

        assertThrows(IllegalArgumentException.class, () -> _player.skipTurn());
        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_chooseCell_DoesNotStartTurn() {
        assertThrows(IllegalArgumentException.class, () -> _player.chooseCell(new Point(0,0)));
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_chooseCell_WrongPositionOfCellOnField() {
        _player.startTurn();

        assertThrows(IllegalArgumentException.class, () -> _player.chooseCell(new Point(100,1000)));
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_chooseCell_PlacesLetter_StartedTurn() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));

        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_chooseCell_PlacesLetter_ChoseCellWithLetter() {
        _player.startTurn();
        _field.cell(new Point(0,2)).setLetter('а');
        _player.chooseCell(new Point(0,2));

        _expectedEvents.add(EVENT.CHOSE_WRONG_CELL);

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_chooseCell_PlacesLetter_ChoseCellTwice() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.chooseCell(new Point(0,2));

        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_chooseCell_PlacesLetter_ChoseCellWithLetterWhenNeedToPlaceLetter() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _field.cell(new Point(1,1)).setLetter('а');
        _player.chooseCell(new Point(1,1));

        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_placeLetter_InCorrectState() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('а');

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_placeLetter_InIncorrectState() {
        assertThrows(IllegalArgumentException.class, () -> _player.placeLetter('а'));
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_placeLetter_ChangeableCellIsNull() {
        _player.startTurn();

        assertThrows(IllegalArgumentException.class, () -> _player.placeLetter('а'));
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_placeLetter_LetterIsNotAvailableInAlphabet() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));

        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertThrows(IllegalArgumentException.class, () -> _player.placeLetter('ж'));
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_chooseCell_FormsWord_ChoseCorrectCell() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('а');
        _player.chooseCell(new Point(0,2));

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_chooseCell_FormsWord_ChoseCellWithoutLetter() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('а');
        _player.chooseCell(new Point(0,2));
        _player.chooseCell(new Point(2,4));

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_WRONG_CELL);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_submitWord_InIncorrectState() {
        _player.startTurn();

        assertThrows(IllegalArgumentException.class, () -> _player.submitWord());
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_submitWord_WordDoesNotContainChangeableCell() {
        _field.cell(new Point(1,0)).setLetter('п');
        _field.cell(new Point(2,0)).setLetter('б');

        _player.startTurn();
        _player.chooseCell(new Point(0,0));
        _player.placeLetter('а');
        _player.chooseCell(new Point(1,0));
        _player.chooseCell(new Point(2,0));
        _player.submitWord();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.WORD_DOES_NOT_CONTAIN_CHANGEABLE_CELL);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_submitWord_WordWasUsedAlready() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('а');
        _player.chooseCell(new Point(0,2));
        _player.submitWord();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.FAILED_TO_SUBMIT_WORD);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_submitWord_UnknownWord() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('п');
        _player.chooseCell(new Point(0,2));
        _player.submitWord();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.FAILED_TO_SUBMIT_WORD);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_submitWord_EverythingFine() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('с');
        _player.chooseCell(new Point(0,2));
        _player.submitWord();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.SUBMITTED_WORD);
        _expectedEvents.add(EVENT.FINISHED_TURN);

        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addNewWordToDictionary_InCorrectState() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('п');
        _player.chooseCell(new Point(0,2));
        _player.addNewWordToDictionary();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addNewWordToDictionary_InIncorrectState() {
        _player.startTurn();

        assertThrows(IllegalArgumentException.class, () -> _player.addNewWordToDictionary());
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_addNewWordToDictionary_AlreadyAddedWord() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('а');
        _player.chooseCell(new Point(0,2));
        _player.addNewWordToDictionary();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_cancelActionOnField_InIncorrectState() {
        assertThrows(IllegalArgumentException.class, () -> _player.cancelActionOnField());
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_cancelActionOnField_PlacesLetter_ForgetAboutNothing() {
        _player.startTurn();
        _player.cancelActionOnField();

        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_cancelActionOnField_PlacesLetter_ForgetAboutChoseCell() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.cancelActionOnField();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_cancelActionOnField_FormsWord_ForgetAboutChoseCellsWithLetter() {
        _field.cell(new Point(1, 0)).setLetter('а');
        _field.cell(new Point(2, 0)).setLetter('а');

        _player.startTurn();
        _player.chooseCell(new Point(0,0));
        _player.placeLetter('а');
        _player.chooseCell(new Point(1,0));
        _player.chooseCell(new Point(2,0));
        _player.cancelActionOnField();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_cancelActionOnField_FormsWord_ForgetAboutPlacedLetterAndCell() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.placeLetter('а');

        assertEquals(PlayerState.FORMS_WORD, _player.state());

        _player.cancelActionOnField();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(_expectedEvents, _events);
    }
}
