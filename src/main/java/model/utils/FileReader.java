package model.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {
    public static void readFromFileByLetters(@NotNull String filePath, @NotNull List<Character> destList) {
        try {
            // Read data from file
            List<String> fileOutput = Files.readAllLines(Paths.get(filePath));
            // Convert strings to characters
            for (String line : fileOutput) {
                for (char c : line.toCharArray()) {
                    destList.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
