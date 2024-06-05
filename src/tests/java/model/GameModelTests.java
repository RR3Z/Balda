package model;

import model.enums.GameState;
import model.enums.PlayerState;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import model.players.AIPlayer;
import model.players.AbstractPlayer;
import model.players.UserPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.EventListener;
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

        _gameModel = new GameModel(5, 5, false);
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
        assertEquals(PlayerState.SELECTING_LETTER, ((UserPlayer)_gameModel.activePlayer()).state());
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
        assertEquals(PlayerState.SELECTING_LETTER, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placesLetter_activePlayerPlaceLetterIntoCellWithLetter() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertNull(_gameModel.gameField().changedCell());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.PLACES_LETTER, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placesLetter_activePlayerPlaceLetter() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placesLetter_activePlayerCanceledPlacedLetter() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        ((UserPlayer)_gameModel.activePlayer()).cancelActionOnField();
        assertEquals(letter, _gameModel.alphabet().selectedLetter());
        assertNull(_gameModel.gameField().changedCell());

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.PLACES_LETTER, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void formsWord_activePlayerCanceledSelectedCell() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        ((UserPlayer)_gameModel.activePlayer()).cancelActionOnField();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void formsWord_activePlayerAddedWordToDictionary() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        cell = _gameModel.gameField().cell(new Point(1,2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        ((UserPlayer)_gameModel.activePlayer()).addNewWordToDictionary();
        ((UserPlayer)_gameModel.activePlayer()).submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.SELECTING_LETTER, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        assertEquals(3, ((UserPlayer)_gameModel.activePlayer()).scoreCounter().score());
    }

    @Test
    public void formsWord_activePlayerSubmittedWordWithoutChangeableCell() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,3));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        ((UserPlayer)_gameModel.activePlayer()).submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
        assertEquals(0, ((UserPlayer)_gameModel.activePlayer()).scoreCounter().score());
    }

    @Test
    public void formsWord_activePlayerFailedToSubmitWord() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,3));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        ((UserPlayer)_gameModel.activePlayer()).submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.FORMS_WORD, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
        assertEquals(0, ((UserPlayer)_gameModel.activePlayer()).scoreCounter().score());
    }

    @Test
    public void gameIsFinished_becauseOfSkipTurns() {
        _gameModel.startGame();

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        ((UserPlayer)_gameModel.activePlayer()).skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.GAME_IS_FINISHED);

        assertEquals(GameState.FINISHED, _gameModel.state());
        assertEquals(PlayerState.SKIPPED_TURN, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void submittedWord() {
        _gameModel.startGame();

        Character letter = 'ъ';
        ((UserPlayer)_gameModel.activePlayer()).selectLetter(letter);
        assertEquals(letter, _gameModel.alphabet().selectedLetter());

        Cell changedCell = _gameModel.gameField().cell(new Point(0, 1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(changedCell);
        assertEquals(changedCell, _gameModel.gameField().changedCell());

        Cell cell = _gameModel.gameField().cell(new Point(0,1));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        cell = _gameModel.gameField().cell(new Point(0,2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        cell = _gameModel.gameField().cell(new Point(1,2));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(cell);

        ((UserPlayer)_gameModel.activePlayer()).addNewWordToDictionary();
        ((UserPlayer)_gameModel.activePlayer()).submitWord();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);

        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(PlayerState.SELECTING_LETTER, ((UserPlayer)_gameModel.activePlayer()).state());
        assertEquals(expectedEvents, _events);

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        assertEquals(3, ((UserPlayer)_gameModel.activePlayer()).scoreCounter().score());
    }

    @Test
    public void gameFinished_playerTryToSkipTurn() {
        _gameModel.startGame();

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        ((UserPlayer)_gameModel.activePlayer()).skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.GAME_IS_FINISHED);

        assertThrows(IllegalArgumentException.class, () -> ((UserPlayer)_gameModel.activePlayer()).skipTurn());
        assertNull(_gameModel.gameField().changedCell());
        assertNull(_gameModel.alphabet().selectedLetter());
        assertEquals(GameState.FINISHED, _gameModel.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void gameFinished_playerTryToSelectLetter() {
        _gameModel.startGame();

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        ((UserPlayer)_gameModel.activePlayer()).skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.GAME_IS_FINISHED);

        assertThrows(IllegalArgumentException.class, () -> ((UserPlayer)_gameModel.activePlayer()).selectLetter('а'));
        assertNull(_gameModel.gameField().changedCell());
        assertNull(_gameModel.alphabet().selectedLetter());
        assertEquals(GameState.FINISHED, _gameModel.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void gameFinished_playerTryToSelectCell() {
        _gameModel.startGame();

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        ((UserPlayer)_gameModel.activePlayer()).skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.GAME_IS_FINISHED);

        assertThrows(IllegalArgumentException.class, () -> ((UserPlayer)_gameModel.activePlayer()).selectCell(_gameModel.gameField().cell(new Point(0, 0))));
        assertNull(_gameModel.gameField().changedCell());
        assertNull(_gameModel.alphabet().selectedLetter());
        assertEquals(GameState.FINISHED, _gameModel.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void gameFinished_playerTryToCancelAction() {
        _gameModel.startGame();

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        ((UserPlayer)_gameModel.activePlayer()).skipTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED);
        expectedEvents.add(EVENT.GAME_IS_FINISHED);

        assertThrows(IllegalArgumentException.class, () -> ((UserPlayer)_gameModel.activePlayer()).cancelActionOnField());
        assertNull(_gameModel.gameField().changedCell());
        assertNull(_gameModel.alphabet().selectedLetter());
        assertEquals(GameState.FINISHED, _gameModel.state());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void startGame_againstAIPlayer() {
        _gameModel = new GameModel(5, 5, true);
        _gameModel.addGameModelListener(new GameListener());

        _events.clear();

        _gameModel.startGame();

        ((UserPlayer)_gameModel.activePlayer()).skipTurn();
        AbstractPlayer currentPlayer = _gameModel.activePlayer();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_START_WORD);
        expectedEvents.add(EVENT.PLAYER_EXCHANGED); // UserPlayer turn (start game)
        expectedEvents.add(EVENT.PLAYER_EXCHANGED); // AIPlayer turn (bcs UserPlayer skipped turn)
        expectedEvents.add(EVENT.PLAYER_EXCHANGED); // UserPlayer turn (after AIPlayer turn)

        assertInstanceOf(AIPlayer.class, currentPlayer);
        assertEquals(GameState.IN_PROCESS, _gameModel.state());
        assertEquals(expectedEvents, _events);
    }
}
