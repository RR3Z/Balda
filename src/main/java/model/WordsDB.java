package model;

import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import model.players.AbstractPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WordsDB {
    private HashSet<String> _dictionary;
    private HashMap<String, AbstractPlayer> _usedWords;
    private HashMap<String, String> _maskToWord; // It should be understood that masks are set once when reading a dictionary from a file

    public WordsDB(@NotNull String filePath) {
        _dictionary = new HashSet<>();
        _usedWords = new HashMap<>();
        _maskToWord = new HashMap<>();

        readFromFile(filePath);
    }

    public boolean addToDictionary(@NotNull String word, AbstractPlayer player) {
        if (_dictionary.contains(word)) {
            return false;
        }

        _dictionary.add(word);
        fireAddedNewWordToDictionary(player, word);
        return true;
    }

    public boolean addToUsedWords(@NotNull String word, AbstractPlayer player) {
        if (!containsInDictionary(word)) {
            fireWordNotAllowed(player, word);
            return false;
        }

        if (containsInUsedWords(word)) {
            fireWordAlreadyUsed(player, word);
            return false;
        }

        _usedWords.put(word, player);
        fireAddedToUsedWords(player, word);
        return true;
    }

    public String randomWord(int length) {
        if (length <= 1) {
            throw new IllegalArgumentException("WordsDB -> randomWord(): wrong length (1 or less)");
        }

        List<String> dictionaryCopy = new ArrayList<>(_dictionary);
        Collections.shuffle(dictionaryCopy);

        // Looking for a word of a specified length
        for (String word : dictionaryCopy) {
            if (word.length() == length) {
                return word;
            }
        }

        // If there is no word of the specified length, looking for the longest word in the dictionary
        String longestWord = "";
        for (String word : dictionaryCopy) {
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }

        return longestWord;
    }

    public boolean containsInDictionary(@NotNull String word) {
        return _dictionary.contains(word);
    }

    public boolean containsInUsedWords(@NotNull String word) {
        return _usedWords.containsKey(word);
    }

    private void readFromFile(@NotNull String filePath) {
        try {
            List<String> fileInput;
            fileInput = Files.readAllLines(Paths.get(filePath));

            // Translate all words to lowercase
            HashSet<String> lowerCaseWords = new HashSet<>();
            for (String word : fileInput) {
                lowerCaseWords.add(word.toLowerCase());
                formMasksForWord(word.toLowerCase());
            }

            _dictionary = lowerCaseWords;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void formMasksForWord(@NotNull String word) {
        for (int i = 0; i < word.length(); i++) {
            StringBuilder mask = new StringBuilder(word);
            mask.setCharAt(i, '*');

            _maskToWord.put(mask.toString(), word);
        }
    }

    // Listeners
    private List<EventListener> _wordsDBListeners = new ArrayList<>();

    public void addWordsDBListener(@NotNull WordsDBListener listener) {
        _wordsDBListeners.add(listener);
    }

    private void fireAddedToUsedWords(AbstractPlayer player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).addedToUsedWords(event);
        }
    }

    private void fireWordAlreadyUsed(AbstractPlayer player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).wordAlreadyUsed(event);
        }
    }

    private void fireWordNotAllowed(AbstractPlayer player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).wordNotAllowed(event);
        }
    }

    private void fireAddedNewWordToDictionary(AbstractPlayer player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).addedNewWordToDictionary(event);
        }
    }
}
