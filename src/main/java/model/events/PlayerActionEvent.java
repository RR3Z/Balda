package model.events;

import model.Cell;
import model.players.AbstractPlayer;

import java.util.EventObject;

public class PlayerActionEvent extends EventObject {
    public PlayerActionEvent(Object source) {
        super(source);
    }

    private AbstractPlayer _player;

    public void setPlayer(AbstractPlayer player) {
        _player = player;
    }

    public AbstractPlayer player() {
        return _player;
    }

    private String _word;

    public void setWord(String word) {
        _word = word;
    }

    public String word() {
        return _word;
    }

    private Cell _cell;

    public void setCell(Cell cell) {
        _cell = cell;
    }

    public Cell cell() {
        return _cell;
    }

    private Character _letter;

    public void setLetter(Character letter) {
        _letter = letter;
    }

    public Character letter() {
        return _letter;
    }
}
