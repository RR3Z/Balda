package model.ai;

import model.Alphabet;
import model.Cell;
import model.GameField;
import model.WordsDB;

import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BruteForceWordsSearchStrategy extends AbstractWordsSearchStrategy {
    private HashSet<PlayableWord> _availablePlayableWords;
    private HashSet<List<Cell>> _playablePaths;
    private HashSet<List<Cell>> _unplayablePaths;

    private final int MAX_PATH_LENGTH;

    public BruteForceWordsSearchStrategy(@NotNull GameField gameField, @NotNull WordsDB wordsDB, @NotNull Alphabet alphabet) {
        _gameField = gameField;
        _wordsDB = wordsDB;
        _alphabet = alphabet;
        MAX_PATH_LENGTH = _wordsDB.maximumDictionaryWordLength();

        _availablePlayableWords = new HashSet<>();
        _playablePaths = new HashSet<>();
        _unplayablePaths = new HashSet<>();
    }

    @Override
    public HashSet<PlayableWord> findAvailablePlayableWords() {
        long startTime = System.currentTimeMillis();

        _availablePlayableWords.clear();
        _playablePaths.clear();

        for(int i = 0; i < _gameField.height(); i++) {
            for(int j = 0; j < _gameField.width(); j++) {
                Cell cell = _gameField.cell(new Point(j, i));
                findPlayablePathsForCell(cell, new ArrayList<>());
            }
        }

        for(List<Cell> path: _playablePaths) {
            formWordForPath(path);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Find " + _availablePlayableWords.size() + " words!" + "\nWordsSearchStrategy time: " + (endTime - startTime) + " ms");

        return _availablePlayableWords;
    }

    private void findPlayablePathsForCell(@NotNull Cell checkableCell, @NotNull List<Cell> path) {
        if(_unplayablePaths.contains(path) || path.contains(checkableCell)) {
            return;
        }

        if(path.size() > MAX_PATH_LENGTH) {
            _unplayablePaths.add(path);
            return;
        }

        if(checkableCell.letter() == null && isPathContainCellWithoutLetter(path)) {
            return;
        }

        path.add(checkableCell);

        if(!isPathPlayable(path)) {
            _unplayablePaths.add(path);
            return;
        }

        if(path.size() > 1) {
            _playablePaths.add(path);
        }

        for(Cell adjacentCell: checkableCell.adjacentCells()) {
            findPlayablePathsForCell(adjacentCell, new ArrayList<>(path));
        }
    }

    private boolean isPathPlayable(@NotNull List<Cell> path) {
        if(!isPathContainCellWithoutLetter(path)) {
            return false;
        }

        for(Character letter: _alphabet.availableLetters()) {
            // Составляю префикс
            StringBuilder prefix = new StringBuilder();
            for (Cell cell : path)
            {
                if(cell.letter() != null) {
                    prefix.append(cell.letter());
                } else {
                    prefix.append(letter);
                }
            }

            if (_wordsDB.isPrefixMakesSense(prefix.toString())) {
                return true;
            }
        }

        return false;
    }

    private boolean isPathContainCellWithoutLetter(@NotNull List<Cell> path) {
        for(Cell cell: path) {
            if(cell.letter() == null){
                return true;
            }
        }

        return false;
    }

    private void formWordForPath(@NotNull List<Cell> path) {
        for(Character letter: _alphabet.availableLetters()) {
            Cell cellWithoutLetter = null;
            StringBuilder possibleWord = new StringBuilder();
            for (Cell cell : path)
            {
                if(cell.letter() != null) {
                    possibleWord.append(cell.letter());
                } else {
                    cellWithoutLetter = cell;
                    possibleWord.append(letter);
                }
            }

            if (_wordsDB.containsInDictionary(possibleWord.toString()) && !_wordsDB.containsInUsedWords(possibleWord.toString())) {
                _availablePlayableWords.add(new PlayableWord(letter, cellWithoutLetter, path));
            }
        }
    }
}
