package model.events;

import model.Player;
import model.ScoreCounter;

import java.util.EventObject;

public class ScoreCounterEvent extends EventObject {
    public ScoreCounterEvent(Object source) {
        super(source);
    }

    private ScoreCounter _scoreCounter;
    public void setScoreCounter(ScoreCounter scoreCounter) {
        _scoreCounter = scoreCounter;
    }

    public ScoreCounter scoreCounter() {
        return _scoreCounter;
    }
}
