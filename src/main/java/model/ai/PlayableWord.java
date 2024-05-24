package model.ai;

import model.Cell;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlayableWord {
    private Pair<Cell, Character> _letterToPlace;
    private List<Cell> _cellsToSelect;

    public PlayableWord(Character letter, Cell cellForLetter, List<Cell> cellsToSelect) {
        _letterToPlace = new Pair<>(cellForLetter, letter);
        _cellsToSelect = cellsToSelect;
    }

    // Getters
    public Character letter() { return _letterToPlace.getValue(); }

    public Cell cellForLetter() { return _letterToPlace.getKey(); }

    public List<Cell> cellsToSelect() { return Collections.unmodifiableList(_cellsToSelect); }
}
