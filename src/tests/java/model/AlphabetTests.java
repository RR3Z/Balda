package model;

import model.events.AlphabetEvent;
import model.events.AlphabetListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import model.players.UserPlayer;
import model.utils.DataFilePaths;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlphabetTests {
    private Alphabet _alphabet;
    private GameModel _gameModel;
    private enum EVENT {
        SELECTED_LETTER,
        FORGOT_SELECTED_LETTER
    }
    private List<EVENT> _events;

    private class EventsListener implements AlphabetListener {
        @Override
        public void forgotSelectedLetter(AlphabetEvent event) {
            _events.add(EVENT.FORGOT_SELECTED_LETTER);
        }

        @Override
        public void selectedLetter(AlphabetEvent event) {
            _events.add(EVENT.SELECTED_LETTER);
        }
    }

    @BeforeEach
    public void testSetup() {
        _events = new ArrayList<>();

        _gameModel = new GameModel(5, 5, false);
        _gameModel.startGame();

        _alphabet = _gameModel.alphabet();
        _alphabet.addAlphabetListener(new EventsListener());
    }

    @Test
    public void selectLetter_LetterIsNull() {
        Character letter = null;

        assertThrows(IllegalArgumentException.class, () -> _alphabet.selectLetter(letter));

        assertEquals(letter, _alphabet.selectedLetter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void selectLetter_UnknownLetter() {
        Character letter = 'b';

        assertFalse(_alphabet.selectLetter(letter));

        assertNull(_alphabet.selectedLetter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void selectLetter_KnownLetter() {
        Character letter = 'а';

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SELECTED_LETTER);

        assertTrue(_alphabet.selectLetter(letter));

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void selectLetter_LetterAlreadySelected() {
        Character letter = 'а';

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SELECTED_LETTER);

        assertTrue(_alphabet.selectLetter(letter));
        assertThrows(IllegalArgumentException.class, () -> _alphabet.selectLetter(letter));

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void forgetSelectedLetter_SelectedLetterIsNotNull() {
        Character letter = 'а';

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SELECTED_LETTER);
        expectedEvents.add(EVENT.FORGOT_SELECTED_LETTER);

        assertTrue(_alphabet.selectLetter(letter));
        _alphabet.forgetSelectedLetter();

        assertNull(_alphabet.selectedLetter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void forgetSelectedLetter_SelectedLetterIsNull() {
        _alphabet.forgetSelectedLetter();

        assertNull(_alphabet.selectedLetter());
        assertTrue(_events.isEmpty());
    }

    @Test
    public void forgetSelectedLetter_AfterPlayerExchanged() {
        Character letter = 'а';

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SELECTED_LETTER);
        expectedEvents.add(EVENT.FORGOT_SELECTED_LETTER);

        assertTrue(_alphabet.selectLetter(letter));
        ((UserPlayer)_gameModel.activePlayer()).skipTurn();

        assertNull(_alphabet.selectedLetter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void selectedLetter_LetterIsNotNull() {
        Character letter = 'а';
        _alphabet.selectLetter(letter);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SELECTED_LETTER);

        assertEquals(letter, _alphabet.selectedLetter());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void selectedLetter_LetterIsNull() {
        assertNull(_alphabet.selectedLetter());
        assertTrue(_events.isEmpty());
    }
    
    @Test
    public void isLetterAvailable_KnownLowercaseLetter() {
        Character letter = 'ф';

        assertTrue(_alphabet.isLetterAvailable(letter));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void isLetterAvailable_UnknownLetter() {
        Character letter = 'b';

        assertFalse(_alphabet.isLetterAvailable(letter));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void isLetterAvailable_KnownUppercaseLetter() {
        Character letter = 'Ф';

        assertFalse(_alphabet.isLetterAvailable(letter));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void isLetterAvailable_Null() {
        assertThrows(IllegalArgumentException.class, () -> _alphabet.isLetterAvailable(null));
        assertTrue(_events.isEmpty());
    }
}
