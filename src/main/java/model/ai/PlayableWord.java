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

    public PlayableWord() {
        _cellsToSelect = new ArrayList<>();
    }

    public PlayableWord(Pair<Cell, Character> letterToPlace, List<Cell> cellsToSelect) {
        _letterToPlace = letterToPlace;
        _cellsToSelect = cellsToSelect;
    }

    // Getters
    public Pair<Cell, Character> letterToPlace() { return _letterToPlace; }

    public List<Cell> cellsToSelect() { return Collections.unmodifiableList(_cellsToSelect); }
}
