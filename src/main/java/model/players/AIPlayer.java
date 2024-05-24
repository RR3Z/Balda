package model.players;

import model.*;
import model.ai.AbstractWordSelectionStrategy;
import model.ai.PlayableWord;
import model.enums.PlayerState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class AIPlayer extends AbstractPlayer {
    private final int DELAY_STEP = 10;

    private AbstractWordSelectionStrategy _wordSelectionStrategy;

    public AIPlayer(@NotNull String name, @NotNull GameField field, @NotNull WordsDB wordsDB, @NotNull Alphabet alphabet, @NotNull AbstractWordSelectionStrategy wordSelectionStrategy) {
        _wordsDB = wordsDB;
        _alphabet = alphabet;
        _field = field;
        _word = new Word();
        _name = name;
        _scoreCounter = new ScoreCounter();

        _wordSelectionStrategy = wordSelectionStrategy;

        _state = PlayerState.WAITING_TURN;
    }

    @Override
    public void startTurn() {
        super.startTurn();

        PlayableWord playableWord = _wordSelectionStrategy.selectPlayableWord();
        if(playableWord == null) {
            skipTurn();
            System.out.println("\nPlayer: " + _name + " skipped turn");
            System.out.println(_wordSelectionStrategy.);
        } else {
            playWord(playableWord);
        }
    }

    private void playWord(@NotNull PlayableWord word) {
        Timer timer = new Timer();
        int delay = 200;

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
