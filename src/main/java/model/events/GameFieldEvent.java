package model.events;

import model.Cell;

import java.util.EventObject;

public class GameFieldEvent extends EventObject {
    public GameFieldEvent(Object source) {
        super(source);
    }

    private Cell _cell;
    public void setCell(Cell cell) {
        _cell = cell;
    }
    public Cell cell() {
        return _cell;
    }
}
