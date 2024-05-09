package model;

import model.events.WordEvent;
import model.events.WordListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordTests {
    private enum EVENT {
        FAILED_TO_ADD_LETTER
    }

    private final List<EVENT> _events = new ArrayList<>();
    private final List<EVENT> _expectedEvents = new ArrayList<>();

    private class EventsListener implements WordListener {

        @Override
        public void failedToAddLetter(WordEvent event) {
            _events.add(EVENT.FAILED_TO_ADD_LETTER);
        }
    }

    private Word _word = new Word();

    private GameField _field = new GameField(5, 5);

    public WordTests() {
    }

    @BeforeEach
    public void testSetup() {
        _events.clear();
        _expectedEvents.clear();

        Cell cell1 = _field.cell(new Point(0, 0));
        cell1.setLetter('д');
        boolean res = _word.addLetter(cell1);

        Cell cell2 = _field.cell(new Point(1, 0));
        cell2.setLetter('е');
        _word.addLetter(cell2);

        Cell cell3 = _field.cell(new Point(2, 0));
        cell3.setLetter('л');
        _word.addLetter(cell3);

        Cell cell4 = _field.cell(new Point(3, 0));
        cell4.setLetter('а');
        _word.addLetter(cell4);

        _word.addWordListener(new EventsListener());
    }

    @Test
    public void test_toString_WordContainLetters() {
        String expectedWord = "дела";

        assertEquals(expectedWord, _word.toString());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_toString_WordDoesNotContainLetters() {
        _word = new Word();
        String expectedWord = "";

        assertEquals(expectedWord, _word.toString());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_length_IsNotZero() {
        int expectedLength = 4;

        assertEquals(expectedLength, _word.length());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_length_IsZero() {
        _word = new Word();
        int expectedLength = 0;

        assertEquals(expectedLength, _word.length());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_addLetter_Null() {
        assertThrows(IllegalArgumentException.class, () -> _word.addLetter(null));
    }

    @Test
    public void test_addLetter_NotNeighborForLastCell() {
        Cell cell = _field.cell(new Point(3, 3));
        cell.setLetter('к');

        _expectedEvents.add(EVENT.FAILED_TO_ADD_LETTER);

        assertFalse(_word.addLetter(cell));
        assertFalse(_word.containCell(cell));
        assertEquals('к', cell.letter());
        assertEquals(_expectedEvents,_events);
    }

    @Test
    public void test_addLetter_ContainedCell() {
        Cell cell = _field.cell(new Point(4,0));
        cell.setLetter('к');
        _word.addLetter(cell);

        _expectedEvents.add(EVENT.FAILED_TO_ADD_LETTER);

        assertFalse(_word.addLetter(cell));
        assertTrue(_word.containCell(cell));
        assertEquals('к', cell.letter());
        assertEquals(_expectedEvents,_events);
    }

    @Test
    public void test_addLetter_CellWithoutLetter() {
        Cell cell = _field.cell(new Point(4, 0));

        _expectedEvents.add(EVENT.FAILED_TO_ADD_LETTER);

        assertFalse(_word.addLetter(cell));
        assertFalse(_word.containCell(cell));
        assertNull(cell.letter());
        assertEquals(_expectedEvents,_events);
    }

    @Test
    public void test_addLetter_CorrectCell() {
        Cell cell = _field.cell(new Point(4,0));
        cell.setLetter('к');

        assertTrue(_word.addLetter(cell));
        assertTrue(_word.containCell(cell));
        assertEquals('к', cell.letter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_containCell_ContainedCell() {
        Cell cell = _field.cell(new Point(2,0));

        assertTrue(_word.containCell(cell));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_containCell_NotContainedCell() {
        Cell cell = _field.cell(new Point(4,0));

        assertFalse(_word.containCell(cell));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void test_clear() {
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
        assertTrue(_events.isEmpty());
    }
}
