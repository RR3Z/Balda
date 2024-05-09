package model.events;

import model.Cell;

import java.util.EventObject;

public class WordEvent extends EventObject {
    public WordEvent(Object source) {
        super(source);
    }

    private Cell _cell;

    public void setCell(Cell cell) {
        _cell = cell;
    }

    public Cell cell() {
        return _cell;
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
