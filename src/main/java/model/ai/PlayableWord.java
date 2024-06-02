package model.ai;

import model.Cell;

import javafx.util.Pair;

import java.util.*;

public class PlayableWord {
    private Pair<Cell, Character> _letterToPlace;
    private List<Cell> _cellsToSelect;

    public PlayableWord(Character letter, Cell cellForLetter, List<Cell> cellsToSelect) {
        _letterToPlace = new Pair<>(cellForLetter, letter);
        _cellsToSelect = new ArrayList<>(cellsToSelect);
    }

    // Getters
    public Character letter() { return _letterToPlace.getValue(); }

    public Cell cellForLetter() { return _letterToPlace.getKey(); }

    public List<Cell> cellsToSelect() { return Collections.unmodifiableList(_cellsToSelect); }

    public String toString() {
        StringBuilder word = new StringBuilder();
        for(Cell cell: _cellsToSelect) {
            if(cell.letter() != null) {
                word.append(cell.letter());
            } else {
                word.append(letter());
            }
        }
        return word.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayableWord that = (PlayableWord) o;
        return Objects.equals(_letterToPlace, that._letterToPlace) &&
                Objects.equals(_cellsToSelect, that._cellsToSelect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_letterToPlace, _cellsToSelect);
    }
}
