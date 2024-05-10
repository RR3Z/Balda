package model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Alphabet {
    private List<Character> _availableLetters = new ArrayList<>();

    public Alphabet(@NotNull String filePath) {
        readFromFile(filePath);
    }

    public List<Character> availableLetters() {
        return Collections.unmodifiableList(_availableLetters);
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


