package model.players;

import model.*;
import model.ai.AbstractWordsSearchStrategy;
import model.ai.AbstractWordSelectionStrategy;
import model.ai.PlayableWord;
import model.enums.PlayerState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class AIPlayer extends AbstractPlayer {
    private final int DELAY_STEP = 10;

    private AbstractWordsSearchStrategy _wordsSearchStrategy;
    private AbstractWordSelectionStrategy _wordSelectionStrategy;

    public AIPlayer(@NotNull String name, @NotNull GameField field, @NotNull WordsDB wordsDB, @NotNull Alphabet alphabet, @NotNull AbstractWordSelectionStrategy wordSelectionStrategy, @NotNull AbstractWordsSearchStrategy wordsSearchStrategy) {
        _wordsDB = wordsDB;
        _alphabet = alphabet;
        _field = field;
        _word = new Word();
        _name = name;
        _scoreCounter = new ScoreCounter();

        _wordSelectionStrategy = wordSelectionStrategy;
        _wordsSearchStrategy = wordsSearchStrategy;

        _state = PlayerState.WAITING_TURN;
    }

    @Override
    public void startTurn() {
        super.startTurn();

        PlayableWord playableWord = _wordSelectionStrategy.selectPlayableWord(_wordsSearchStrategy.findAvailablePlayableWords());
        if(playableWord == null) {
            skipTurn();
        } else {
            playWord(playableWord);
        }
    }

    private void playWord(@NotNull PlayableWord word) {
        Timer timer = new Timer();
        int delay = DELAY_STEP;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    selectLetter(word.letter());
                });
            }
        }, delay);
        delay += DELAY_STEP;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    selectCell(word.cellForLetter());
                });
            }
        }, delay);
        delay += DELAY_STEP;

        for(Cell cell: word.cellsToSelect()) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> {
                        selectCell(cell);
                    });
                }
            }, delay);
            delay += DELAY_STEP;
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    submitWord();
                });
            }
        }, delay);
    }
}
