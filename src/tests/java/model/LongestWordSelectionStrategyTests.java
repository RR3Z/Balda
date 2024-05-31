package model;

import model.ai.LongestWordSelectionStrategy;
import model.ai.PlayableWord;
import model.utils.DataFilePaths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LongestWordSelectionStrategyTests {
    private LongestWordSelectionStrategy _wordSelectionStrategy = new LongestWordSelectionStrategy(new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH_FOR_TESTS));

    @Test
    public void pickLongestWord() {
        HashSet<PlayableWord> words = new HashSet<>();

        List<Cell> cellsToSelect = new ArrayList<>();

        // First word
        Cell cellForLetter = new Cell(new Point(0, 0));
        cellsToSelect.add(cellForLetter);
        Cell cellToSelect = new Cell(new Point(1, 0));
        cellToSelect.setLetter('о');
        cellsToSelect.add(cellToSelect);
        cellToSelect = new Cell(new Point(2, 0));
        cellToSelect.setLetter('к');
        cellsToSelect.add(cellToSelect);
        cellToSelect = new Cell(new Point(3, 0));
        cellToSelect.setLetter('а');
        cellsToSelect.add(cellToSelect);
        PlayableWord firstWord = new PlayableWord('п', cellForLetter, cellsToSelect);
        words.add(firstWord);

        // Second word
        cellsToSelect.clear();
        cellForLetter = new Cell(new Point(0, 0));
        cellForLetter.setLetter('к');
        cellsToSelect.add(cellForLetter);
        cellToSelect = new Cell(new Point(1, 0));
        cellsToSelect.add(cellToSelect);
        PlayableWord secondWord = new PlayableWord('у', cellForLetter, cellsToSelect);
        words.add(secondWord);

        PlayableWord longestWord = _wordSelectionStrategy.selectPlayableWord(words);

        assertEquals(firstWord.letter(), longestWord.letter());
        assertEquals(firstWord.cellForLetter(), longestWord.cellForLetter());
        assertEquals(firstWord.cellsToSelect(), longestWord.cellsToSelect());
        assertEquals("пока", longestWord.toString());
    }

    @Test
    public void emptyWordsList() {
        HashSet<PlayableWord> words = new HashSet<>();
        PlayableWord longestWord = _wordSelectionStrategy.selectPlayableWord(words);

        assertNull(longestWord);
    }

    @Test
    public void wordsListIsNull() {
        HashSet<PlayableWord> words = null;

        assertThrows(IllegalArgumentException.class, () -> {_wordSelectionStrategy.selectPlayableWord(words);});
    }

    @Test
    public void pickingWordFromTwoWordsWithSameLength() {
        HashSet<PlayableWord> words = new HashSet<>();

        List<Cell> cellsToSelect = new ArrayList<>();

        // First word
        Cell cellForLetter = new Cell(new Point(0, 0));
        cellsToSelect.add(cellForLetter);
        Cell cellToSelect = new Cell(new Point(1, 0));
        cellToSelect.setLetter('ы');
        cellsToSelect.add(cellToSelect);
        PlayableWord firstWord = new PlayableWord('т', cellForLetter, cellsToSelect);
        words.add(firstWord);

        // Second word
        cellsToSelect.clear();
        cellForLetter = new Cell(new Point(0, 0));
        cellForLetter.setLetter('к');
        cellsToSelect.add(cellForLetter);
        cellToSelect = new Cell(new Point(1, 0));
        cellsToSelect.add(cellToSelect);
        PlayableWord secondWord = new PlayableWord('у', cellForLetter, cellsToSelect);
        words.add(secondWord);

        PlayableWord longestWord = _wordSelectionStrategy.selectPlayableWord(words);

        assertEquals(secondWord.letter(), longestWord.letter());
        assertEquals(secondWord.cellForLetter(), longestWord.cellForLetter());
        assertEquals(secondWord.cellsToSelect(), longestWord.cellsToSelect());
        assertEquals("ку", longestWord.toString());
    }
}
