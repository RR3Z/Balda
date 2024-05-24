package model;

import model.enums.Direction;
import org.jetbrains.annotations.NotNull;

import java.awt.Point;
import java.util.*;

public class Cell {
    private Character _letter;
    private Point _position;
    private Map<Direction, Cell> _adjacentCells = new HashMap<>();

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

    public Character letter() {
        return _letter;
    }

    public List<Cell> adjacentCells() {
        return List.copyOf(_adjacentCells.values());
    }

    public Cell adjacentCellWithoutLetter() {
        // It should be understood that it returns the first available free cell adjacent to this one
        if(haveAdjacentCellWithoutLetter()) {
            for(Cell cell: _adjacentCells.values()) {
                if(cell.letter() == null) {
                    return cell;
                }
            }
        }

        return null;
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
        if (_adjacentCells.containsValue(cell) || cell == this) {
            return;
        }

        // Check diagonal cells
        if (Math.abs(_position.getX() - cell.position().getX()) == 1 && Math.abs(_position.getY() - cell.position().getY()) == 1) {
            return;
        }

        if(cell.position().getY() - _position.getY() == 1 && cell.position().getX() - _position.getX() == 0) {
            _adjacentCells.put(Direction.UP, cell);
        }

        if(_position.getY() - cell.position().getY() == 1 && cell.position().getX() - _position.getX() == 0) {
            _adjacentCells.put(Direction.DOWN, cell);
        }

        if(cell.position().getY() - _position.getY() == 0 && cell.position().getX() - _position.getX() == 1) {
            _adjacentCells.put(Direction.RIGHT, cell);
        }

        if(cell.position().getY() - _position.getY() == 0 && _position.getX() - cell.position().getX() == 1) {
            _adjacentCells.put(Direction.LEFT, cell);
        }
    }

    public boolean isAdjacent(Cell cell) {
        return _adjacentCells.containsValue(cell);
    }

    public boolean isNeighborWithLetter() {
        for (Cell cell: _adjacentCells.values()) {
            if(cell.letter() != null) {
                return true;
            }
        }

        return false;
    }

    public boolean haveAdjacentCellWithoutLetter() {
        for (Cell cell: _adjacentCells.values()){
            if(cell.letter() == null) {
                return true;
            }
        }

        return false;
    }
}
