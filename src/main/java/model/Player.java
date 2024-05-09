package model;

import model.enums.PlayerState;
import model.events.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
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
    private Cell _changeableCell;

    public Player(@NotNull String name, @NotNull Alphabet alphabet, @NotNull WordsDB wordsDB, @NotNull GameField field) {
        _wordsDB = wordsDB;
        _wordsDB.addWordsDBListener(new WordsDBObserve());

        _alphabet = alphabet;
        _field = field;

        _word = new Word();
        _word.addWordListener(new WordObserve());

        _name = name;
        _scoreCounter = new ScoreCounter();

        _state = PlayerState.WAITING_TURN;
    }

    public ScoreCounter scoreCounter() {
        return _scoreCounter;
    }

    public void startTurn() {
        if (_state != PlayerState.WAITING_TURN && _state != PlayerState.SKIPPED_TURN) {
            throw new IllegalArgumentException("Wrong \"startTurn\" function call (incorrect state) for player: " + this._name);
        }

        _changeableCell = null;
        _word.clear();
        _state = PlayerState.PLACES_LETTER;
    }

    public void skipTurn() {
        if (_state != PlayerState.PLACES_LETTER && _state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"skipTurn\" function call (incorrect state) for player: " + this._name);
        }

        if (_changeableCell != null) {
            // Removing the letter placed in the Cell
            _changeableCell.removeLetter();
        }

        _state = PlayerState.SKIPPED_TURN;
        fireSkippedTurn();
    }

    public void placeLetter(@NotNull Character letter) {
        if (_state != PlayerState.PLACES_LETTER) {
            throw new IllegalArgumentException("Wrong \"placeLetter\" function call (incorrect state) for player: " + this._name);
        }

        if (_changeableCell == null) {
            throw new IllegalArgumentException("Wrong \"placeLetter\" function call (changeableCell is null for some reason) for player: " + this._name);
        }

        if (!_alphabet.isLetterAvailable(letter)) {
            throw new IllegalArgumentException("Wrong \"placeLetter\" function call (unknown letter) for player: " + this._name);
        }

        _changeableCell.setLetter(letter);
        _state = PlayerState.FORMS_WORD;
        firePlacedLetter(letter, _changeableCell);
    }

    public void addNewWordToDictionary() {
        if (_state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"addNewWord\" function call (incorrect state) for player: " + this._name);
        }

        String wordStringRepresentation = _word.toString();
        _wordsDB.addToDictionary(wordStringRepresentation);
    }

    public void cancelActionOnField() {
        if (_state != PlayerState.FORMS_WORD && _state != PlayerState.PLACES_LETTER) {
            throw new IllegalArgumentException("Wrong \"cancelActionOnField\" function call (incorrect state) for player: " + this._name);
        }

        if (_state == PlayerState.PLACES_LETTER) {
            _changeableCell = null;
        }

        if (_state == PlayerState.FORMS_WORD) {
            if (_word.length() == 0) {
                _changeableCell.removeLetter();
                _changeableCell = null;
                _state = PlayerState.PLACES_LETTER;
            }

            if (_word.length() > 0) {
                _word.clear();
            }
        }

        fireCanceledActionOnField();
    }

    public void chooseCell(@NotNull Point position) {
        if (_state != PlayerState.PLACES_LETTER && _state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"chooseCell\" function call (incorrect state) for player: " + this._name);
        }

        Cell selectedCell = _field.cell(position);
        if (selectedCell == null) {
            throw new IllegalArgumentException("Wrong \"chooseCell\" function call (the cell is missing from the field) for player: " + this._name);
        }

        if (_state == PlayerState.PLACES_LETTER) {
            if (_changeableCell != null) {
                return;
            }

            if (!selectedCell.areNeighborsWithLetter()){
                return;
            }

            if (selectedCell.letter() != null) {
                fireChoseWrongCell(selectedCell, false, false, false);
                return;
            }

            _changeableCell = selectedCell;
        }

        if (_state == PlayerState.FORMS_WORD) {
            if (!_word.addLetter(selectedCell)) {
                return;
            }
        }

        fireChoseCell(selectedCell);
    }

    public void submitWord() {
        if (_state != PlayerState.FORMS_WORD) {
            throw new IllegalArgumentException("Wrong \"submitWord\" function call (incorrect state) for player: " + this._name);
        }

        if (!_word.containCell(_changeableCell)) {
            fireWordDoesNotContainChangeableCell(_changeableCell);
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

    public PlayerState state() {
        return _state;
    }

    /* ============================================================================================================== */
    // WordsDB observe
    private class WordsDBObserve implements WordsDBListener {
        @Override
        public void addedUsedWord(WordsDBEvent event) {
            fireSubmittedWord(event.word());
        }

        @Override
        public void failedToAddUsedWord(WordsDBEvent event) {
            fireFailedToSubmitWord(event.word(), event.isKnown(), event.isUsedAlready());
        }

        @Override
        public void addedNewWordToDictionary(WordsDBEvent event) {
            fireAddedNewWordToDictionary(event.word());
        }

        @Override
        public void failedToAddNewWordToDictionary(WordsDBEvent event) {
            fireFailedToAddNewWordToDictionary(event.word());
        }
    }

    /* ============================================================================================================== */
    // Word observe
    private class WordObserve implements WordListener {
        @Override
        public void failedToAddLetter(WordEvent event) {
            fireChoseWrongCell(event.cell(), event.isNotNeighborOfLastCell(), event.isContainCellAlready(), event.isCellWithoutLetter());
        }
    }

    /* ============================================================================================================== */
    // Listeners
    private List<EventListener> _playerListeners = new ArrayList<>();

    public void addPlayerActionListener(@NotNull PlayerActionListener listener) {
        _playerListeners.add(listener);
    }

    private void fireSkippedTurn() {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);

            ((PlayerActionListener) listener).skippedTurn(event);
        }
    }

    private void fireAddedNewWordToDictionary(@NotNull String word) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setWord(word);

            ((PlayerActionListener) listener).addedNewWordToDictionary(event);
        }
    }

    private void fireFailedToAddNewWordToDictionary(@NotNull String word) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setWord(word);

            ((PlayerActionListener) listener).failedToAddNewWordToDictionary(event);
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

    private void fireChoseWrongCell(@NotNull Cell cell, boolean isNotNeighborOfLastCell, boolean isContainCellAlready, boolean isCellWithoutLetter) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setCell(cell);
            event.setIsCellWithoutLetter(isCellWithoutLetter);
            event.setIsContainCellAlready(isContainCellAlready);
            event.setIsNotNeighborOfLastCell(isNotNeighborOfLastCell);

            ((PlayerActionListener) listener).choseWrongCell(event);
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

    private void fireWordDoesNotContainChangeableCell(Cell changeableCell) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setCell(changeableCell);

            ((PlayerActionListener) listener).submittedWordWithoutChangeableCell(event);
        }
    }

    private void fireSubmittedWord(@NotNull String word) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setWord(word);

            ((PlayerActionListener) listener).submittedWord(event);
        }
    }

    private void fireFailedToSubmitWord(@NotNull String word, boolean isKnown, boolean isUsedAlready) {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);
            event.setWord(word);
            event.setIsKnown(isKnown);
            event.setIsUsedAlready(isUsedAlready);


            ((PlayerActionListener) listener).failedToSubmitWord(event);
        }
    }

    private void fireFinishedTurn() {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);

            ((PlayerActionListener) listener).finishedTurn(event);
        }
    }

    private void fireCanceledActionOnField() {
        for (Object listener : _playerListeners) {
            PlayerActionEvent event = new PlayerActionEvent(this);
            event.setPlayer(this);

            ((PlayerActionListener) listener).canceledActionOnField(event);
        }
    }
}
