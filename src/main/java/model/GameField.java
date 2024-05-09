package model;

import model.enums.Direction;
import model.factories.CellFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GameField {
    private final int _width;
    private final int _height;
    private Cell[][] _cells;

    public GameField(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("GameField -> constructor: wrong field sizes");
        }

        _width = width;
        _height = height;
        setCells();
    }

    private void setCells() {
        _cells = new Cell[_width][_height];
        CellFactory cellFactory = new CellFactory();

        // Create cells
        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                _cells[j][i] = cellFactory.createCell(new Point(j, i));
            }
        }

        setNeighborsForCells();
    }

    private void setNeighborsForCells() {
        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                // Left cell
                if (j - 1 >= 0) {
                    _cells[j][i].setAdjacentCell(_cells[j - 1][i]);
                }
                // Right cell
                if (j + 1 < _width) {
                    _cells[j][i].setAdjacentCell(_cells[j + 1][i]);
                }
                // Top cell
                if (i - 1 >= 0) {
                    _cells[j][i].setAdjacentCell(_cells[j][i - 1]);
                }
                // Bottom cell
                if (i + 1 < _height) {
                    _cells[j][i].setAdjacentCell(_cells[j][i + 1]);
                }
            }
        }
    }

    public int width() {
        return _width;
    }

    public int height() {
        return _height;
    }

    public Cell cell(@NotNull Point position) {
        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                Point cellPosition = _cells[j][i].position();
                if (cellPosition.getX() == position.getX() && cellPosition.getY() == position.getY()) {
                    return _cells[j][i];
                }
            }
        }

        return null;
    }

    public int centralLineIndex(@NotNull Direction direction) {
        int centralRowIndex = -1;

        if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            centralRowIndex = Math.ceilDiv(_height, 2) - 1;
        }

        if (direction == Direction.UP || direction == Direction.DOWN) {
            centralRowIndex = Math.ceilDiv(_width, 2) - 1;
        }

        return centralRowIndex;
    }

    public void placeWord(@NotNull String word, int rowIndex, @NotNull Direction direction) {
        Cell[] row = getRow(direction, rowIndex);

        // Index of letter in word
        int letterIndex = 0;

        // Determine the direction of movement and adjust the loop values accordingly
        int start = 0;
        int end = 0;
        int step = 0;

        if (direction == Direction.RIGHT || direction == Direction.DOWN) {
            start = 0;
            end = row.length;
            step = 1;
        }

        if (direction == Direction.LEFT || direction == Direction.UP) {
            start = row.length - 1;
            end = -1;
            step = -1;
        }

        // Setup letters in row based on direction
        for (int i = start; i != end; i += step) {
            if (letterIndex == word.length()) {
                // If last letter was used already, break cycle
                break;
            }
            row[i].setLetter(word.charAt(letterIndex));
            letterIndex++;
        }
    }

    private Cell[] getRow(@NotNull Direction direction, int rowIndex) {
        if (rowIndex < 0 ||
                (direction == Direction.RIGHT || direction == Direction.LEFT) && rowIndex >= _height ||
                (direction == Direction.UP || direction == Direction.DOWN) && rowIndex >= _width) {
            throw new IllegalArgumentException("GameField -> getRow(): wrong row index");
        }

        int length = (direction == Direction.RIGHT || direction == Direction.LEFT) ? _width : _height;
        Cell[] row = new Cell[length];

        for (int i = 0; i < length; i++) {
            if (direction == Direction.RIGHT || direction == Direction.LEFT) {
                row[i] = _cells[i][rowIndex];
            }
            if (direction == Direction.UP || direction == Direction.DOWN) {
                row[i] = _cells[rowIndex][i];
            }
        }

        return row;
    }

    public void printField(){
        // TODO: REMOVE THIS
        for(int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++){
                System.out.print(_cells[j][i].letter() + " ");
            }
            System.out.println();
        }
    }

    public int cellsCountWithoutLetter() {
        int counter = 0;

        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                if (_cells[j][i].letter() == null) {
                    counter++;
                }
            }
        }

        return counter;
    }
}
