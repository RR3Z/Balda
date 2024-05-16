package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

public class WordTests {
    private GameModel _gameModel;
    private Word _word;
    private GameField _field;

    @BeforeEach
    public void testSetup() {
        _gameModel = new GameModel(5 ,5);
        _word = new Word();
        _field = _gameModel.gameField();

        Cell cell1 = _field.cell(new Point(0, 0));
        cell1.setLetter('д');
        _word.addLetter(cell1);

        Cell cell2 = _field.cell(new Point(1, 0));
        cell2.setLetter('е');
        _word.addLetter(cell2);

        Cell cell3 = _field.cell(new Point(2, 0));
        cell3.setLetter('л');
        _word.addLetter(cell3);

        Cell cell4 = _field.cell(new Point(3, 0));
        cell4.setLetter('а');
        _word.addLetter(cell4);
    }

    @Test
    public void toString_WordContainLetters() {
        String expectedWord = "дела";

        assertEquals(expectedWord, _word.toString());
    }

    @Test
    public void toString_WordIsEmpty() {
        _word = new Word();
        String expectedWord = "";

        assertEquals(expectedWord, _word.toString());
    }

    @Test
    public void length_WordIsNotEmptyAndLengthIsNotZero() {
        int expectedLength = 4;

        assertEquals(expectedLength, _word.length());
    }

    @Test
    public void length_WordIsEmptyAndLengthIsZero() {
        _word = new Word();
        int expectedLength = 0;

        assertEquals(expectedLength, _word.length());
    }

    @Test
    public void addLetter_AddedNullObject() {
        assertThrows(IllegalArgumentException.class, () -> _word.addLetter(null));
    }

    @Test
    public void addLetter_NewLetterIsNotNeighborForWordLastLetter() {
        Cell cell = _field.cell(new Point(3, 3));
        cell.setLetter('к');

        assertFalse(_word.addLetter(cell));
        assertFalse(_word.containCell(cell));
        assertEquals('к', cell.letter());
    }

    @Test
    public void addLetter_DiagonalLetter() {
        Cell cell = _field.cell(new Point(4, 1));
        cell.setLetter('к');

        assertFalse(_word.addLetter(cell));
        assertFalse(_word.containCell(cell));
        assertEquals('к', cell.letter());
    }

    @Test
    public void addLetter_AlreadyContainedCell() {
        Cell cell = _field.cell(new Point(3,1));
        cell.setLetter('к');
        _word.addLetter(cell);

        assertFalse(_word.addLetter(cell));
        assertTrue(_word.containCell(cell));
        assertEquals('к', cell.letter());
    }

    @Test
    public void addLetter_AddedCellWithoutLetter() {
        Cell cell = _field.cell(new Point(4, 0));


        assertFalse(_word.addLetter(cell));
        assertFalse(_word.containCell(cell));
        assertNull(cell.letter());
    }

    @Test
    public void addLetter_AddedCorrectCell() {
        Cell cell = _field.cell(new Point(3,1));
        cell.setLetter('к');

        assertTrue(_word.addLetter(cell));
        assertTrue(_word.containCell(cell));
        assertEquals('к', cell.letter());
    }

    @Test
    public void containCell_ContainedCell() {
        Cell cell = _field.cell(new Point(2,0));

        assertTrue(_word.containCell(cell));
    }

    @Test
    public void containCell_NotContainedCell() {
        Cell cell = _field.cell(new Point(4,0));

        assertFalse(_word.containCell(cell));
    }

    @Test
    public void clear() {
        _word = new Word();

        Cell cell1 = _field.cell(new Point(0, 1));
        cell1.setLetter('д');
        _word.addLetter(cell1);

        Cell cell2 = _field.cell(new Point(1, 1));
        cell2.setLetter('е');
        _word.addLetter(cell2);

        _word.clear();

        assertFalse(_word.containCell(cell1));
        assertFalse(_word.containCell(cell2));
    }
}
