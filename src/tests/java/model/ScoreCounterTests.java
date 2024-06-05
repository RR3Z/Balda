package model;

import model.events.GameModelEvent;
import model.events.GameModelListener;
import model.events.ScoreCounterEvent;
import model.events.ScoreCounterListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreCounterTests {
    private enum EVENT {
        SCORE_CHANGED
    }
    private final List<EVENT> _events = new ArrayList<>();
    private class ScoreCounterEventsListener implements ScoreCounterListener {
        @Override
        public void scoreChanged(ScoreCounterEvent event) {
            _events.add(EVENT.SCORE_CHANGED);
        }
    }

    private ScoreCounter _scoreCounter;

    @Test
    public void increaseScore_OnPositiveValue() {
        _scoreCounter = new ScoreCounter();
        _scoreCounter.addScoreCounterListener(new ScoreCounterEventsListener());

        int amount = 15;
        _scoreCounter.increaseScore(amount);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SCORE_CHANGED);

        assertEquals(amount, _scoreCounter.score());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void increaseScore_OnNegativeValue() {
        _scoreCounter = new ScoreCounter();
        _scoreCounter.addScoreCounterListener(new ScoreCounterEventsListener());

        int amount = -15;

        assertThrows(IllegalArgumentException.class, () -> _scoreCounter.increaseScore(amount));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void increaseScore_OnZeroValue() {
        _scoreCounter = new ScoreCounter();
        _scoreCounter.addScoreCounterListener(new ScoreCounterEventsListener());

        int amount = 0;
        _scoreCounter.increaseScore(amount);

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SCORE_CHANGED);

        assertEquals(amount, _scoreCounter.score());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void decreaseScore_ByValidValue() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        _scoreCounter.addScoreCounterListener(new ScoreCounterEventsListener());

        int decreaseValue = 15;
        _scoreCounter.decreaseScore(decreaseValue);

        int expectedValue = startValue - decreaseValue;

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SCORE_CHANGED);

        assertEquals(expectedValue, _scoreCounter.score());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void decreaseScore_ScoreIsZero() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        _scoreCounter.addScoreCounterListener(new ScoreCounterEventsListener());

        int decreaseValue = 16;
        _scoreCounter.decreaseScore(decreaseValue);

        int expectedValue = startValue - decreaseValue;

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SCORE_CHANGED);

        assertEquals(expectedValue, _scoreCounter.score());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void decreaseScore_OnNegativeValue() {
        _scoreCounter = new ScoreCounter(4);
        int decreaseValue = -15;

        assertThrows(IllegalArgumentException.class, () -> _scoreCounter.decreaseScore(decreaseValue));
        assertTrue(_events.isEmpty());
    }

    @Test
    public void decreaseScore_OnZeroValue() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        _scoreCounter.addScoreCounterListener(new ScoreCounterEventsListener());

        int decreaseValue = 0;
        _scoreCounter.decreaseScore(decreaseValue);

        int expectedValue = startValue - decreaseValue;

        List<EVENT> expectedEvents = new ArrayList<>();
        expectedEvents.add(EVENT.SCORE_CHANGED);

        assertEquals(expectedValue, _scoreCounter.score());
        assertEquals(expectedEvents, _events);
    }

    @Test
    public void decreaseScore_MoreThanItContains() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        int decreaseValue = 17;

        assertThrows(IllegalArgumentException.class, () -> _scoreCounter.decreaseScore(decreaseValue));
        assertTrue(_events.isEmpty());
    }
}
