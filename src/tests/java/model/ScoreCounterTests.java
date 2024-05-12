package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreCounterTests {
    private ScoreCounter _scoreCounter;

    @BeforeEach
    public void testSetup() {
        _scoreCounter = new ScoreCounter();
    }

    @Test
    public void test_increaseScore_OnPositiveValue() {
        int amount = 15;
        _scoreCounter.increaseScore(amount);

        assertEquals(amount, _scoreCounter.score());
    }

    @Test
    public void test_increaseScore_OnNegativeValue() {
        int amount = -15;

        assertThrows(IllegalArgumentException.class, () -> _scoreCounter.increaseScore(amount));
    }

    @Test
    public void test_increaseScore_OnZeroValue() {
        int amount = 0;
        _scoreCounter.increaseScore(amount);

        assertEquals(amount, _scoreCounter.score());
    }

    @Test
    public void test_decreaseScore_ByValidValue() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        int decreaseValue = 15;
        _scoreCounter.decreaseScore(decreaseValue);

        int expectedValue = startValue - decreaseValue;

        assertEquals(expectedValue, _scoreCounter.score());
    }

    @Test
    public void test_decreaseScore_ScoreIsZero() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        int decreaseValue = 16;
        _scoreCounter.decreaseScore(decreaseValue);

        int expectedValue = startValue - decreaseValue;

        assertEquals(expectedValue, _scoreCounter.score());
    }

    @Test
    public void test_decreaseScore_OnNegativeValue() {
        int decreaseValue = -15;

        assertThrows(IllegalArgumentException.class, () -> _scoreCounter.decreaseScore(decreaseValue));
    }

    @Test
    public void test_decreaseScore_OnZeroValue() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        int decreaseValue = 0;
        _scoreCounter.decreaseScore(decreaseValue);

        int expectedValue = startValue - decreaseValue;

        assertEquals(expectedValue, _scoreCounter.score());
    }

    @Test
    public void test_decreaseScore_MoreThanItContains() {
        int startValue = 16;
        _scoreCounter = new ScoreCounter(startValue);
        int decreaseValue = 17;

        assertThrows(IllegalArgumentException.class, () -> _scoreCounter.decreaseScore(decreaseValue));
    }
}
