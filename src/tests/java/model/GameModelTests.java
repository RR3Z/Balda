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

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTests {
    private enum EVENT {
        GAME_IS_FINISHED,
        PLAYER_EXCHANGED,
        PLACED_START_WORD
    }
    private final List<EVENT> _events = new ArrayList<>();

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
        public void placedStartWord(GameModelEvent event) {
            _events.add(EVENT.PLACED_START_WORD);
        }
    }

    private GameModel _gameModel;

    @BeforeEach
    public void testSetup() {
        _events.clear();

        _gameModel = new GameModel(5, 5);
        _gameModel.addGameModelListener(new GameListener());
    }

    @Test
    public void startGame_CanStartGame() {
        _gameModel.startGame();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertNotNull(_gameModel.activePlayer());
        assertEquals(PlayerState.SELECTING_LETTER, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void startGame_StartGameTwice() {
        _gameModel.startGame();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertThrows(IllegalArgumentException.class, () -> _gameModel.startGame());
        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertNotNull(_gameModel.activePlayer());
        assertEquals(PlayerState.SELECTING_LETTER, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void PlacesLetter_activePlayerPlaceLetterIntoCellWithLetter() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 2));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertNull(_gameModel.gameField().changedCell());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.PLACES_LETTER, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void PlacesLetter_activePlayerPlaceLetter() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void PlacesLetter_activePlayerCanceledPlacedLetter() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        _gameModel.activePlayer().cancelActionOnField();
        assertEquals(letter, _gameModel.alphabet().selectedLetter());
        assertNull(_gameModel.gameField().changedCell());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.PLACES_LETTER, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void FormsWord_activePlayerCanceledSelectedCell() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,2));
        _gameModel.activePlayer().chooseCell(cell);

        _gameModel.activePlayer().cancelActionOnField();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void FormsWord_activePlayerAddedWordToDictionary() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,1));
        _gameModel.activePlayer().chooseCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,2));
        _gameModel.activePlayer().chooseCell(cell);

        cell = _gameModel.gameField().cell(new Point(1,2));
        _gameModel.activePlayer().chooseCell(cell);

        _gameModel.activePlayer().addNewWordToDictionary();
        _gameModel.activePlayer().submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.SELECTING_LETTER, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);

        _gameModel.activePlayer().skipTurn();
        assertEquals(3, _gameModel.activePlayer().scoreCounter().score());
    }

    @Test
    public void FormsWord_activePlayerSubmittedWordWithoutChangeableCell() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,2));
        _gameModel.activePlayer().chooseCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,3));
        _gameModel.activePlayer().chooseCell(cell);

        _gameModel.activePlayer().submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
        assertEquals(0, _gameModel.activePlayer().scoreCounter().score());
    }

    @Test
    public void FormsWord_activePlayerFailedToSubmitWord() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,1));
        _gameModel.activePlayer().chooseCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,2));
        _gameModel.activePlayer().chooseCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,3));
        _gameModel.activePlayer().chooseCell(cell);

        _gameModel.activePlayer().submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
        assertEquals(0, _gameModel.activePlayer().scoreCounter().score());
    }

    @Test
    public void gameIsFinished_becauseOfSkipTurns() {
        _gameModel.startGame();

        _gameModel.activePlayer().skipTurn();
        _gameModel.activePlayer().skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.GAME_IS_FINISHED);

        assertEquals(GameState.FINISHED, _gameModel.state());
        assertEquals(PlayerState.SKIPPED_TURN, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void submittedWord() {
        _gameModel.startGame();

        Character letter = 'ъ';
        _gameModel.activePlayer().chooseLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        _gameModel.activePlayer().chooseCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,1));
        _gameModel.activePlayer().chooseCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,2));
        _gameModel.activePlayer().chooseCell(cell);

        cell = _gameModel.gameField().cell(new Point(1,2));
        _gameModel.activePlayer().chooseCell(cell);

        _gameModel.activePlayer().addNewWordToDictionary();
        _gameModel.activePlayer().submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.SELECTING_LETTER, _gameModel.activePlayer().state());
        assertEquals(expectedEvents, _events);

        _gameModel.activePlayer().skipTurn();
        assertEquals(3, _gameModel.activePlayer().scoreCounter().score());
    }

}
