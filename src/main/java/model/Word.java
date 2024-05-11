package model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private List<Cell> _cells = new ArrayList<>();

    public Word() {
    }

    public Word(@NotNull List<Cell> cells) {
        _cells = cells;
    }

    public String toString() {
        StringBuilder wordStringRepresentation = new StringBuilder();
        for (Cell cell : _cells) {
            wordStringRepresentation.append(cell.letter());
        }

        return wordStringRepresentation.toString();
    }

    public int length() {
        return _cells.size();
    }

    public boolean addLetter(@NotNull Cell cell) {
        if (cell.letter() == null) {
            return false;
        }

        if (containCell(cell)) {
            return false;
        }

        if (!isNeighborOfLastCell(cell)) {
            return false;
        }

        _cells.add(cell);
        return true;
    }

    public boolean containCell(@NotNull Cell cell) {
        return _cells.contains(cell);
    }

    private boolean isNeighborOfLastCell(Cell cell) {
        if(_cells.isEmpty()) {
            return true;
        }

        return (_cells.get(_cells.size() - 1)).isAdjacent(cell);
    }

    public void clear() {
        _cells.clear();
    }

}
