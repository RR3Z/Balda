package model.ai;

import model.WordsDB;

import java.util.List;

public abstract class AbstractWordSelectionStrategy {
    protected AbstractWordSearchStrategy _wordSearchStrategy;
    protected WordsDB _wordsDB;

    public abstract PlayableWord selectPlayableWord();
}
