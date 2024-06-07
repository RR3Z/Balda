package model.utils;

import java.util.ArrayList;
import java.util.List;

public class RussianLettersPicker {
    private static List<Character> _consonantLetters = new ArrayList<>();
    private static List<Character> _vowelLetters = new ArrayList<>();

    public static List<Character> consonantLetters() {
        if(_consonantLetters.isEmpty()) {
            FileReader.readFromFileByLetters(DataFilePaths.CONSONANT_LETTERS_FILE_PATH, _consonantLetters);
        }

        return _consonantLetters;
    }

    public static List<Character> vowelLetters() {
        if(_vowelLetters.isEmpty()) {
            FileReader.readFromFileByLetters(DataFilePaths.VOWEL_LETTERS_FILE_PATH, _vowelLetters);
        }

        return _vowelLetters;
    }
}
