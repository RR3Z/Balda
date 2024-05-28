package model.players;

import model.*;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public abstract class AbstractPlayer {
    protected String _name;
    protected PlayerState _state;

    protected Alphabet _alphabet;
    protected WordsDB _wordsDB;
    protected GameField _field;
    protected Word _word;
    protected ScoreCounter _scoreCounter;

    // Getters
    public String name() { return _name; }

    public PlayerState state() {
        return _state;
    }

    public ScoreCounter scoreCounter() {
        return _scoreCounter;
    }

    public boolean isSkippedTurn() {
        return _state == PlayerState.SKIPPED_TURN;
    }

    // Logic
    public void startTurn() {
        if (_state != PlayerState.WAITING_TURN && _state != PlayerState.SKIPPED_TURN) {
            throw new IllegalArgumentException("Wrong \"startTurn\" function call (incorrect state) for player: " + this._name);
        }

        _word.clear();
        _field.forgetChangedCell();
        _alphabet.forgetSelectedLetter();
        _state = PlayerState.SELECTING_LETTER;
        fireChangedState();
    }

    protected void skipTurn() {
        if (_state == PlayerState.WAITING_TURN || _state == PlayerState.SKIPPED_TURN) {
            throw new IllegalArgumentException("Wrong \"skipTurn\" function call (incorrect state) for player: " + this._name);
        }

        if (_field.changedCell() != null) {
            _field.undoChangesOfChangedCell();
        }

        _state = PlayerState.SKIPPED_TURN;
        fireSkippedTurn();
    }

    protected void selectLetter(@NotNull Character selectedLetter) {
        if (_state != PlayerState.SELECTING_LETTER) {
            throw new IllegalArgumentException("Wrong \"selectLetter\" function call (incorrect state) for player: " + this._name);
        }

        if(_alphabet.selectLetter(selectedLetter)) {
            fireSelectedLetter(selectedLetter);

            _state = PlayerState.PLACES_LETTER;
            fireChangedState();
        }
    }

    protected void addNewWordToDictionary() {
        if (_state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"addNewWord\" function call (incorrect state) for player: " + this._name);
        }

        _wordsDB.addToDictionary(_word.toString(), this);
    }

    protected void cancelActionOnField() {
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

    protected void selectCell(@NotNull Cell selectedCell) {
        if (_state != PlayerState.PLACES_LETTER && _state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"chooseCell\" function call (incorrect state) for player: " + this._name);
        }

        if(_state == PlayerState.FORMS_WORD) {
            if (!_word.addLetter(selectedCell)) {
                return;
            }

            fireSelectedCell(selectedCell);
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
            placeLetterInCell(_alphabet.selectedLetter());
        }
    }

    protected void submitWord() {
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

    private void placeLetterInCell(@NotNull Character letter) {
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

    private void finishTurn() {
        if (_state == PlayerState.WAITING_TURN || _state == PlayerState.SKIPPED_TURN) {
            throw new IllegalArgumentException("Wrong \"finishTurn\" function call (incorrect state) for player: " + this._name);
        }

        _state = PlayerState.WAITING_TURN;
        fireFinishedTurn();
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

    private void fireSelectedLetter(@NotNull Character letter) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setLetter(letter);

            ((PlayerActionListener) listener).choseLetter(event);
        }
    }

    private void fireSelectedCell(@NotNull Cell cell) {
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

    private void fireSubmittedWordDoesNotContainChangeableCell(@NotNull Cell changeableCell) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setCell(changeableCell);

            ((PlayerActionListener) listener).submittedWordWithoutChangeableCell(event);
        }
    }
}
