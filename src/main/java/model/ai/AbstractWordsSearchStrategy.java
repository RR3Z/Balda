package model.ai;

import model.Alphabet;
import model.GameField;
import model.WordsDB;

import java.util.HashSet;
import java.util.List;

public abstract class AbstractWordsSearchStrategy {
    protected GameField _gameField;
    protected WordsDB _wordsDB;
    protected Alphabet _alphabet;

    public abstract HashSet<PlayableWord> findAvailablePlayableWords();
}
