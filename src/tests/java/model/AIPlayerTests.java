package model;

import model.enums.Direction;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import model.players.AIPlayer;
import model.players.AbstractPlayer;
import model.utils.DataFilePaths;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AIPlayerTests {
    private AbstractPlayer _player;
    private Alphabet _alphabet;
    private WordsDB _wordsDB;
    private GameField _field;
    private enum EVENT {
        CHANGED_STATE,
        SKIPPED_TURN,
        FINISHED_TURN,
        PLACED_LETTER,
        CHOSE_LETTER,
        CHOSE_CELL,
        SUBMITTED_WORD_WITHOUT_CHANGEABLE_CELL,
        CANCELED_ACTION_ON_FIELD
    }

    private class EventsListener implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHANGED_STATE);
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.SKIPPED_TURN);
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.FINISHED_TURN);
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.PLACED_LETTER);
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHOSE_LETTER);
        }

        @Override
        public void addedCellToWord(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CHOSE_CELL);
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.SUBMITTED_WORD_WITHOUT_CHANGEABLE_CELL);
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            _events.add(EVENT.CANCELED_ACTION_ON_FIELD);
        }
    }
    private final List<EVENT> _events = new ArrayList<>();

    @Test
    public void startTurn_playedWord() {
        _events.clear();

        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);
        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _field = new GameField(5, 5);

        _field.placeWord("балда", _field.centralLineIndex(Direction.RIGHT), Direction.RIGHT);
        _wordsDB.addToUsedWords("балда", null);

        _player = new AIPlayer("BOT", _field, _wordsDB, _alphabet);
        _player.addPlayerActionListener(new EventsListener());

        _player.startTurn();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.PLACED_LETTER);
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.CHOSE_CELL);
        expectedEvents.add(EVENT.FINISHED_TURN);

        assertEquals(expectedEvents, _events);
        assertEquals(PlayerState.WAITING_TURN, _player.state());
    }

    @Test
    public void startTurn_noAvailableWords() {
        _events.clear();

        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);
        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _field = new GameField(5, 5);

        _player = new AIPlayer("BOT", _field, _wordsDB, _alphabet);
        _player.addPlayerActionListener(new EventsListener());

        _player.startTurn();

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.CHANGED_STATE);
        expectedEvents.add(EVENT.SKIPPED_TURN);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedEvents, _events);
        assertEquals(PlayerState.SKIPPED_TURN, _player.state());
    }
}
