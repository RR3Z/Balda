package model;

import org.jetbrains.annotations.NotNull;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Character _letter;
    private Point _position;
    private List<Cell> _adjacentCells = new ArrayList<>();

    public Cell() {
        _position = null;
        _letter = null;
    }

    public Cell(@NotNull Point position) {
        _position = position;
        _letter = null;
    }

    public Cell(@NotNull Point position, @NotNull Character letter) {
        _position = position;
        _letter = letter;
    }

    public Cell(@NotNull Point position, @NotNull Character letter, @NotNull ArrayList<Cell> adjacentCells) {
        _position = position;
        _letter = letter;
        _adjacentCells = adjacentCells;
    }

    public Character letter() {
        return _letter;
    }

    public void setLetter(@NotNull Character letter) {
        if (_letter != null) {
            throw new IllegalArgumentException("Cell -> setLetter(): trying add letter to Cell with letter");
        }

        _letter = letter;
    }

    public void removeLetter() {
        _letter = null;
    }

    public Point position() {
        return (Point) _position.clone();
    }

    public void setPosition(@NotNull Point position) {
        if (_position != null) {
            throw new IllegalArgumentException("Cell -> setPosition(): trying set position to Cell with position");
        }

        _position = position;
    }

    public void setAdjacentCell(@NotNull Cell cell) {
        if (_adjacentCells.contains(cell) || cell == this) {
            return;
        }

        if (Math.abs(_position.getX() - cell.position().getX()) == 1 && Math.abs(_position.getY() - cell.position().getY()) == 1) {
            return;
        }

        if (Math.abs(_position.getX() - cell.position().getX()) != 1 && Math.abs(_position.getY() - cell.position().getY()) != 1) {
            return;
        }

        _adjacentCells.add(cell);
    }

    public boolean isAdjacent(Cell cell) {
        return _adjacentCells.contains(cell);
    }

    public boolean areNeighborsWithLetter() {
        for (Cell cell: _adjacentCells){
            if(cell.letter() != null){
                return true;
            }
        }

        return false;
    }
}
