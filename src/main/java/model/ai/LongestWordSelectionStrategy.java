package model.ai;

import model.WordsDB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LongestWordSelectionStrategy extends AbstractWordSelectionStrategy {
    public LongestWordSelectionStrategy(@NotNull WordsDB wordsDB) {
        _wordsDB = wordsDB;
    }

    @Override
    public PlayableWord selectPlayableWord(@NotNull List<PlayableWord> availablePlayableWords) {
        if(availablePlayableWords.isEmpty()) {
            return null;
        }

        PlayableWord longestWord = availablePlayableWords.get(0);
        for(PlayableWord word: availablePlayableWords) {
            if (word.toString().length() > longestWord.toString().length()) {
                longestWord = word;
            }
        }
        return longestWord;
    }
}
