package model.ai;

import model.Cell;
import model.GameField;
import model.WordsDB;

import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BruteForceWordsSearchStrategy extends AbstractWordsSearchStrategy {
    private List<PlayableWord> _availablePlayableWords;

    public BruteForceWordsSearchStrategy(@NotNull GameField gameField, @NotNull WordsDB wordsDB) {
        _gameField = gameField;
        _wordsDB = wordsDB;
    }

    @Override
    public List<PlayableWord> findAvailablePlayableWords() {
        _availablePlayableWords = new ArrayList<>();

        for(int i = 0; i < _gameField.height(); i++) {
            for(int j = 0; j < _gameField.width(); j++) {
                Cell cell = _gameField.cell(new Point(j, i));

                findAvailablePlayableWordsForCell(cell, new ArrayList<>());
            }
        }

        return Collections.unmodifiableList(_availablePlayableWords);
    }

    private void findAvailablePlayableWordsForCell(@NotNull Cell cell, @NotNull List<Cell> cellsToSelect) {
        if(cellsToSelect.contains(cell)) {
           return;
        }

        if(!cellsToSelect.isEmpty() && !cell.isAdjacent(cellsToSelect.get(cellsToSelect.size() - 1))) {
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
            if (cell.letter() != null) {
                mask.append(cell.letter());
            } else {
                mask.append("*");
            }
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
