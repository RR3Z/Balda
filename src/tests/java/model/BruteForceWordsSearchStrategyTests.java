package model;

import model.ai.BruteForceWordsSearchStrategy;
import model.ai.PlayableWord;
import model.enums.Direction;
import model.utils.DataFilePaths;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BruteForceWordsSearchStrategyTests {
    private GameField _field;
    private WordsDB _wordsDB;
    private Alphabet _alphabet;
    private BruteForceWordsSearchStrategy _wordSearchStrategy;

    @Test
    public void findAvailablePlayableWords_canFindWords() {
        _field = new GameField(3, 3);
        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);
        _wordSearchStrategy = new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet);

        _field.placeWord("пик", _field.centralLineIndex(Direction.RIGHT), Direction.RIGHT);

        HashSet<PlayableWord> actualWordsSet = _wordSearchStrategy.findAvailablePlayableWords();

        HashSet<PlayableWord> expectedWordsSet = new HashSet<>();
        List<Cell> cellsToSelect = new ArrayList<>();

        cellsToSelect.add(_field.cell(new Point(1,2))); // +
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('ц', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('л', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(2,2)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('с', _field.cell(new Point(2,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('т', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(0,0)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('ш', _field.cell(new Point(0,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('п', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1, 0)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        cellsToSelect.add(_field.cell(new Point(0, 1)));
        expectedWordsSet.add(new PlayableWord('ш', _field.cell(new Point(1, 0)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(2,0)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('я', _field.cell(new Point(2,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('ч', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('с', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(0, 2)));
        cellsToSelect.add(_field.cell(new Point(0, 1)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        cellsToSelect.add(_field.cell(new Point(2, 1)));
        expectedWordsSet.add(new PlayableWord('э', _field.cell(new Point(0, 2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1, 0)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        expectedWordsSet.add(new PlayableWord('с', _field.cell(new Point(1, 0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1, 0)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        cellsToSelect.add(_field.cell(new Point(0, 1)));
        expectedWordsSet.add(new PlayableWord('т', _field.cell(new Point(1, 0)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('х', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1, 2)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        cellsToSelect.add(_field.cell(new Point(2, 1)));
        expectedWordsSet.add(new PlayableWord('ш', _field.cell(new Point(1, 2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1, 0)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        expectedWordsSet.add(new PlayableWord('х', _field.cell(new Point(1, 0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('с', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('г', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1, 2)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        expectedWordsSet.add(new PlayableWord('м', _field.cell(new Point(1, 2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(0,2)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('ш', _field.cell(new Point(0,2)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('м', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(2,2)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('у', _field.cell(new Point(2,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('а', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1, 0)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        expectedWordsSet.add(new PlayableWord('а', _field.cell(new Point(1, 0)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('г', _field.cell(new Point(1, 0)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('л', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(0, 0)));
        cellsToSelect.add(_field.cell(new Point(0, 1)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        cellsToSelect.add(_field.cell(new Point(2, 1)));
        expectedWordsSet.add(new PlayableWord('э', _field.cell(new Point(0, 0)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1, 2)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        expectedWordsSet.add(new PlayableWord('д', _field.cell(new Point(1, 2)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('д', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear();// +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('ч', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(2, 2)));
        cellsToSelect.add(_field.cell(new Point(2, 1)));
        expectedWordsSet.add(new PlayableWord('я', _field.cell(new Point(2, 2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('т', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('ш', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('п', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(2,0)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('с', _field.cell(new Point(2,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('ц', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('т', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('ф', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('ф', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('ш', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        cellsToSelect.add(_field.cell(new Point(0,1)));
        expectedWordsSet.add(new PlayableWord('с', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('л', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,2)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('л', _field.cell(new Point(1,2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('п', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1, 2)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        expectedWordsSet.add(new PlayableWord('н', _field.cell(new Point(1, 2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1,0)));
        cellsToSelect.add(_field.cell(new Point(1,1)));
        expectedWordsSet.add(new PlayableWord('н', _field.cell(new Point(1,0)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(1, 2)));
        cellsToSelect.add(_field.cell(new Point(1, 1)));
        expectedWordsSet.add(new PlayableWord('п', _field.cell(new Point(1, 2)), cellsToSelect));

        cellsToSelect.clear(); // +
        cellsToSelect.add(_field.cell(new Point(2,0)));
        cellsToSelect.add(_field.cell(new Point(2,1)));
        expectedWordsSet.add(new PlayableWord('у', _field.cell(new Point(2,0)), cellsToSelect));

        assertEquals(expectedWordsSet.size(), actualWordsSet.size());
        assertEquals(expectedWordsSet, actualWordsSet);
    }


    @Test
    public void findAvailablePlayableWords_emptyField() {
        _field = new GameField(5, 5);
        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);
        _wordSearchStrategy = new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet);

        HashSet<PlayableWord> actualWords = _wordSearchStrategy.findAvailablePlayableWords();

        assertEquals(0, actualWords.size());
    }

    @Test
    public void findAvailablePlayableWords_fieldIsNull() {
        _field = null;
        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);

        assertThrows(IllegalArgumentException.class, () -> new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet));
    }

    @Test
    public void findAvailablePlayableWords_emptyAlphabet() {
        _field = new GameField(5, 5);
        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _alphabet = new Alphabet();
        _wordSearchStrategy = new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet);

        HashSet<PlayableWord> actualWords = _wordSearchStrategy.findAvailablePlayableWords();

        assertEquals(0, actualWords.size());
    }

    @Test
    public void findAvailablePlayableWords_alphabetIsNull() {
        _field = new GameField(5, 5);
        _wordsDB = new WordsDB(DataFilePaths.DICTIONARY_FILE_PATH);
        _alphabet = null;

        assertThrows(IllegalArgumentException.class, () -> new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet));
    }

    @Test
    public void findAvailablePlayableWords_emptyDictionary() {
        _field = new GameField(5, 5);
        _wordsDB = new WordsDB();
        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);
        _wordSearchStrategy = new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet);

        HashSet<PlayableWord> actualWords = _wordSearchStrategy.findAvailablePlayableWords();

        assertEquals(0, actualWords.size());
    }

    @Test
    public void findAvailablePlayableWords_wordsDBIsNull() {
        _field = new GameField(5, 5);
        _wordsDB = null;
        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);

        assertThrows(IllegalArgumentException.class, () -> new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet));
    }

    @Test
    public void findAvailablePlayableWords_nullObjectInConstructor() {
        _field = null;
        _wordsDB = null;
        _alphabet = null;

        assertThrows(IllegalArgumentException.class, () -> new BruteForceWordsSearchStrategy(_field, _wordsDB, _alphabet));
    }
}
