package model;

import model.events.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

public class Alphabet {
    private List<Character> _availableLetters = new ArrayList<>();

    private Character _selectedLetter;

    public Alphabet(@NotNull String filePath) {
        readFromFile(filePath);
    }

    public List<Character> availableLetters() {
        return Collections.unmodifiableList(_availableLetters);
    }

    public Character selectedLetter() { return _selectedLetter; }

    public void forgetSelectedLetter() {
        if(_selectedLetter != null) {
            fireForgotSelectedLetter(_selectedLetter);
            _selectedLetter = null;
        }
    }

    public boolean selectLetter(@NotNull Character letter) {
        if(_selectedLetter != null) {
            throw new IllegalArgumentException("Alphabet -> selectLetter: trying to select a letter when it has already been selected");
        }

        if(isLetterAvailable(letter)) {
            _selectedLetter = letter;
            fireSelectedLetter(letter);
            return true;
        }

        return false;
    }

    public boolean isLetterAvailable(@NotNull Character letter) {
        return _availableLetters.contains(letter);
    }

    private void readFromFile(@NotNull String filePath) {
        try {
            // Read data from file
            List<String> fileOutput = Files.readAllLines(Paths.get(filePath));
            // Convert strings to characters
            for (String line : fileOutput) {
                for (char c : line.toCharArray()) {
                    _availableLetters.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Listeners
    private List<EventListener> _alphabetListeners = new ArrayList<>();

    public void addAlphabetListener(@NotNull AlphabetListener listener) {
        _alphabetListeners.add(listener);
    }

    private void fireForgotSelectedLetter(Character letter) {
        for (Object listener : _alphabetListeners) {
            AlphabetEvent event = new AlphabetEvent(this);
            event.setAlphabet(this);
            event.setLetter(letter);

            ((AlphabetListener) listener).forgotSelectedLetter(event);
        }
    }

    private void fireSelectedLetter(Character letter) {
        for (Object listener : _alphabetListeners) {
            AlphabetEvent event = new AlphabetEvent(this);
            event.setAlphabet(this);
            event.setLetter(letter);

            ((AlphabetListener) listener).selectedLetter(event);
        }
    }
}


