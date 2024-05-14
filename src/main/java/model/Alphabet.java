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

    public Alphabet(@NotNull GameModel gameModel, @NotNull String filePath) {
        gameModel.addGameModelListener(new GameModelObserve());

        readFromFile(filePath);
    }

    public List<Character> availableLetters() {
        return Collections.unmodifiableList(_availableLetters);
    }

    public Character selectedLetter() { return _selectedLetter; }

    public void forgetSelectedLetter() {
        if(_selectedLetter != null) {
            fireForgetSelectedLetter(_selectedLetter);
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

    private class GameModelObserve implements GameModelListener {
        @Override
        public void playerExchanged(GameModelEvent event) {
            forgetSelectedLetter();
        }

        @Override
        public void placedStartWord(GameModelEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void gameIsFinished(GameModelEvent event) {
            // DON'T NEED IT HERE
        }
    }



    // Listeners
    private List<EventListener> _alphabetListeners = new ArrayList<>();

    public void addAlphabetListener(@NotNull AlphabetListener listener) {
        _alphabetListeners.add(listener);
    }

    private void fireForgetSelectedLetter(Character letter) {
        for (Object listener : _alphabetListeners) {
            AlphabetEvent event = new AlphabetEvent(this);
            event.setAlphabet(this);
            event.setLetter(letter);

            ((AlphabetListener) listener).forgetSelectedLetter(event);
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


