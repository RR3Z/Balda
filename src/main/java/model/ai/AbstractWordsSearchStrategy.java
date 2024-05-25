package model.ai;

import model.GameField;
import model.WordsDB;

import java.util.HashSet;
import java.util.List;

public abstract class AbstractWordsSearchStrategy {
    protected GameField _gameField;
    protected WordsDB _wordsDB;

    public abstract HashSet<PlayableWord> findAvailablePlayableWords();
}
