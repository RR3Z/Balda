package model.ai;

import model.WordsDB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractWordSelectionStrategy {
    protected WordsDB _wordsDB;

    public abstract PlayableWord selectPlayableWord(@NotNull List<PlayableWord> availablePlayableWords);
}
