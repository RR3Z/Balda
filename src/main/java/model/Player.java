package model;

import model.enums.PlayerState;
import model.events.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Player {
    private String _name;
    private Alphabet _alphabet;
    private WordsDB _wordsDB;
    private GameField _field;
    private Word _word;
    private ScoreCounter _scoreCounter;
    private PlayerState _state;

    public Player(@NotNull String name, @NotNull Alphabet alphabet, @NotNull WordsDB wordsDB, @NotNull GameField field) {
        _wordsDB = wordsDB;
        _alphabet = alphabet;
        _field = field;
        _word = new Word();
        _name = name;
        _scoreCounter = new ScoreCounter();

        _state = PlayerState.WAITING_TURN;
    }

    public ScoreCounter scoreCounter() {
        return _scoreCounter;
    }

    public PlayerState state() {
        return _state;
    }

    public String name() { return _name; }

    public void startTurn() {
        if (_state != PlayerState.WAITING_TURN && _state != PlayerState.SKIPPED_TURN) {
            throw new IllegalArgumentException("Wrong \"startTurn\" function call (incorrect state) for player: " + this._name);
        }

        _word.clear();
        _state = PlayerState.SELECTING_LETTER;
        fireChangedState();
    }

    public void skipTurn() {
        if (_state == PlayerState.WAITING_TURN || _state == PlayerState.SKIPPED_TURN) {
            throw new IllegalArgumentException("Wrong \"skipTurn\" function call (incorrect state) for player: " + this._name);
        }

        if (_field.changedCell() != null) {
            _field.undoChangesOfChangedCell();
        }

        _state = PlayerState.SKIPPED_TURN;
        fireSkippedTurn();
    }

    public void chooseLetter(@NotNull Character letter) {
        if (_state != PlayerState.SELECTING_LETTER) {
            throw new IllegalArgumentException("Wrong \"selectLetter\" function call (incorrect state) for player: " + this._name);
        }

        if(_alphabet.selectLetter(letter)) {
            fireChoseLetter(letter);

            _state = PlayerState.PLACES_LETTER;
            fireChangedState();
        }
    }

    private void placeLetter(@NotNull Character letter) {
        if (_state != PlayerState.PLACES_LETTER) {
            throw new IllegalArgumentException("Wrong \"placeLetter\" function call (incorrect state) for player: " + this._name);
        }

        Cell changedCell = _field.changedCell();
        if (changedCell == null) {
            throw new IllegalArgumentException("Wrong \"placeLetter\" function call (changedCell is null for some reason) for player: " + this._name);
        }

        changedCell.setLetter(letter);
        firePlacedLetter(_alphabet.selectedLetter(), changedCell);

        _state = PlayerState.FORMS_WORD;
        fireChangedState();
    }

    public void addNewWordToDictionary() {
        if (_state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"addNewWord\" function call (incorrect state) for player: " + this._name);
        }

        _wordsDB.addToDictionary(_word.toString(), this);
    }

    public void cancelActionOnField() {
        if (_state != PlayerState.FORMS_WORD && _state != PlayerState.PLACES_LETTER && _state != PlayerState.SELECTING_LETTER) {
            throw new IllegalArgumentException("Wrong \"cancelActionOnField\" function call (incorrect state) for player: " + this._name);
        }

        if (_state == PlayerState.PLACES_LETTER) {
            fireCanceledActionOnField();

            if(_alphabet.selectedLetter() != null) {
                _alphabet.forgetSelectedLetter();
            }

            _state = PlayerState.SELECTING_LETTER;
            fireChangedState();
        }

        if (_state == PlayerState.FORMS_WORD) {
            fireCanceledActionOnField();

            if (_word.length() == 0) {
                _field.undoChangesOfChangedCell();

                _state = PlayerState.PLACES_LETTER;
                fireChangedState();
            }

            if (_word.length() > 0) {
                _word.clear();
            }
        }
    }

    public void chooseCell(@NotNull Cell selectedCell) {
        if (_state != PlayerState.PLACES_LETTER && _state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"chooseCell\" function call (incorrect state) for player: " + this._name);
        }

        if(_state == PlayerState.FORMS_WORD) {
            if (!_word.addLetter(selectedCell)) {
                return;
            }

            fireChoseCell(selectedCell);
        }

        if (_state == PlayerState.PLACES_LETTER) {
            if(_alphabet.selectedLetter() == null){
                throw new IllegalArgumentException("Wrong \"chooseCell\" function call (alphabet selected letter is null for some reason) for player: " + this._name);
            }

            if (_field.changedCell() != null) {
                return;
            }

            if (!selectedCell.isNeighborWithLetter()){
                return;
            }

            if (selectedCell.letter() != null) {
                return;
            }

            _field.setChangedCell(selectedCell);
            placeLetter(_alphabet.selectedLetter());
        }
    }

    public void submitWord() {
        if (_state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"submitWord\" function call (incorrect state) for player: " + this._name);
        }

        Cell changedCell = _field.changedCell();
        if (!_word.containCell(changedCell)) {
            fireSubmittedWordDoesNotContainChangeableCell(changedCell); // TODO: вот это сообщать должно само слово
            return;
        }

        String wordStringRepresentation = _word.toString();
        if (!_wordsDB.addToUsedWords(wordStringRepresentation, this)) {
            return;
        }

        finishTurn();
    }

    private void finishTurn() {
        if (_state == PlayerState.WAITING_TURN || _state == PlayerState.SKIPPED_TURN) {
            throw new IllegalArgumentException("Wrong \"finishTurn\" function call (incorrect state) for player: " + this._name);
        }

        _state = PlayerState.WAITING_TURN;
        fireFinishedTurn();
    }

    public boolean isSkippedTurn() {
        return _state == PlayerState.SKIPPED_TURN;
    }

    // Listeners
    private List<EventListener> _playerListeners = new ArrayList<>();

    public void addPlayerActionListener(@NotNull PlayerActionListener listener) {
        _playerListeners.add(listener);
    }

    private void fireChangedState() {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);

            ((PlayerActionListener) listener).changedState(event);
        }
    }

    private void fireSkippedTurn() {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);

            ((PlayerActionListener) listener).skippedTurn(event);
        }
    }

    private void fireFinishedTurn() {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);

            ((PlayerActionListener) listener).finishedTurn(event);
        }
    }

    private void firePlacedLetter(@NotNull Character letter, @NotNull Cell cell) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setLetter(letter);
            event.setCell(cell);

            ((PlayerActionListener) listener).placedLetter(event);
        }
    }

    private void fireChoseLetter(@NotNull Character letter) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setLetter(letter);

            ((PlayerActionListener) listener).choseLetter(event);
        }
    }

    private void fireChoseCell(@NotNull Cell cell) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setCell(cell);

            ((PlayerActionListener) listener).choseCell(event);
        }
    }

    private void fireCanceledActionOnField() {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setCell(_field.changedCell());

            ((PlayerActionListener) listener).canceledActionOnField(event);
        }
    }

    private void fireSubmittedWordDoesNotContainChangeableCell(Cell changeableCell) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setCell(changeableCell);

            ((PlayerActionListener) listener).submittedWordWithoutChangeableCell(event);
        }
    }
}
