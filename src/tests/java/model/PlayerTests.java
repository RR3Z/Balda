package model;

import model.enums.Direction;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import model.utils.DataFilePaths;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {
    private GameModel _gameModel;
    private Player _player;
    private Alphabet _alphabet;
    private WordsDB _wordsDB;
    private GameField _field;
    private enum EVENT {
        CHANGED_STATE,
        SKIPPED_TURN,
        FINISHED_TURN,
        PLACED_LETTER,
        CHOSE_LETTER,
        CHOSE_CELL,
        SUBMITTED_WORD_WITHOUT_CHANGEABLE_CELL,
        CANCELED_ACTION_ON_FIELD
    }

    private class EventsListener implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHANGED_STATE);
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.SKIPPED_TURN);
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.FINISHED_TURN);
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.PLACED_LETTER);
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHOSE_LETTER);
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHOSE_CELL);
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.SUBMITTED_WORD_WITHOUT_CHANGEABLE_CELL);
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CANCELED_ACTION_ON_FIELD);
        }
    }
    private final List<EVENT> _events = new ArrayList<>();

    @BeforeEach
    public void testSetup() {
        _events.clear();

        _gameModel = new GameModel(5, 5);
        _gameModel.startGame();

        _alphabet = _gameModel.alphabet();
        _wordsDB = _gameModel.wordsDB();

        _field = _gameModel.gameField();

        _player = _gameModel.activePlayer();
        _player.addPlayerActionListener(new EventsListener());
    }

    @Test
    public void startTurn_CanStartTurn() {
        _events.clear();

        _player = new Player("PLAYER", _alphabet, _wordsDB, _field);
        _player.addPlayerActionListener(new EventsListener());

        _player.startTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);

        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SELECTING_LETTER, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void startTurn_CanNotStartTurn_AlreadyStartedTurn() {
        _events.clear();

        _player = new Player("PLAYER", _alphabet, _wordsDB, _field);
        _player.addPlayerActionListener(new EventsListener());

        _player.startTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);

        assertThrows(IllegalArgumentException.class, () -> _player.startTurn());
        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SELECTING_LETTER, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void startTurn_CanStartTurn_StartTurnAfterSkipTurn() {
        _events.clear();

        _player = new Player("PLAYER", _alphabet, _wordsDB, _field);
        _player.addPlayerActionListener(new EventsListener());

        _player.startTurn();
        _player.skipTurn();
        _player.startTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.SKIPPED_TURN);
        expectedEvents.add(EVENT.CHANGED_STATE);

        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SELECTING_LETTER, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void skipTurn_CanSkipTurn_AfterStartTurn() {
        _player.skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SKIPPED_TURN);

        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void skipTurn_CanNotSkipTurn_DoesNotStartTurn() {
        _events.clear();

        _player = new Player("PLAYER", _alphabet, _wordsDB, _field);
        _player.addPlayerActionListener(new EventsListener());

        assertThrows(IllegalArgumentException.class, () -> _player.skipTurn());
        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void skipTurn_CanNotSkipTurn_AlreadySkippedTurn() {
        _player.skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SKIPPED_TURN);

        assertThrows(IllegalArgumentException.class, () -> _player.skipTurn());
        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void skipTurn_CanSkipTurn_AfterChooseLetter() {
        _player.chooseLetter('а');
        _player.skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.SKIPPED_TURN);

        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void skipTurn_CanSkipTurn_AfterPlaceLetter() {
        Character letter = 'а';
        _player.chooseLetter(letter);
        assertEquals(letter, _alphabet.selectedLetter());

        Cell cell = _field.cell(new Point(0,1));
        _player.chooseCell(cell);
        assertEquals(cell, _field.changedCell());
        assertEquals(letter, _field.changedCell().letter());

        _player.skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.SKIPPED_TURN);

        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void skipTurn_CanSkipTurn_WhenFormWord() {
        Character letter = 'а';
        _player.chooseLetter(letter);
        assertEquals(letter, _alphabet.selectedLetter());

        Cell cell = _field.cell(new Point(0,1));
        _player.chooseCell(cell);
        assertEquals(cell, _field.changedCell());
        assertEquals(letter, _field.changedCell().letter());

        _player.chooseCell(_field.cell(new Point(0,1)));
        _player.chooseCell(_field.cell(new Point(0,2)));
        _player.chooseCell(_field.cell(new Point(1,2)));

        _player.skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.SKIPPED_TURN);

        assertNull(_field.changedCell());
        assertNull(_alphabet.selectedLetter());
        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void chooseLetter_InCorrectState() {
        Character letter = 'а';
        _player.chooseLetter(letter);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void chooseLetter_DoesNotStartTurn() {
        _events.clear();

        _player = new Player("PLAYER", _alphabet, _wordsDB, _field);
        _player.addPlayerActionListener(new EventsListener());

        Character letter = 'а';

        assertThrows(IllegalArgumentException.class, () -> _player.chooseLetter(letter));
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void chooseLetter_ChooseLetterAfterChoseItAlready() {
        Character letter = 'а';
        _player.chooseLetter(letter);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);

        assertThrows(IllegalArgumentException.class, () -> _player.chooseLetter('ф'));
        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeLetter_InCorrectState() {
        Character letter = 'а';
        _player.chooseLetter(letter);

        Cell changedCell = _field.cell(new Point(0,1));
        _player.chooseCell(changedCell);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(changedCell, _field.changedCell());
        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeLetter_DoesNotChooseLetter() {
        Cell changedCell = _field.cell(new Point(0,0));

        assertThrows(IllegalArgumentException.class, () -> _player.chooseCell(changedCell));
        assertNull(_alphabet.selectedLetter());
        assertNull(_field.changedCell());
        assertEquals(PlayerState.SELECTING_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void placeLetter_LetterIsNotAvailableInAlphabet() {
        Character letter = 'b';
        _player.chooseLetter(letter);

        assertNull(_alphabet.selectedLetter());
        assertNull(_field.changedCell());
        assertEquals(PlayerState.SELECTING_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void chooseCell_DoesNotStartTurn() {
        _events.clear();

        _player = new Player("PLAYER", _alphabet, _wordsDB, _field);
        _player.addPlayerActionListener(new EventsListener());

        assertThrows(IllegalArgumentException.class, () -> _player.chooseCell(_field.cell(new Point(0,0))));
        assertNull(_alphabet.selectedLetter());
        assertNull(_field.changedCell());
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void chooseCell_DoesNotSelectLetter() {
        assertThrows(IllegalArgumentException.class, () -> _player.chooseCell(_field.cell(new Point(0,1))));
        assertNull(_alphabet.selectedLetter());
        assertNull(_field.changedCell());
        assertEquals(PlayerState.SELECTING_LETTER, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void chooseCell_PlaceLetterInCell() {
        Character letter = 'ф';
        _player.chooseLetter(letter);
        assertEquals(letter, _alphabet.selectedLetter());

        Cell changedCell = _field.cell(new Point(0, 1));
        _player.chooseCell(changedCell);
        assertEquals(changedCell, _field.changedCell());
        assertEquals(letter, _field.changedCell().letter());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);

        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void chooseCell_PlaceLetterInCell_CellHasNoNeighborsWithLetter() {
        Character letter = 'ф';
        _player.chooseLetter(letter);

        Cell cellWithoutNegihborsWithLetter = _field.cell(new Point(0, 0));
        _player.chooseCell(cellWithoutNegihborsWithLetter);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);

        assertEquals(letter, _alphabet.selectedLetter());
        assertNull(_field.changedCell());
        assertNull(cellWithoutNegihborsWithLetter.letter());
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void chooseCell_PlaceLetterInCell_CellHasLetter() {
        Character letter = 'ф';
        _player.chooseLetter(letter);

        Cell cellWithLetter = _field.cell(new Point(0, 2));
        assertNotNull(cellWithLetter.letter());
        _player.chooseCell(cellWithLetter);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);

        assertEquals(letter, _alphabet.selectedLetter());
        assertNull(_field.changedCell());
        assertEquals(PlayerState.PLACES_LETTER, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void chooseCell_FormsWord_ChoseSameCellTwice() {
        Character letter = 'ф';
        _player.chooseLetter(letter);

        Cell changedCell = _field.cell(new Point(0, 1));
        _player.chooseCell(changedCell);

        _player.chooseCell(changedCell);
        _player.chooseCell(changedCell);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(changedCell, _field.changedCell());
        assertEquals(letter, changedCell.letter());
        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void chooseCell_FormsWord_ChoseCorrectCell() {
        Character letter = 'ф';
        _player.chooseLetter(letter);

        Cell changedCell = _field.cell(new Point(0, 1));
        _player.chooseCell(changedCell);

        _player.chooseCell(changedCell);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(changedCell, _field.changedCell());
        assertEquals(letter, changedCell.letter());
        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void chooseCell_FormsWord_ChoseCellWithoutLetter() {
        Character letter = 'ф';
        _player.chooseLetter(letter);

        Cell changedCell = _field.cell(new Point(0, 1));
        _player.chooseCell(changedCell);

        Cell cellWithoutLetter = _field.cell(new Point(1,1));
        assertNull(cellWithoutLetter.letter());
        _player.chooseCell(cellWithoutLetter);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(changedCell, _field.changedCell());
        assertEquals(letter, changedCell.letter());
        assertEquals(PlayerState.FORMS_WORD, _player.state());
        assertEquals(expectedEvents, _events);
    }

    // TODO
    /*
    @Test
    public void submitWord_InIncorrectState() {
        _player.startTurn();

        assertThrows(IllegalArgumentException.class, () -> _player.submitWord());
        assertEquals(PlayerState.SELECTING_CHANGEABLE_CELL, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void submitWord_WordDoesNotContainChangeableCell() {
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
    public void submitWord_WordWasUsedAlready() {
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
    public void submitWord_UnknownWord() {
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
    public void submitWord_EverythingFine() {
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
    public void addNewWordToDictionary_InCorrectState() {
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
    public void addNewWordToDictionary_InIncorrectState() {
        _player.startTurn();

        assertThrows(IllegalArgumentException.class, () -> _player.addNewWordToDictionary());
        assertEquals(PlayerState.SELECTING_CHANGEABLE_CELL, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void addNewWordToDictionary_AlreadyAddedWord() {
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
    public void cancelActionOnField_InIncorrectState() {
        assertThrows(IllegalArgumentException.class, () -> _player.cancelActionOnField());
        assertEquals(PlayerState.WAITING_TURN, _player.state());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void cancelActionOnField_SelectingChangeableCell_ForgetAboutNothing() {
        _player.startTurn();
        _player.cancelActionOnField();

        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(PlayerState.SELECTING_CHANGEABLE_CELL, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void cancelActionOnField_SelectingChangeableCell_ForgetAboutChoseCell() {
        _player.startTurn();
        _player.chooseCell(new Point(0,2));
        _player.cancelActionOnField();

        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(PlayerState.SELECTING_CHANGEABLE_CELL, _player.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void cancelActionOnField_FormsWord_ForgetAboutChoseCellsWithLetter() {
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
    public void cancelActionOnField_FormsWord_ForgetAboutPlacedLetterAndCell() {
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

     */
}
