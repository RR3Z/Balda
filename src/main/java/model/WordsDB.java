package model;

import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WordsDB {
    private List<String> _dictionary = new ArrayList<>();
    private Map<Player, String> _usedWords = new HashMap<>();

    public WordsDB(@NotNull String filePath) {
        readFromFile(filePath);
    }

    public boolean addToDictionary(@NotNull String word, Player player) {
        String lowercaseWord = word.toLowerCase();

        if (_dictionary.contains(lowercaseWord)) {
            return false;
        }

        _dictionary.add(lowercaseWord);
        fireAddedNewWordToDictionary(player, word);
        return true;
    }

    public boolean addToUsedWords(@NotNull String word, Player player) {
        if (!containsInDictionary(word)) {
            fireWordNotAllowed(player, word);
            return false;
        }

        if (containsInUsedWords(word)) {
            fireWordAlreadyUsed(player, word);
            return false;
        }

        _usedWords.put(player, word.toLowerCase());
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
        return _dictionary.contains(word.toLowerCase());
    }

    public boolean containsInUsedWords(@NotNull String word) {
        return _usedWords.containsValue(word.toLowerCase());
    }

    private void readFromFile(@NotNull String filePath) {
        try {
            List<String> fileInput;
            fileInput = Files.readAllLines(Paths.get(filePath));

            // Translate all words to lowercase
            List<String> lowerCaseWords = new ArrayList<>();
            for (String word : fileInput) {
                lowerCaseWords.add(word.toLowerCase());
            }

            _dictionary = lowerCaseWords;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Listeners
    private List<EventListener> _wordsDBListeners = new ArrayList<>();

    public void addWordsDBListener(@NotNull WordsDBListener listener) {
        _wordsDBListeners.add(listener);
    }

    private void fireAddedToUsedWords(Player player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).addedToUsedWords(event);
        }
    }

    private void fireWordAlreadyUsed(Player player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).wordAlreadyUsed(event);
        }
    }

    private void fireWordNotAllowed(Player player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).wordNotAllowed(event);
        }
    }

    private void fireAddedNewWordToDictionary(Player player, @NotNull String word) {
        for (Object listener : _wordsDBListeners) {
            WordsDBEvent event = new WordsDBEvent(this);
            event.setPlayer(player);
            event.setWord(word);

            ((WordsDBListener) listener).addedNewWordToDictionary(event);
        }
    }
}
