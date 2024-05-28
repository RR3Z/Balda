package model.ai;

import model.WordsDB;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public class LongestWordSelectionStrategy extends AbstractWordSelectionStrategy {
    public LongestWordSelectionStrategy(@NotNull WordsDB wordsDB) {
        _wordsDB = wordsDB;
    }

    @Override
    public PlayableWord selectPlayableWord(@NotNull HashSet<PlayableWord> availablePlayableWords) {
        if(availablePlayableWords.isEmpty()) {
            return null;
        }

        PlayableWord longestWord = null;
        for(PlayableWord word: availablePlayableWords) {
            if (longestWord == null) {
                longestWord = word;
            } else if(word.toString().length() > longestWord.toString().length()) {
                longestWord = word;
            }
        }
        return longestWord;
    }
}
