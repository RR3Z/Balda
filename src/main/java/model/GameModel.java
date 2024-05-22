package model;

import model.enums.Direction;
import model.enums.GameState;
import model.events.*;
import model.utils.DataFilePaths;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameModel {
    private GameField _field;
    private WordsDB _wordsDB;
    private Alphabet _alphabet;
    private List<Player> _players = new ArrayList<>();
    private Player _activePlayer;
    private GameState _state;

    public GameModel(int width, int height) {
        _field = new GameField(this, width, height);
        _alphabet = new Alphabet(this, DataFilePaths.ALPHABET_FILE_PATH);

        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _wordsDB.addWordsDBListener(new WordsDBObserve());

        Player firstPlayer = new Player("Игрок 1", _alphabet, _wordsDB, _field);
        firstPlayer.addPlayerActionListener(new PlayerObserve());
        _players.add(firstPlayer);

        Player secondPlayer = new Player("Игрок 2", _alphabet, _wordsDB, _field);
        secondPlayer.addPlayerActionListener(new PlayerObserve());
        _players.add(secondPlayer);

        _state = GameState.WAITING_START;
    }

    public Player activePlayer() {
        return _activePlayer;
    }

    public GameField gameField() { return _field; }

    public Alphabet alphabet() { return _alphabet; }

    public WordsDB wordsDB() { return _wordsDB; }

    public List<Player> players() {
        return Collections.unmodifiableList(_players);
    }

    public GameState state() {
        return _state;
    }

    public void startGame() {
        if (_state != GameState.WAITING_START) {
            throw new IllegalArgumentException("GameModel: wrong \"startGame\" function call (incorrect state)");
        }

        placeStartWord(Direction.RIGHT);

        _activePlayer = _players.get(0);
        _activePlayer.startTurn();

        _state = GameState.IN_PROCESS;
        firePlayerExchanged(_activePlayer);
    }

    private void switchTurn() {
        if (_state != GameState.IN_PROCESS) {
            throw new IllegalArgumentException("GameModel: wrong \"switchTurn\" function call (incorrect state)");
        }

        _activePlayer = nextPlayer();
        _activePlayer.startTurn();

        firePlayerExchanged(_activePlayer);
    }

    private void determineWinner() {
        if (_state != GameState.IN_PROCESS) {
            throw new IllegalArgumentException("GameModel: wrong \"determineWinner\" function call (incorrect state)");
        }

        List<Player> winners = new ArrayList<>();

        // Find the player with the most points
        Player playerWithMostScore = _players.get(0);
        for (int i = 1; i < _players.size(); i++) {
            if (playerWithMostScore.scoreCounter().score() < _players.get(i).scoreCounter().score()) {
                playerWithMostScore = _players.get(i);
            }
        }
        // Consider the player with the most points the winner
        winners.add(playerWithMostScore);

        // Looking to see if there are any other players with the same number of points, and if there are,  also consider them the winner
        for (Player player : _players) {
            if (!winners.contains(player) && player.scoreCounter().score() == playerWithMostScore.scoreCounter().score()) {
                winners.add(player);
            }
        }

        _state = GameState.FINISHED;
        fireGameIsFinished(winners);
    }

    private void placeStartWord(@NotNull Direction direction) {
        if (_state != GameState.WAITING_START) {
            throw new IllegalArgumentException("GameModel: wrong \"placeStartWord\" function call (incorrect state)");
        }

        int wordLength = 0;
        if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            wordLength = _field.width();
        }
        if (direction == Direction.UP || direction == Direction.DOWN) {
            wordLength = _field.height();
        }

        String word = _wordsDB.randomWord(wordLength);
        int centralRowIndex = _field.centralLineIndex(direction);
        _field.placeWord(word, centralRowIndex, direction);
        _wordsDB.addToUsedWords(word, null);

        firePlacedStartWord(word);
    }

    private int numberOfPlayersWhoSkippedTurn() {
        int counter = 0;

        for (Player player : _players) {
            if (player.isSkippedTurn()) {
                counter++;
            }
        }

        return counter;
    }

    private Player nextPlayer() {
        // Find the index of the active player in the list of players
        int activePlayerIndex = _players.indexOf(_activePlayer);
        if (activePlayerIndex < 0) {
            throw new IllegalArgumentException("GameModel -> getNextPlayer(): active player is not in the list of players");
        }

        // Get the player's next turn in order
        int nextPlayerIndex = (activePlayerIndex + 1) % _players.size();
        return _players.get(nextPlayerIndex);
    }

    /* ============================================================================================================== */
    // Player observe
    private class PlayerObserve implements PlayerActionListener {
        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                if (numberOfPlayersWhoSkippedTurn() == _players.size()) {
                    determineWinner();
                } else {
                    switchTurn();
                }
            }
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                if (_field.cellsCountWithoutLetter() > 0) {
                    switchTurn();
                } else {
                    determineWinner();
                }
            }
        }

        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }
    }

    /* ============================================================================================================== */
    // WordsDB observe
    private class WordsDBObserve implements WordsDBListener {
        @Override
        public void addedToUsedWords(WordsDBEvent event) {
            if(event.player() != null && event.player() == _activePlayer) {
                _activePlayer.scoreCounter().increaseScore(event.word().length());
            }
        }

        @Override
        public void addedNewWordToDictionary(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void wordAlreadyUsed(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void wordNotAllowed(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }
    }

    /* ============================================================================================================== */
    // GameModel Listeners
    private List<EventListener> _gameModelListeners = new ArrayList<>();

    public void addGameModelListener(@NotNull GameModelListener listener) {
        _gameModelListeners.add(listener);
    }

    private void firePlayerExchanged(@NotNull Player player) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);

            ((GameModelListener) listener).playerExchanged(event);
        }
    }

    private void fireGameIsFinished(@NotNull List<Player> winners) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setWinners(winners);

            ((GameModelListener) listener).gameIsFinished(event);
        }
    }

    private void firePlacedStartWord(@NotNull String word) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setWord(word);

            ((GameModelListener) listener).placedStartWord(event);
        }
    }
}
