package model;

import model.events.WordEvent;
import model.events.WordListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.NoSuchElementException;

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
            fireFailedToAddLetter(cell, false, false, true);
            return false;
        }

        if (containCell(cell)) {
            fireFailedToAddLetter(cell, false, true, false);
            return false;
        }

        if (!isNeighborOfLastCell(cell)) {
            fireFailedToAddLetter(cell, true, false, false);
            return false;
        }

        _cells.add(cell);
        // TODO: ивент об успешности действия?
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

    /* ============================================================================================================== */
    // Listeners
    private List<EventListener> _wordListeners = new ArrayList<>();

    public void addWordListener(@NotNull WordListener listener) {
        _wordListeners.add(listener);
    }

    private void fireFailedToAddLetter(@NotNull Cell cell, boolean isNotNeighborOfLastCell, boolean isContainCellAlready, boolean isCellWithoutLetter) {
        for (Object listener : _wordListeners) {
            WordEvent event = new WordEvent(this);
            event.setCell(cell);
            event.setIsCellWithoutLetter(isCellWithoutLetter);
            event.setIsContainCellAlready(isContainCellAlready);
            event.setIsNotNeighborOfLastCell(isNotNeighborOfLastCell);

            ((WordListener) listener).failedToAddLetter(event);
        }
    }
}
