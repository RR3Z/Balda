package model.players;

import model.*;
import model.enums.PlayerState;
import org.jetbrains.annotations.NotNull;

public class UserPlayer extends AbstractPlayer {

    public UserPlayer(@NotNull String name,@NotNull GameField field, @NotNull WordsDB wordsDB, @NotNull Alphabet alphabet) {
        _wordsDB = wordsDB;
        _alphabet = alphabet;
        _field = field;
        _word = new Word();
        _name = name;
        _scoreCounter = new ScoreCounter();

        _state = PlayerState.WAITING_TURN;
    }

    @Override
    public void skipTurn() {
        super.skipTurn();
    }

    @Override
    public void selectLetter(@NotNull Character selectedLetter) {
        super.selectLetter(selectedLetter);
    }

    @Override
    public void addNewWordToDictionary() {
        super.addNewWordToDictionary();
    }

    @Override
    public void cancelActionOnField() {
        super.cancelActionOnField();
    }

    @Override
    public void selectCell(@NotNull Cell selectedCell) {
        super.selectCell(selectedCell);
    }

    @Override
    public void submitWord() {
        super.submitWord();
    }
}
