package model.ai;

import model.WordsDB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LongestWordSelectionStrategy extends AbstractWordSelectionStrategy {
    public LongestWordSelectionStrategy(WordsDB wordsDB, @NotNull AbstractWordSearchStrategy wordSearchStrategy) {
        _wordSearchStrategy = wordSearchStrategy;
        _wordsDB = wordsDB;
    }

    @Override
    public PlayableWord selectPlayableWord() {
        List<PlayableWord> availablePlayableWords = _wordSearchStrategy.findAvailablePlayableWords();

        if(availablePlayableWords.isEmpty()) {
            return null;
        }

        System.out.println("Find " + availablePlayableWords.size() + " words");

        PlayableWord longestWord = availablePlayableWords.get(0);
        for(PlayableWord word: availablePlayableWords) {
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }

        return longestWord;
    }
}
