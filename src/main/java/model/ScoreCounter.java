package model;

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
    }

    public void decreaseScore(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("ScoreCounter -> decreaseScore(): an attempt to decrease the score by a negative value");
        }

        if (_score - amount < 0) {
            throw new IllegalArgumentException("ScoreCounter -> decreaseScore(): an attempt to reduce the number of points by a value greater than it contains");
        }

        _score -= amount;
    }
}
