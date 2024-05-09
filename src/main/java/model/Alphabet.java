package model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Alphabet {
    private List<Character> _availableLetters = new ArrayList<>();

    public Alphabet() {
        // TEMP SOLUTION
        _availableLetters.add('а');
        _availableLetters.add('б');
        _availableLetters.add('в');
        _availableLetters.add('г');
        _availableLetters.add('д');
        _availableLetters.add('е');
        _availableLetters.add('ё');
        _availableLetters.add('ж');
        _availableLetters.add('з');
        _availableLetters.add('и');
        _availableLetters.add('й');
        _availableLetters.add('к');
        _availableLetters.add('л');
        _availableLetters.add('м');
        _availableLetters.add('н');
        _availableLetters.add('о');
        _availableLetters.add('п');
        _availableLetters.add('р');
        _availableLetters.add('с');
        _availableLetters.add('т');
        _availableLetters.add('у');
        _availableLetters.add('ф');
        _availableLetters.add('х');
        _availableLetters.add('ц');
        _availableLetters.add('ч');
        _availableLetters.add('ш');
        _availableLetters.add('щ');
        _availableLetters.add('ъ');
        _availableLetters.add('ы');
        _availableLetters.add('ь');
        _availableLetters.add('э');
        _availableLetters.add('ю');
        _availableLetters.add('я');
    }

    public Alphabet(@NotNull List<Character> letters) {
        // Translate characters to lower case
        List<Character> lowerCaseLetters = new ArrayList<>();
        for (Character letter : letters) {
            if (!lowerCaseLetters.contains(Character.toLowerCase(letter))) {
                lowerCaseLetters.add(Character.toLowerCase(letter));
            }
        }

        _availableLetters = lowerCaseLetters;
    }

    public Alphabet(@NotNull String filePath) {
        readFromFile(filePath);
    }

    public boolean isLetterAvailable(@NotNull Character letter) {
        return _availableLetters.contains(Character.toLowerCase(letter));
    }

    private void readFromFile(@NotNull String filePath) {
        try {
            // Читаю данные из файла
            List<String> fileOutput = Files.readAllLines(Paths.get(filePath));
            // Преобразую строки в символы
            for (String line : fileOutput) {
                for (char c : line.toCharArray()) {
                    _availableLetters.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}


