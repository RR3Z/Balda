package model;

import model.enums.Direction;
import model.events.AlphabetEvent;
import model.events.AlphabetListener;
import model.events.GameFieldEvent;
import model.events.GameFieldListener;
import model.players.UserPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameFieldTests {
    private GameModel _gameModel;
    private GameField _gameField;
    private enum EVENT {
        UNDO_CHANGES_OF_CHANGED_CELL,
        PLACED_WORD
    }
    private List<EVENT> _events;

    private class EventsListener implements GameFieldListener {
        @Override
        public void undoChangesOfChangedCell(GameFieldEvent event) {
            _events.add(EVENT.UNDO_CHANGES_OF_CHANGED_CELL);
        }

        @Override
        public void placedWord(GameFieldEvent event) {
            _events.add(EVENT.PLACED_WORD);
        }
    }

    @BeforeEach
    public void testSetup() {
        _events = new ArrayList<>();

        _gameModel = new GameModel(5, 5, false);
        _gameModel.startGame();

        _gameField = _gameModel.gameField();
        _gameField.addGameFieldListener(new EventsListener());
    }

    @Test
    public void constructor_NegativeWidth() {
        int width = -5;
        int height = 3;

        assertThrows(IllegalArgumentException.class, () -> _gameField = new GameField(width, height));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_NegativeHeight() {
        int width = 5;
        int height = -3;

        assertThrows(IllegalArgumentException.class, () -> _gameField = new GameField(width, height));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_WidthIsZero() {
        int width = 0;
        int height = 3;

        assertThrows(IllegalArgumentException.class, () -> _gameField = new GameField(width, height));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_HeightIsZero() {
        int width = 5;
        int height = 0;

        assertThrows(IllegalArgumentException.class, () -> _gameField = new GameField(width, height));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_ChangedCellIsNullObject() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        assertNull(_gameField.changedCell());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckLeftNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> NO LEFT NEIGHBOR
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(-1, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> Left neighbor: (0, 0)
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(0, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> Left neighbor: (1, 0)
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(1, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> NO LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(-1, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> Left neighbor: (0, 1)
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(0, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> Left neighbor: (1, 1)
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> NO LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(-1, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> Left neighbor: (0, 2)
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(0, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> Left neighbor: (1, 2)
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(1, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckRightNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> Right neighbor: (1, 0)
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(1, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> Right neighbor: (2, 0)
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(2, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> NO RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(3, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> Right neighbor: (1, 1)
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> Right neighbor: (2, 1)
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(2, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> NO RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(3, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> Right neighbor: (1, 2)
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(1, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> Right neighbor: (2, 2)
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(2, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> NO RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(3, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckTopNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> Top neighbor: (0, 1)
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(0, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> Top neighbor: (1, 1)
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> Top neighbor: (2, 1)
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(2, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> Top neighbor: (0, 2)
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(0, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> Top neighbor: (1, 2)
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(1, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> Top neighbor: (2, 2)
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(2, 2));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> NO TOP NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(0, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> NO TOP NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(1, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> NO TOP NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(2, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckBottomNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> NO BOTTOM NEIGHBOR
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(0, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> NO BOTTOM NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(1, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> NO BOTTOM NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(2, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> Bottom neighbor: (0, 0)
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(0, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> Bottom neighbor: (1, 0)
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(1, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> Bottom neighbor: (2, 0)
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(2, 0));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> Bottom neighbor: (0, 1)
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(0, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> Bottom neighbor: (1, 1)
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(1, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> Bottom neighbor: (2, 1)
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(2, 1));
        assertTrue(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckTopRightNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> NO TOP RIGHT NEIGHBOR
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(2, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(3, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(1, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(2, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(3, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(1, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(2, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> NO TOP RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(3, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckTopLeftNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> NO TOP LEFT NEIGHBOR
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(-1, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(0, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(-1, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(0, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(1, 2));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(-1, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(0, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> NO TOP LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(1, 3));
        assertFalse(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckBottomLeftNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> NO BOTTOM LEFT NEIGHBOR
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(-1, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(0, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(1, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(-1, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(0, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(1, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(-1, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(0, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> NO BOTTOM LEFT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckBottomRightNeighbors() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0) -> NO BOTTOM RIGHT NEIGHBOR
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        Cell neighbor = _gameField.cell(new Point(1, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,0) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 0));
        neighbor = _gameField.cell(new Point(2, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,0) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 0));
        neighbor = _gameField.cell(new Point(3, -1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,1) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 1));
        neighbor = _gameField.cell(new Point(1, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,1) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 1));
        neighbor = _gameField.cell(new Point(2, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,1) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 1));
        neighbor = _gameField.cell(new Point(3, 0));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (0,2) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(0, 2));
        neighbor = _gameField.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (1,2) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(1, 2));
        neighbor = _gameField.cell(new Point(2, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        // Cell: (2,2) -> NO BOTTOM RIGHT NEIGHBOR
        checkedCell = _gameField.cell(new Point(2, 2));
        neighbor = _gameField.cell(new Point(3, 1));
        assertFalse(checkedCell.isAdjacent(neighbor));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void constructor_CheckItselfCellIsNotNeighbor() {
        _events.clear();

        int width = 3;
        int height = 3;
        _gameField = new GameField(width, height);
        _gameField.addGameFieldListener(new EventsListener());

        /*
        GameField -> [position(x, y)]:
        [(0,0)] [(1,0)] [(2,0)]
        [(0,1)] [(1,1)] [(2,1)]
        [(0,2)] [(1,2)] [(2,2)]
        */

        // Cell: (0,0)
        Cell checkedCell = _gameField.cell(new Point(0, 0));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (1,0)
        checkedCell = _gameField.cell(new Point(1, 0));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (2,0)
        checkedCell = _gameField.cell(new Point(2, 0));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (0,1)
        checkedCell = _gameField.cell(new Point(0, 1));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (1,1)
        checkedCell = _gameField.cell(new Point(1, 1));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (2,1)
        checkedCell = _gameField.cell(new Point(2, 1));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (0,2)
        checkedCell = _gameField.cell(new Point(0, 2));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (1,2)
        checkedCell = _gameField.cell(new Point(1, 2));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        // Cell: (2,2)
        checkedCell = _gameField.cell(new Point(2, 2));
        assertFalse(checkedCell.isAdjacent(checkedCell));

        assertTrue(_events.isEmpty());
    }

    @Test
    public void width() {
        int expectedWidth = 5;

        assertEquals(expectedWidth, _gameField.width());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void height() {
        int expectedHeight = 5;

        assertEquals(expectedHeight, _gameField.height());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void cell_EmptyCellBelongToGameField() {
        int x = 0;
        int y = 0;
        Point cellPos = new Point(x, y);

        assertNotNull(_gameField.cell(cellPos));
        assertEquals(x, _gameField.cell(cellPos).position().getX());
        assertEquals(y, _gameField.cell(cellPos).position().getY());
        assertNull(_gameField.cell(cellPos).letter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void cell_NotEmptyCellBelongToGameField() {
        int x = 0;
        int y = 2;
        Point cellPos = new Point(x, y);

        assertNotNull(_gameField.cell(cellPos));
        assertEquals(x, _gameField.cell(cellPos).position().getX());
        assertEquals(y, _gameField.cell(cellPos).position().getY());
        assertNotNull(_gameField.cell(cellPos).letter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void cell_CellDoesNotBelongToGameField() {
        int x = 15;
        int y = 64;
        Point cellPos = new Point(x, y);

        assertNull(_gameField.cell(cellPos));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_NullDirection() {
        assertThrows(IllegalArgumentException.class, () -> _gameField.centralLineIndex(null));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_VerticalRow_OddNumberOfColumns_UpDirection() {
        _events.clear();

        _gameField = new GameField(3, 7);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 1;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.UP));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_VerticalRow_OddNumberOfColumns_DownDirection() {
        _events.clear();

        _gameField = new GameField(3, 7);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 1;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.DOWN));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_VerticalRow_EvenNumberOfColumns_UpDirection() {
        _events.clear();

        _gameField = new GameField(6, 7);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 2;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.UP));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_VerticalRow_EvenNumberOfColumns_DownDirection() {
        _events.clear();

        _gameField = new GameField(6, 7);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 2;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.DOWN));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_HorizontalRow_OddNumberOfLines_LeftDirection() {
        _events.clear();

        _gameField = new GameField(7, 3);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 1;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.LEFT));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_HorizontalRow_OddNumberOfLines_RightDirection() {
        _events.clear();

        _gameField = new GameField(7, 3);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 1;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.RIGHT));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_HorizontalRow_EvenNumberOfLines_LeftDirection() {
        _events.clear();

        _gameField = new GameField(7, 8);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 3;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.LEFT));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void centralRowIndex_HorizontalRow_EvenNumberOfLines_RightDirection() {
        _events.clear();

        _gameField = new GameField(7, 8);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 3;

        assertEquals(expectedResult, _gameField.centralLineIndex(Direction.RIGHT));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void cellsCountWithoutLetter_FieldIsNotEmpty() {
        int expectedResult = 20;

        assertEquals(expectedResult, _gameField.cellsCountWithoutLetter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void cellsCountWithoutLetter_FieldIsEmpty() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        int expectedResult = 25;

        assertEquals(expectedResult, _gameField.cellsCountWithoutLetter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void placeWord_VerticalLine_UpToDown() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "балда";
        int rowIndex = 0;
        _gameField.placeWord(word, rowIndex, Direction.DOWN);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('б', _gameField.cell(new Point(0, 0)).letter());
        assertEquals('а', _gameField.cell(new Point(0, 1)).letter());
        assertEquals('л', _gameField.cell(new Point(0, 2)).letter());
        assertEquals('д', _gameField.cell(new Point(0, 3)).letter());
        assertEquals('а', _gameField.cell(new Point(0, 4)).letter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeWord_VerticalLine_DownToUp() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "балда";
        int rowIndex = 1;
        _gameField.placeWord(word, rowIndex, Direction.UP);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('б', _gameField.cell(new Point(1, 4)).letter());
        assertEquals('а', _gameField.cell(new Point(1, 3)).letter());
        assertEquals('л', _gameField.cell(new Point(1, 2)).letter());
        assertEquals('д', _gameField.cell(new Point(1, 1)).letter());
        assertEquals('а', _gameField.cell(new Point(1, 0)).letter());

        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeWord_HorizontalLine_LeftToRight() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "балда";
        int rowIndex = 2;
        _gameField.placeWord(word, rowIndex, Direction.RIGHT);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('б', _gameField.cell(new Point(0, 2)).letter());
        assertEquals('а', _gameField.cell(new Point(1, 2)).letter());
        assertEquals('л', _gameField.cell(new Point(2, 2)).letter());
        assertEquals('д', _gameField.cell(new Point(3, 2)).letter());
        assertEquals('а', _gameField.cell(new Point(4, 2)).letter());
        assertEquals(expectedEvents, _events);

    }

    @Test
    public void placeWord_HorizontalLine_RightToLeft() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "балда";
        int rowIndex = 3;
        _gameField.placeWord(word, rowIndex, Direction.LEFT);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('б', _gameField.cell(new Point(4, 3)).letter());
        assertEquals('а', _gameField.cell(new Point(3, 3)).letter());
        assertEquals('л', _gameField.cell(new Point(2, 3)).letter());
        assertEquals('д', _gameField.cell(new Point(1, 3)).letter());
        assertEquals('а', _gameField.cell(new Point(0, 3)).letter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeWord_HorizontalLine_WordLengthIsLessThanWidth() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "суп";
        int rowIndex = 3;
        _gameField.placeWord(word, rowIndex, Direction.LEFT);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('с', _gameField.cell(new Point(4, 3)).letter());
        assertEquals('у', _gameField.cell(new Point(3, 3)).letter());
        assertEquals('п', _gameField.cell(new Point(2, 3)).letter());
        assertNull(_gameField.cell(new Point(1, 3)).letter());
        assertNull(_gameField.cell(new Point(0, 3)).letter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeWord_HorizontalLine_WordLengthIsGreaterThanWidth() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "неуязвимый";
        int rowIndex = 3;
        _gameField.placeWord(word, rowIndex, Direction.LEFT);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('н', _gameField.cell(new Point(4, 3)).letter());
        assertEquals('е', _gameField.cell(new Point(3, 3)).letter());
        assertEquals('у', _gameField.cell(new Point(2, 3)).letter());
        assertEquals('я', _gameField.cell(new Point(1, 3)).letter());
        assertEquals('з', _gameField.cell(new Point(0, 3)).letter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeWord_VerticalLine_WordLengthIsLessThanHeight() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "суп";
        int rowIndex = 0;
        _gameField.placeWord(word, rowIndex, Direction.DOWN);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('с', _gameField.cell(new Point(0, 0)).letter());
        assertEquals('у', _gameField.cell(new Point(0, 1)).letter());
        assertEquals('п', _gameField.cell(new Point(0, 2)).letter());
        assertNull(_gameField.cell(new Point(0, 3)).letter());
        assertNull(_gameField.cell(new Point(0, 4)).letter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeWord_VerticalLine_WordLengthIsGreaterThanHeight() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        String word = "неуязвимый";
        int rowIndex = 0;
        _gameField.placeWord(word, rowIndex, Direction.DOWN);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.PLACED_WORD);

        assertEquals('н', _gameField.cell(new Point(0, 0)).letter());
        assertEquals('е', _gameField.cell(new Point(0, 1)).letter());
        assertEquals('у', _gameField.cell(new Point(0, 2)).letter());
        assertEquals('я', _gameField.cell(new Point(0, 3)).letter());
        assertEquals('з', _gameField.cell(new Point(0, 4)).letter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void placeWord_WordIsNullObject() {
        String word = null;
        int rowIndex = 0;

        assertThrows(IllegalArgumentException.class, () -> _gameField.placeWord(word, rowIndex, Direction.DOWN));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void placeWord_NegativeRowIndex() {
        String word = "яд";
        int rowIndex = -1;

        assertThrows(IllegalArgumentException.class, () -> _gameField.placeWord(word, rowIndex, Direction.DOWN));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void placeWord_NonExistentRowIndex() {
        String word = "яд";
        int rowIndex = 15;

        assertThrows(IllegalArgumentException.class, () -> _gameField.placeWord(word, rowIndex, Direction.DOWN));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void placeWord_DirectionIsNull() {
        String word = "яд";
        int rowIndex = 0;

        assertThrows(IllegalArgumentException.class, () -> _gameField.placeWord(word, rowIndex, null));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void setChangedCell_CellBelongsToGameField() {
        Point cellPosition = new Point(0, 0);
        Cell cell = _gameField.cell(cellPosition);
        _gameField.setChangedCell(cell);

        assertNotNull(cell);
        assertEquals(cell, _gameField.changedCell());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void setChangedCell_CellDoesNotBelongToGameField() {
        Point cellPosition = new Point(100, 100);
        Cell cell = _gameField.cell(cellPosition);

        assertThrows(IllegalArgumentException.class, () -> _gameField.setChangedCell(cell));
        assertNull(cell);
        assertNull(_gameField.changedCell());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void setChangedCell_CellAlreadyHasBeenSet() {
        Point cellPosition = new Point(0, 0);
        Cell firstCell = _gameField.cell(cellPosition);

        cellPosition = new Point(3, 3);
        Cell secondCell = _gameField.cell(cellPosition);

        _gameField.setChangedCell(firstCell);

        assertThrows(IllegalArgumentException.class, () -> _gameField.setChangedCell(secondCell));
        assertNotNull(firstCell);
        assertEquals(firstCell, _gameField.changedCell());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void setChangedCell_CellIsNullObject() {
        assertThrows(IllegalArgumentException.class, () -> _gameField.setChangedCell(null));
    }

    @Test
    public void forgetChangedCell_WhenPlayerExchanged() {
        assertNull(_gameField.changedCell());

        ((UserPlayer)_gameModel.activePlayer()).selectLetter('б');
        ((UserPlayer)_gameModel.activePlayer()).selectCell(_gameField.cell(new Point(0, 1)));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(_gameField.cell(new Point(0, 1)));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(_gameField.cell(new Point(0, 2)));
        ((UserPlayer)_gameModel.activePlayer()).selectCell(_gameField.cell(new Point(1, 2)));
        ((UserPlayer)_gameModel.activePlayer()).addNewWordToDictionary();

        assertNotNull(_gameField.changedCell());

        ((UserPlayer)_gameModel.activePlayer()).submitWord();

        assertNull(_gameField.changedCell());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void undoChangesOfChangedCell_ChangedCellIsNullObject() {
        _events.clear();

        _gameField = new GameField(5, 5);
        _gameField.addGameFieldListener(new EventsListener());

        Cell cell = _gameField.changedCell();
        _gameField.undoChangesOfChangedCell();

        assertNull(cell);
        assertNull(_gameField.changedCell());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void undoChangesOfChangedCell_ChangedCellIsNotNullObject() {
        Cell cell = _gameField.cell(new Point(0, 0));
        _gameField.setChangedCell(cell);

        assertNotNull(_gameField.changedCell());

        _gameField.undoChangesOfChangedCell();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.UNDO_CHANGES_OF_CHANGED_CELL);

        assertEquals(cell, _gameField.changedCell());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void undoChangesOfChangedCell_LetterIsSpecifiedInChangedCell() {
        Cell changedCell = _gameField.cell(new Point(0, 0));
        Character letter = 'а';
        changedCell.setLetter(letter);

        _gameField.setChangedCell(_gameField.cell(new Point(0, 0)));

        assertNotNull(_gameField.changedCell());
        assertNotNull(_gameField.changedCell().letter());

        _gameField.undoChangesOfChangedCell();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.UNDO_CHANGES_OF_CHANGED_CELL);

        assertNull(changedCell.letter());
        assertEquals(changedCell, _gameField.changedCell());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void undoChangesOfChangedCell_LetterIsNotSpecifiedInChangedCell() {
        Cell cell = _gameField.cell(new Point(0, 0));

        _gameField.setChangedCell(cell);

        assertEquals(cell, _gameField.changedCell());
        assertNull(_gameField.changedCell().letter());

        _gameField.undoChangesOfChangedCell();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.UNDO_CHANGES_OF_CHANGED_CELL);

        assertEquals(cell, _gameField.changedCell());
        assertEquals(expectedEvents, _events);
    }
}
