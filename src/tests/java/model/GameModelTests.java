package model;

import model.enums.GameState;
import model.enums.PlayerState;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameModelTests {
    private enum EVENT {
        SKIPPED_TURN,
        ADDED_NEW_WORD_TO_DICTIONARY,
        FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY,
        CHOSE_CELL,
        CHOSE_WRONG_CELL,
        PLACED_LETTER,
        SUBMITTED_WORD,
        FAILED_TO_SUBMIT_WORD,
        CANCELED_ACTION_ON_FIELD,
        PLAYER_EXCHANGED,
        DEFINED_START_WORD,
        GAME_IS_FINISHED,
        SUBMITTED_WORD_WITHOUT_CHANGEABLE_CELL
    }

    private final List<EVENT> _events = new ArrayList<>();
    private final List<EVENT> _expectedEvents = new ArrayList<>();

    private class GameListener implements GameModelListener {

        @Override
        public void gameIsFinished(GameModelEvent event) {
            _events.add(EVENT.GAME_IS_FINISHED);
        }

        @Override
        public void playerExchanged(GameModelEvent event) {
            _events.add(EVENT.PLAYER_EXCHANGED);
        }

        @Override
        public void definedStartWord(GameModelEvent event) {
            _events.add(EVENT.DEFINED_START_WORD);
        }

        @Override
        public void playerSkippedTurn(GameModelEvent event) {
            _events.add(EVENT.SKIPPED_TURN);
        }

        @Override
        public void playerChoseCell(GameModelEvent event) {
            _events.add(EVENT.CHOSE_CELL);
        }

        @Override
        public void playerFailedToAddNewWordToDictionary(GameModelEvent event) {
            _events.add(EVENT.FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY);
        }

        @Override
        public void playerAddedNewWordToDictionary(GameModelEvent event) {
            _events.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);
        }

        @Override
        public void playerChoseWrongCell(GameModelEvent event) {
            _events.add(EVENT.CHOSE_WRONG_CELL);
        }

        @Override
        public void playerPlacedLetter(GameModelEvent event) {
            _events.add(EVENT.PLACED_LETTER);
        }

        @Override
        public void playerSubmittedWordWithoutChangeableCell(GameModelEvent event) {
            _events.add(EVENT.SUBMITTED_WORD_WITHOUT_CHANGEABLE_CELL);
        }

        @Override
        public void playerCanceledActionOnField(GameModelEvent event) {
            _events.add(EVENT.CANCELED_ACTION_ON_FIELD);
        }

        @Override
        public void playerSubmittedWord(GameModelEvent event) {
            _events.add(EVENT.SUBMITTED_WORD);
        }

        @Override
        public void playerFailedToSubmitWord(GameModelEvent event) {
            _events.add(EVENT.FAILED_TO_SUBMIT_WORD);
        }
    }

    private GameModel _game;

    @BeforeEach
    public void testSetup() {
        // Clear lists of events
        _events.clear();
        _expectedEvents.clear();

        _game = new GameModel(4, 7);
        _game.addGameModelListener(new GameListener());
    }

    @Test
    public void test_startGame_CanStartGame() {
        _game.startGame();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_startGame_StartGameTwice() {
        _game.startGame();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertThrows(IllegalArgumentException.class, () -> _game.startGame());
        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_PlacesLetter_activePlayerChoseCellThatHasNeighborWithLetter() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_PlacesLetter_activePlayerChoseCellThatHasNoNeighborWithLetter() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,0));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_PlacesLetter_activePlayerChoseCellThatHasNeighborWithLetterTwice() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().chooseCell(new Point(0,3));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_PlacesLetter_activePlayerChoseCellWithLetter() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,4));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_WRONG_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_PlacesLetter_activePlayerPlaceLetter() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerPlaceLetter() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);

        assertThrows(IllegalArgumentException.class, () -> _game.activePlayer().placeLetter('б'));
        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerChoseCellWithLetter() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,3));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerChoseCellWithoutLetter() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,0));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_WRONG_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerChoseCellWhichAdjacentWithPrevious() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,4));
        _game.activePlayer().chooseCell(new Point(0,3));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerChoseCellWhichIsNotAdjacentWithPrevious() {
        _game.startGame();
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,4));
        _game.activePlayer().chooseCell(new Point(3,4));

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_WRONG_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerSubmittedWord() {
        _game.startGame();
        Player prevPlayer = _game.activePlayer();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().submitWord();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.SUBMITTED_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(PlayerState.WAITING_TURN, prevPlayer.state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerCanceledFormWordActionOnField() {
        _game.startGame();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().cancelActionOnField();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerCanceledPlacedLetterActionOnField() {
        _game.startGame();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().cancelActionOnField();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }
    @Test
    public void test_FormsWord_activePlayerCanceledChoseCellActionOnField() {
        _game.startGame();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().cancelActionOnField();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CANCELED_ACTION_ON_FIELD);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.PLACES_LETTER, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerAddedWordToDictionary() {
        _game.startGame();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().chooseCell(new Point(0,4));
        _game.activePlayer().addNewWordToDictionary();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.ADDED_NEW_WORD_TO_DICTIONARY);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerFailedToAddWordToDictionary() {
        _game.startGame();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().addNewWordToDictionary();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.FAILED_TO_ADD_NEW_WORD_TO_DICTIONARY);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerSubmittedWordWithoutChangeableCell() {
        _game.startGame();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,4));
        _game.activePlayer().chooseCell(new Point(1,4));
        _game.activePlayer().chooseCell(new Point(2,4));
        _game.activePlayer().chooseCell(new Point(3,4));
        _game.activePlayer().submitWord();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.SUBMITTED_WORD_WITHOUT_CHANGEABLE_CELL);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_FormsWord_activePlayerFailedToSubmitWord() {
        _game.startGame();

        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,3));
        _game.activePlayer().submitWord();
        _game.activePlayer().chooseCell(new Point(0,2));
        _game.activePlayer().placeLetter('а');
        _game.activePlayer().chooseCell(new Point(0,2));
        _game.activePlayer().submitWord();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.SUBMITTED_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.PLACED_LETTER);
        _expectedEvents.add(EVENT.CHOSE_CELL);
        _expectedEvents.add(EVENT.FAILED_TO_SUBMIT_WORD);

        assertEquals(GameState.IN_PROCESS, _game.state());
        assertEquals(PlayerState.FORMS_WORD, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }

    @Test
    public void test_gameIsFinished_becauseOfSkipTurns() {
        _game.startGame();

        _game.activePlayer().skipTurn();
        _game.activePlayer().skipTurn();

        _expectedEvents.add(EVENT.DEFINED_START_WORD);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        _expectedEvents.add(EVENT.GAME_IS_FINISHED);

        assertEquals(GameState.FINISHED, _game.state());
        assertEquals(PlayerState.SKIPPED_TURN, _game.activePlayer().state());
        assertEquals(_expectedEvents, _events);
    }
}
