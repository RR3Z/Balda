package model.ai;

import model.Cell;
import model.GameField;
import model.WordsDB;

import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BruteForceWordsSearchStrategy extends AbstractWordsSearchStrategy {
    private HashSet<PlayableWord> _availablePlayableWords;

    public BruteForceWordsSearchStrategy(@NotNull GameField gameField, @NotNull WordsDB wordsDB) {
        _gameField = gameField;
        _wordsDB = wordsDB;

        _availablePlayableWords = new HashSet<>();
    }

    @Override
    public HashSet<PlayableWord> findAvailablePlayableWords() {
        long startTime = System.currentTimeMillis();

        _availablePlayableWords.clear();

        for(int i = 0; i < _gameField.height(); i++) {
            for(int j = 0; j < _gameField.width(); j++) {
                Cell cell = _gameField.cell(new Point(j, i));
                findAvailablePlayableWordsForCell(cell, new ArrayList<>());
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Find " + _availablePlayableWords.size() + " words!" + "\nWordsSearchStrategy time: " + (endTime - startTime) + " ms");

        return _availablePlayableWords;
    }

    private void findAvailablePlayableWordsForCell(@NotNull Cell cell, @NotNull List<Cell> cellsToSelect) {
        if(cellsToSelect.contains(cell)) {
           return;
        }

        if(isContainCellWithoutLetter(cellsToSelect) && cell.letter() == null) {
            return;
        }

        cellsToSelect.add(cell);

        if(isContainCellWithoutLetter(cellsToSelect)) {
            formAvailablePlayableWordsFromCells(cellsToSelect);
        }

        for(Cell adjacentCell: cell.adjacentCells()) {
            findAvailablePlayableWordsForCell(adjacentCell, new ArrayList<>(cellsToSelect));
        }
    }

    private void formAvailablePlayableWordsFromCells(@NotNull List<Cell> cells) {
        if(cells.isEmpty()) {
            throw new IllegalArgumentException("BruteForceWordsSearchStrategy: formAvailablePlayableWordsFromCells -> cells is empty");
        }

        // Формирую маску
        StringBuilder mask = new StringBuilder();
        for (Cell cell : cells) {
            mask.append(cell.letter() != null ? cell.letter() : "*");
        }

        // Формирую доступные для розыгрыша слова на основе маски
        if(_wordsDB.isMaskExist(mask.toString())) {
            for(String word: _wordsDB.wordsByMask(mask.toString())) {
                int letterToPlaceIndex = mask.indexOf("*");
                Character letterToPlace = word.charAt(letterToPlaceIndex);
                Cell cellForLetter = cells.get(letterToPlaceIndex);

                PlayableWord newPlayableWord = new PlayableWord(letterToPlace, cellForLetter, cells);
                if(!_wordsDB.containsInUsedWords(newPlayableWord.toString()) && _wordsDB.containsInDictionary(newPlayableWord.toString())) {
                    _availablePlayableWords.add(newPlayableWord);
                }
            }
        }
    }

    private boolean isContainCellWithoutLetter(@NotNull List<Cell> cells) {
        for(Cell cell: cells) {
            if(cell.letter() == null) {
                return true;
            }
        }

        return false;
    }
}
