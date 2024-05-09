package model;

import model.enums.Direction;
import model.enums.GameState;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import org.jetbrains.annotations.NotNull;
import ui.GameFieldWidget;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class GameModel {
    private GameField _field;
    private WordsDB _wordsDB;
    private Alphabet _alphabet;
    private List<Player> _players = new ArrayList<>();
    private Player _activePlayer;
    private GameState _state;

    public GameModel(int width, int height) {
        _field = new GameField(width, height);
        _wordsDB = new WordsDB();
        _alphabet = new Alphabet();

        Player firstPlayer = new Player("Player 1", _alphabet, _wordsDB, _field);
        firstPlayer.addPlayerActionListener(new PlayerObserve());
        _players.add(firstPlayer);

        Player secondPlayer = new Player("Player 2", _alphabet, _wordsDB, _field);
        secondPlayer.addPlayerActionListener(new PlayerObserve());
        _players.add(secondPlayer);

        _state = GameState.WAITING_START;
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

        // Нахожу игрока с наибольшим количеством очков
        Player playerWithMostScore = _players.get(0);
        for (int i = 1; i < _players.size(); i++) {
            if (playerWithMostScore.scoreCounter().score() < _players.get(i).scoreCounter().score()) {
                playerWithMostScore = _players.get(i);
            }
        }
        // Считаю игрока с наибольшим количеством очков победителем
        winners.add(playerWithMostScore);

        // Смотрю, есть ли еще игроки с таким же количеством очков и если есть, то тоже считаю их победителем
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
        int centralRowIndex = _field.centralRowIndex(direction);
        _field.placeWord(word, centralRowIndex, direction);
        _wordsDB.addToUsedWords(word, null);

        fireDefinedStartWord(word);
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

    public Player activePlayer() {
        return _activePlayer;
    }

    public GameField gameField() { return _field; }

    public GameState state() {
        return _state;
    }

    /* ============================================================================================================== */
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
        public void submittedWord(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                _activePlayer.scoreCounter().increaseScore(event.word().length());
                firePlayerSubmittedWord(event.player(), event.word());
            }
        }

        @Override
        public void addedNewWordToDictionary(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerAddedNewWordToDictionary(event.player(), event.word());
            }
        }

        @Override
        public void failedToAddNewWordToDictionary(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerFailedToAddNewWordToDictionary(event.player());
            }
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerChoseCell(event.player(), event.cell());
            }
        }

        @Override
        public void choseWrongCell(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerChoseWrongCell(event.player(), event.cell());
            }
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerPlacedLetter(event.player(), event.cell());
            }
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerSubmittedWordWithoutChangeableCell(event.player(), event.cell());
            }
        }

        @Override
        public void failedToSubmitWord(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerFailedToSubmitWord(event.player(), event.word(), event.isKnown(), event.isUsedAlready());
            }
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            if (event.player() == _activePlayer) {
                firePlayerCanceledActionOnField(event.player());
            }
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

    private void fireDefinedStartWord(@NotNull String word) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setWord(word);

            ((GameModelListener) listener).definedStartWord(event);
        }
    }

    private void fireGameIsFinished(@NotNull List<Player> winners) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setWinners(winners);

            ((GameModelListener) listener).gameIsFinished(event);
        }
    }

    private void firePlayerSubmittedWord(@NotNull Player player,@NotNull String word) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((GameModelListener) listener).playerSubmittedWord(event);
        }
    }
    
    private void firePlayerAddedNewWordToDictionary(@NotNull Player player,@NotNull String word) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((GameModelListener) listener).playerAddedNewWordToDictionary(event);
        }
    }

    private void firePlayerFailedToAddNewWordToDictionary(@NotNull Player player) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);

            ((GameModelListener) listener).playerFailedToAddNewWordToDictionary(event);
        }
    }

    private void firePlayerChoseCell(@NotNull Player player, @NotNull Cell cell) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);
            event.setCell(cell);

            ((GameModelListener) listener).playerChoseCell(event);
        }
    }

    private void firePlayerChoseWrongCell(@NotNull Player player, @NotNull Cell cell) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);
            event.setCell(cell);

            ((GameModelListener) listener).playerChoseWrongCell(event);
        }
    }

    private void firePlayerPlacedLetter(@NotNull Player player, @NotNull Cell cell) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);
            event.setCell(cell);

            ((GameModelListener) listener).playerPlacedLetter(event);
        }
    }

    private void firePlayerSubmittedWordWithoutChangeableCell(@NotNull Player player, @NotNull Cell cell) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);
            event.setCell(cell);

            ((GameModelListener) listener).playerSubmittedWordWithoutChangeableCell(event);
        }
    }

    private void firePlayerFailedToSubmitWord(@NotNull Player player, @NotNull String word, boolean isKnown, boolean isUsedAlready) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);
            event.setWord(word);
            event.setIsKnown(isKnown);
            event.setIsUsedAlready(isUsedAlready);

            ((GameModelListener) listener).playerFailedToSubmitWord(event);
        }
    }

    private void firePlayerCanceledActionOnField(@NotNull Player player) {
        for (Object listener : _gameModelListeners) {
            GameModelEvent event = new GameModelEvent(this);
            event.setPlayer(player);

            ((GameModelListener) listener).playerCanceledActionOnField(event);
        }
    }
}
