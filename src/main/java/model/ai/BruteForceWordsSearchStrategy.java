package model.ai;

import model.Cell;
import model.GameField;
import model.WordsDB;

import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BruteForceWordsSearchStrategy extends AbstractWordsSearchStrategy {
    private HashSet<PlayableWord> _availablePlayableWords;
    private HashSet<List<Cell>> _unplayablePaths;

    private final int MAX_PATH_LENGTH;

    public BruteForceWordsSearchStrategy(@NotNull GameField gameField, @NotNull WordsDB wordsDB) {
        _gameField = gameField;
        _wordsDB = wordsDB;

        _availablePlayableWords = new HashSet<>();
        _unplayablePaths = new HashSet<>();
        MAX_PATH_LENGTH = _wordsDB.dictionaryLongestWordLength();
    }

    @Override
    public HashSet<PlayableWord> findAvailablePlayableWords() {
        long startTime = System.currentTimeMillis();

        _availablePlayableWords.clear();

        for(int i = 0; i < _gameField.height(); i++) {
            for(int j = 0; j < _gameField.width(); j++) {
                Cell cell = _gameField.cell(new Point(j, i));
                findAvailablePlayableWordsForCell(cell, new ArrayList<>()); // TODO: попробовать распаралелить это
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Find " + _availablePlayableWords.size() + " words!" + "\nWordsSearchStrategy time: " + (endTime - startTime) + " ms");

        return _availablePlayableWords;
    }

    private void findAvailablePlayableWordsForCell(@NotNull Cell cell, @NotNull List<Cell> path) {
        // Путь по длине превышает максимально возможную длину слова из словаря
        if(path.size() > MAX_PATH_LENGTH) {
            return;
        }

        // Заданная ячейка уже содержится в пути (т.е. она уже проверялась)
        if(path.contains(cell)) {
           return;
        }

        // Текущая ячейка без буквы и какая-то ячейка без буквы уже содержится в пути
        if(isContainCellWithoutLetter(path) && cell.letter() == null) {
            return;
        }

        path.add(cell);

        // Текущий путь уже проверялся и из него нельзя составить слово
        if(_unplayablePaths.contains(path)) {
            return;
        }

        formAvailablePlayableWordsFromCells(path);

        for(Cell adjacentCell: cell.adjacentCells()) {// TODO: попробовать распаралелить это
            findAvailablePlayableWordsForCell(adjacentCell, new ArrayList<>(path));
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

                PlayableWord newPlayableWord = new PlayableWord(word.charAt(letterToPlaceIndex), cells.get(letterToPlaceIndex), cells);

                if(!_wordsDB.containsInUsedWords(newPlayableWord.toString()) && _wordsDB.containsInDictionary(newPlayableWord.toString())) {
                    _availablePlayableWords.add(newPlayableWord);
                }
            }
        } else if(cells.size() > 1) {
            _unplayablePaths.add(cells);
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
