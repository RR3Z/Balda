package model;

import model.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameFieldTests {
    private GameField _field = new GameField(5, 5);

    public GameFieldTests() {
    }

    @BeforeEach
    public void testSetup() {
        Cell cell1 = _field.cell(new Point(0, 0));
        cell1.setLetter('д');

        Cell cell2 = _field.cell(new Point(1, 0));
        cell2.setLetter('е');

        Cell cell3 = _field.cell(new Point(2, 0));
        cell3.setLetter('л');

        Cell cell4 = _field.cell(new Point(3, 0));
        cell4.setLetter('а');
    }

    @Test
    public void test_constructor_NegativeWidth() {
        int width = -5;
        int height = 3;

        assertThrows(IllegalArgumentException.class, () -> _field = new GameField(width, height));
    }

    @Test
    public void test_constructor_NegativeHeight() {
        int width = 5;
        int height = -3;

        assertThrows(IllegalArgumentException.class, () -> _field = new GameField(width, height));
    }

    @Test
    public void test_constructor_WidthIsZero() {
        int width = 0;
        int height = 3;

        assertThrows(IllegalArgumentException.class, () -> _field = new GameField(width, height));
    }

    @Test
    public void test_constructor_HeightIsZero() {
        int width = 5;
        int height = 0;

        assertThrows(IllegalArgumentException.class, () -> _field = new GameField(width, height));
    }

    @Test
    public void test_constructor_CheckNeighbors() {
        // Соседи для ячеек задаются после заполнения ячейками поля (см. метод setupCells в GameField)
        int width = 3;
        int height = 3;
        _field = new GameField(width, height);

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> Neighbors: (0, 1), (1, 0)
        Cell checkedCell = _field.cell(new Point(0, 0));
        Cell neighbor = _field.cell(new Point(0, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> Neighbors: (0, 0), (2, 0), (1,1)
        checkedCell = _field.cell(new Point(1, 0));
        neighbor = _field.cell(new Point(0, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(2, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> Neighbors: (1, 0), (2,1)
        checkedCell = _field.cell(new Point(2, 0));
        neighbor = _field.cell(new Point(1, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(2, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> Neighbors: (0, 0), (0, 2), (1,1)
        checkedCell = _field.cell(new Point(0, 1));
        neighbor = _field.cell(new Point(0, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(0, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> Neighbors: (0, 1), (1, 0), (2,1), (1,2)
        checkedCell = _field.cell(new Point(1, 1));
        neighbor = _field.cell(new Point(0, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(2, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> Neighbors: (2, 0), (1, 1), (2,2)
        checkedCell = _field.cell(new Point(2, 1));
        neighbor = _field.cell(new Point(2, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(2, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> Neighbors: (0,1), (1, 2)
        checkedCell = _field.cell(new Point(0, 2));
        neighbor = _field.cell(new Point(0, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> Neighbors: (0, 2), (1, 1), (2,2)
        checkedCell = _field.cell(new Point(1, 2));
        neighbor = _field.cell(new Point(0, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(2, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> Neighbors: (1, 2), (2, 1)
        checkedCell = _field.cell(new Point(2, 2));
        neighbor = _field.cell(new Point(1, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));
        neighbor = _field.cell(new Point(2, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));
    }

    @Test
    public void test_constructor_CheckDiagonalCellsAsNotNeighbors() {
        int width = 3;
        int height = 3;
        _field = new GameField(width, height);

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> Diagonal cells: (0, 1), (1, 0)
        Cell checkedCell = _field.cell(new Point(0, 0));
        Cell notNeighbor = _field.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (1,0) -> Diagonal cells: (0, 1), (2, 1)
        checkedCell = _field.cell(new Point(1, 0));
        notNeighbor = _field.cell(new Point(0, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
        notNeighbor = _field.cell(new Point(2, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (2,0) -> Diagonal cells: (1, 1)
        checkedCell = _field.cell(new Point(2, 0));
        notNeighbor = _field.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (0,1) -> Diagonal cells: (1, 0), (1, 2)
        checkedCell = _field.cell(new Point(0, 1));
        notNeighbor = _field.cell(new Point(1, 0));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
        notNeighbor = _field.cell(new Point(1, 2));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (1,1) -> Diagonal cells: (0, 0), (2, 0), (0, 2), (2, 2)
        checkedCell = _field.cell(new Point(1, 1));
        notNeighbor = _field.cell(new Point(0, 0));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
        notNeighbor = _field.cell(new Point(2, 0));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
        notNeighbor = _field.cell(new Point(0, 2));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
        notNeighbor = _field.cell(new Point(2, 2));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (2,1) -> Diagonal cells: (1, 0), (1, 2)
        checkedCell = _field.cell(new Point(2, 1));
        notNeighbor = _field.cell(new Point(1, 0));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
        notNeighbor = _field.cell(new Point(1, 2));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (0,2) -> Diagonal cells: (1, 1)
        checkedCell = _field.cell(new Point(0, 2));
        notNeighbor = _field.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (1,2) -> Diagonal cells: (0, 1), (2, 1)
        checkedCell = _field.cell(new Point(1, 2));
        notNeighbor = _field.cell(new Point(0, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
        notNeighbor = _field.cell(new Point(2, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));

        // Cell: (2,2) -> Diagonal cells: (1, 1)
        checkedCell = _field.cell(new Point(2, 2));
        notNeighbor = _field.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(notNeighbor));
    }

    @Test
    public void test_constructor_CheckItselfCellIsNotNeighbor() {
        // Соседи для ячеек задаются после заполнения ячейками поля (см. метод setupCells в GameField)
        int width = 3;
        int height = 3;
        _field = new GameField(width, height);

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0)
        Cell checkedCell = _field.cell(new Point(0, 0));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (1,0)
        checkedCell = _field.cell(new Point(1, 0));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (2,0)
        checkedCell = _field.cell(new Point(2, 0));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (0,1)
        checkedCell = _field.cell(new Point(0, 1));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (1,1)
        checkedCell = _field.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (2,1)
        checkedCell = _field.cell(new Point(2, 1));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (0,2)
        checkedCell = _field.cell(new Point(0, 2));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (1,2)
        checkedCell = _field.cell(new Point(1, 2));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (2,2)
        checkedCell = _field.cell(new Point(2, 2));
        assertFalse(checkedCell.isAdjacent(checkedCell));
    }

    @Test
    public void test_width() {
        int expectedWidth = 5;

        assertEquals(expectedWidth, _field.width());
    }

    @Test
    public void test_height() {
        int expectedHeight = 5;

        assertEquals(expectedHeight, _field.height());
    }

    @Test
    public void test_cell_KnownCell() {
        int x = 0;
        int y = 0;
        Point cellPos = new Point(x, y);

        assertNotNull(_field.cell(cellPos));
        assertEquals(x, _field.cell(cellPos).position().getX());
        assertEquals(y, _field.cell(cellPos).position().getY());
        assertEquals('д', _field.cell(cellPos).letter());
    }

    @Test
    public void test_cell_UnknownCell() {
        int x = 15;
        int y = 64;
        Point cellPos = new Point(x, y);

        assertNull(_field.cell(cellPos));
    }

    @Test
    public void test_centralRowIndex_NullDirection() {
        _field = new GameField(3, 7);
        int expectedResult = 2;

        assertThrows(IllegalArgumentException.class, () -> _field.centralLineIndex(null));
    }

    @Test
    public void test_centralRowIndex_VerticalRowAndOddNumberOfColumns() {
        _field = new GameField(3, 7);
        int expectedResult = 2;

        assertEquals(expectedResult, _field.centralLineIndex(Direction.UP));
        assertEquals(expectedResult, _field.centralLineIndex(Direction.DOWN));
    }

    @Test
    public void test_centralRowIndex_VerticalRowAndEvenNumberOfColumns() {
        _field = new GameField(6, 7);
        int expectedResult = 3;

        assertEquals(expectedResult, _field.centralLineIndex(Direction.UP));
        assertEquals(expectedResult, _field.centralLineIndex(Direction.DOWN));
    }

    @Test
    public void test_centralRowIndex_HorizontalRowAndOddNumberOfColumns() {
        _field = new GameField(7, 3);
        int expectedResult = 2;

        assertEquals(expectedResult, _field.centralLineIndex(Direction.LEFT));
        assertEquals(expectedResult, _field.centralLineIndex(Direction.RIGHT));
    }

    @Test
    public void test_centralRowIndex_HorizontalRowAndEvenNumberOfColumns() {
        _field = new GameField(8, 7);
        int expectedResult = 4;

        assertEquals(expectedResult, _field.centralLineIndex(Direction.LEFT));
        assertEquals(expectedResult, _field.centralLineIndex(Direction.RIGHT));
    }

    @Test
    public void test_cellsCountWithoutLetter_FieldIsNotEmpty() {
        int expectedResult = 21;

        assertEquals(expectedResult, _field.cellsCountWithoutLetter());
    }

    @Test
    public void test_cellsCountWithoutLetter_FieldIsEmpty() {
        _field = new GameField(5, 5);
        int expectedResult = 25;

        assertEquals(expectedResult, _field.cellsCountWithoutLetter());
    }

    @Test
    public void test_placeWord_VerticalRowUpToDown() {
        _field = new GameField(4, 4);
        String word = "туча";
        int rowIndex = 0;
        _field.placeWord(word, rowIndex, Direction.DOWN);

        assertEquals('т', _field.cell(new Point(0, 0)).letter());
        assertEquals('у', _field.cell(new Point(0, 1)).letter());
        assertEquals('ч', _field.cell(new Point(0, 2)).letter());
        assertEquals('а', _field.cell(new Point(0, 3)).letter());
    }

    @Test
    public void test_placeWord_VerticalRowDownToUp() {
        _field = new GameField(4, 4);
        String word = "туча";
        int rowIndex = 1;
        _field.placeWord(word, rowIndex, Direction.UP);

        assertEquals('т', _field.cell(new Point(1, 3)).letter());
        assertEquals('у', _field.cell(new Point(1, 2)).letter());
        assertEquals('ч', _field.cell(new Point(1, 1)).letter());
        assertEquals('а', _field.cell(new Point(1, 0)).letter());
    }

    @Test
    public void test_placeWord_HorizontalRowLeftToRight() {
        _field = new GameField(4, 4);
        String word = "туча";
        int rowIndex = 2;
        _field.placeWord(word, rowIndex, Direction.RIGHT);

        assertEquals('т', _field.cell(new Point(0, 2)).letter());
        assertEquals('у', _field.cell(new Point(1, 2)).letter());
        assertEquals('ч', _field.cell(new Point(2, 2)).letter());
        assertEquals('а', _field.cell(new Point(3, 2)).letter());
    }

    @Test
    public void test_placeWord_HorizontalRowRightToLeft() {
        _field = new GameField(4, 4);
        String word = "туча";
        int rowIndex = 3;
        _field.placeWord(word, rowIndex, Direction.LEFT);

        assertEquals('т', _field.cell(new Point(3, 3)).letter());
        assertEquals('у', _field.cell(new Point(2, 3)).letter());
        assertEquals('ч', _field.cell(new Point(1, 3)).letter());
        assertEquals('а', _field.cell(new Point(0, 3)).letter());
    }

    @Test
    public void test_placeWord_HorizontalRowAndWordLengthIsLessThanWidth() {
        _field = new GameField(4, 4);
        String word = "суп";
        int rowIndex = 3;
        _field.placeWord(word, rowIndex, Direction.LEFT);

        assertEquals('с', _field.cell(new Point(3, 3)).letter());
        assertEquals('у', _field.cell(new Point(2, 3)).letter());
        assertEquals('п', _field.cell(new Point(1, 3)).letter());
        assertNull(_field.cell(new Point(0, 3)).letter());
    }

    @Test
    public void test_placeWord_HorizontalRowAndWordLengthIsGreaterThanWidth() {
        _field = new GameField(4, 4);
        String word = "банан";
        int rowIndex = 3;
        _field.placeWord(word, rowIndex, Direction.LEFT);

        assertEquals('б', _field.cell(new Point(3, 3)).letter());
        assertEquals('а', _field.cell(new Point(2, 3)).letter());
        assertEquals('н', _field.cell(new Point(1, 3)).letter());
        assertEquals('а', _field.cell(new Point(0, 3)).letter());
    }

    @Test
    public void test_placeWord_VerticalRowAndWordLengthIsLessThanHeight() {
        _field = new GameField(4, 4);
        String word = "суп";
        int rowIndex = 0;
        _field.placeWord(word, rowIndex, Direction.DOWN);

        assertEquals('с', _field.cell(new Point(0, 0)).letter());
        assertEquals('у', _field.cell(new Point(0, 1)).letter());
        assertEquals('п', _field.cell(new Point(0, 2)).letter());
        assertNull(_field.cell(new Point(0, 3)).letter());
    }

    @Test
    public void test_placeWord_VerticalRowAndWordLengthIsGreaterThanHeight() {
        _field = new GameField(4, 4);
        String word = "банан";
        int rowIndex = 0;
        _field.placeWord(word, rowIndex, Direction.DOWN);

        assertEquals('б', _field.cell(new Point(0, 0)).letter());
        assertEquals('а', _field.cell(new Point(0, 1)).letter());
        assertEquals('н', _field.cell(new Point(0, 2)).letter());
        assertEquals('а', _field.cell(new Point(0, 3)).letter());
    }

    @Test
    public void test_placeWord_NullWord() {
        _field = new GameField(4, 4);
        String word = null;
        int rowIndex = 0;

        assertThrows(IllegalArgumentException.class, () -> _field.placeWord(word, rowIndex, Direction.DOWN));
    }

    @Test
    public void test_placeWord_NegativeRowIndex() {
        _field = new GameField(4, 4);
        String word = "яд";
        int rowIndex = -1;

        assertThrows(IllegalArgumentException.class, () -> _field.placeWord(word, rowIndex, Direction.DOWN));
    }

    @Test
    public void test_placeWord_NonExistentRowIndex() {
        _field = new GameField(4, 4);
        String word = "яд";
        int rowIndex = 15;

        assertThrows(IllegalArgumentException.class, () -> _field.placeWord(word, rowIndex, Direction.DOWN));
    }

    @Test
    public void test_placeWord_NullDirection() {
        _field = new GameField(4, 4);
        String word = "яд";
        int rowIndex = 15;

        assertThrows(IllegalArgumentException.class, () -> _field.placeWord(word, rowIndex, null));
    }
}
