package model;

import model.events.ScoreCounterEvent;
import model.events.ScoreCounterListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class ScoreCounter {
    private int _score;

    public ScoreCounter() {
        _score = 0;
    }

    public ScoreCounter(int score) {
        _score = score;
    }

    public int score() {
        return _score;
    }

    public void increaseScore(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("ScoreCounter -> increaseScore(): an attempt to increase the score by a negative value");
        }

        _score += amount;
        fireScoreChanged();
    }

    public void decreaseScore(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("ScoreCounter -> decreaseScore(): an attempt to decrease the score by a negative value");
        }

        if (_score - amount < 0) {
            throw new IllegalArgumentException("ScoreCounter -> decreaseScore(): an attempt to reduce the number of points by a value greater than it contains");
        }

        _score -= amount;
        fireScoreChanged();
    }

    // Listeners
    private List<EventListener> _scoreCounterListeners = new ArrayList<>();

    public void addScoreCounterListener(@NotNull ScoreCounterListener listener) {
        _scoreCounterListeners.add(listener);
    }

    private void fireScoreChanged() {
        for (Object listener : _scoreCounterListeners) {
            ScoreCounterEvent event = new ScoreCounterEvent(this);
            event.setScoreCounter(this);

            ((ScoreCounterListener) listener).scoreChanged(event);
        }
    }
}
