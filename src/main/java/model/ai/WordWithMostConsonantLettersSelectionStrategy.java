package model.ai;

import model.Alphabet;
import model.WordsDB;
import model.utils.RussianLettersPicker;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public class WordWithMostConsonantLettersSelectionStrategy extends AbstractWordSelectionStrategy {
    private List<Character> _consonantLetters;

    public WordWithMostConsonantLettersSelectionStrategy(@NotNull WordsDB wordsDB) {
        _wordsDB = wordsDB;
        _consonantLetters = RussianLettersPicker.consonantLetters();
    }

    @Override
    public PlayableWord selectPlayableWord(@NotNull HashSet<PlayableWord> availablePlayableWords) {
        if(availablePlayableWords.isEmpty()) {
            return null;
        }

        PlayableWord wordWithMostConsonantLetters = null;
        int maxNumberOfConsonantLetters = 0;

        for(PlayableWord word: availablePlayableWords) {
            if (wordWithMostConsonantLetters == null) {
                wordWithMostConsonantLetters = word;
            }

            int currentNumberOfConsonantLetters = countNumberOfConsonantLettersInWord(word);
            if(currentNumberOfConsonantLetters > maxNumberOfConsonantLetters) {
                maxNumberOfConsonantLetters = currentNumberOfConsonantLetters;
                wordWithMostConsonantLetters = word;
            }
        }
        System.out.println(maxNumberOfConsonantLetters + "\t" + countNumberOfConsonantLettersInWord(wordWithMostConsonantLetters));
        return wordWithMostConsonantLetters;
    }

    private int countNumberOfConsonantLettersInWord(@NotNull PlayableWord word) {
        int numberOfConsonantLetters = 0;
        String wordStringRepresentation = word.toString();

        for(int i = 0; i < wordStringRepresentation.length(); i++) {
            if(_consonantLetters.contains(wordStringRepresentation.charAt(i))) {
                numberOfConsonantLetters++;
            }
        }

        return numberOfConsonantLetters;
    }
}
