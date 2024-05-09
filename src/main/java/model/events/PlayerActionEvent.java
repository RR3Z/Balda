package model.events;

import model.Cell;
import model.Player;
import java.util.EventObject;

public class PlayerActionEvent extends EventObject {
    public PlayerActionEvent(Object source) {
        super(source);
    }

    private Player _player;

    public void setPlayer(Player player) {
        _player = player;
    }

    public Player player() {
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

    private boolean _isKnown;

    public void setIsKnown(boolean status) {
        _isKnown = status;
    }

    public boolean isKnown() {
        return _isKnown;
    }

    private boolean _isUsedAlready;

    public void setIsUsedAlready(boolean status) {
        _isUsedAlready = status;
    }

    public boolean isUsedAlready() {
        return _isUsedAlready;
    }

    private boolean _isNotNeighborOfLastCell;

    public void setIsNotNeighborOfLastCell(boolean status) {
        _isNotNeighborOfLastCell = status;
    }

    public boolean isNotNeighborOfLastCell() {
        return _isNotNeighborOfLastCell;
    }

    private boolean _isContainCellAlready;

    public void setIsContainCellAlready(boolean status) {
        _isContainCellAlready = status;
    }

    public boolean isContainCellAlready() {
        return _isContainCellAlready;
    }

    private boolean _isCellWithoutLetter;

    public void setIsCellWithoutLetter(boolean status) {
        _isCellWithoutLetter = status;
    }

    public boolean isCellWithoutLetter() {
        return _isCellWithoutLetter;
    }
}
