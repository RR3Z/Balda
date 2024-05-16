package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CellTests {
    private Cell _cell;

    @BeforeEach
    public void testSetup() {
        _cell = new Cell();
    }

    @Test
    public void setLetter_InEmptyCell() {
        _cell.setLetter('a');

        assertEquals('a', _cell.letter());
    }

    @Test
    public void setLetter_InCellWithLetter() {
        _cell.setLetter('a');

        assertThrows(IllegalArgumentException.class, () -> _cell.setLetter('b'));
        assertEquals('a', _cell.letter());
    }

    @Test
    public void setLetter_Null() {
        assertThrows(IllegalArgumentException.class, () -> _cell.setLetter(null));
    }

    @Test
    public void setPosition_InEmptyCell() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        assertEquals(pos, _cell.position());
    }

    @Test
    public void setPosition_InCellWithPosition() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        assertThrows(IllegalArgumentException.class, () -> _cell.setPosition(new Point(1, 1)));
        assertEquals(pos, _cell.position());
    }

    @Test
    public void setPosition_Null() {
        assertThrows(IllegalArgumentException.class, () -> _cell.setPosition(null));
    }

    @Test
    public void removeLetter_FromCellWithLetter() {
        _cell.setLetter('a');
        _cell.removeLetter();

        assertNull(_cell.letter());
    }

    @Test
    public void removeLetter_FromCellWithoutLetter() {
        _cell.removeLetter();

        assertNull(_cell.letter());
    }

    @Test
    public void setAdjacentCell_AdjacentRight() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point adjacentPos = new Point((int) pos.getX() + 1, (int) pos.getY());
        Cell adjacentCell = new Cell(adjacentPos);

        _cell.setAdjacentCell(adjacentCell);

        assertTrue(_cell.isAdjacent(adjacentCell));
    }

    @Test
    public void setAdjacentCell_AdjacentLeft() {
        Point pos = new Point(1, 1);
        _cell.setPosition(pos);

        Point adjacentPos = new Point((int) pos.getX() - 1, (int) pos.getY());
        Cell adjacentCell = new Cell(adjacentPos);

        _cell.setAdjacentCell(adjacentCell);

        assertTrue(_cell.isAdjacent(adjacentCell));
    }

    @Test
    public void setAdjacentCell_AdjacentTop() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point adjacentPos = new Point((int) pos.getX(), (int) pos.getY() + 1);
        Cell adjacentCell = new Cell(adjacentPos);

        _cell.setAdjacentCell(adjacentCell);

        assertTrue(_cell.isAdjacent(adjacentCell));
    }

    @Test
    public void setAdjacentCell_AdjacentBottom() {
        Point pos = new Point(1, 1);
        _cell.setPosition(pos);

        Point adjacentPos = new Point((int) pos.getX(), (int) pos.getY() - 1);
        Cell adjacentCell = new Cell(adjacentPos);

        _cell.setAdjacentCell(adjacentCell);

        assertTrue(_cell.isAdjacent(adjacentCell));
    }

    @Test
    public void setAdjacentCell_NotAdjacentTopLeftCell() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point notAdjacentPos = new Point((int) pos.getX() - 1, (int) pos.getY() + 1);
        Cell notAdjacentCell = new Cell(notAdjacentPos);

        _cell.setAdjacentCell(notAdjacentCell);

        assertFalse(_cell.isAdjacent(notAdjacentCell));
    }

    @Test
    public void setAdjacentCell_NotAdjacentTopRightCell() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point notAdjacentPos = new Point((int) pos.getX() + 1, (int) pos.getY() + 1);
        Cell notAdjacentCell = new Cell(notAdjacentPos);

        _cell.setAdjacentCell(notAdjacentCell);

        assertFalse(_cell.isAdjacent(notAdjacentCell));
    }

    @Test
    public void setAdjacentCell_NotAdjacentBottomLeftCell() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point notAdjacentPos = new Point((int) pos.getX() - 1, (int) pos.getY() - 1);
        Cell notAdjacentCell = new Cell(notAdjacentPos);

        _cell.setAdjacentCell(notAdjacentCell);

        assertFalse(_cell.isAdjacent(notAdjacentCell));
    }

    @Test
    public void setAdjacentCell_NotAdjacentBottomRightCell() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point notAdjacentPos = new Point((int) pos.getX() + 1, (int) pos.getY() - 1);
        Cell notAdjacentCell = new Cell(notAdjacentPos);

        _cell.setAdjacentCell(notAdjacentCell);

        assertFalse(_cell.isAdjacent(notAdjacentCell));
    }

    @Test
    public void setAdjacentCell_NotAdjacentCell() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point notAdjacentPos = new Point((int) pos.getX() + 64, (int) pos.getY() + 64);
        Cell notAdjacentCell = new Cell(notAdjacentPos);

        _cell.setAdjacentCell(notAdjacentCell);

        assertFalse(_cell.isAdjacent(notAdjacentCell));
    }

    @Test
    public void setAdjacentCell_ItSelf() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        _cell.setAdjacentCell(_cell);

        assertFalse(_cell.isAdjacent(_cell));
    }

    @Test
    public void setAdjacentCell_Null() {
        assertThrows(IllegalArgumentException.class, () -> _cell.setAdjacentCell(null));
    }

    @Test
    public void areNeighborsWithLetter_hasNeighborWithLetter() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        Point adjacentPos = new Point(0, 1);
        Cell adjacentCell = new Cell(adjacentPos);
        adjacentCell.setLetter('а');

        _cell.setAdjacentCell(adjacentCell);

        assertTrue(_cell.isNeighborWithLetter());
    }

    @Test
    public void areNeighborsWithLetter_hasNoNeighborWithLetter() {
        Point pos = new Point(0, 0);
        _cell.setPosition(pos);

        assertFalse(_cell.isNeighborWithLetter());
    }
}
