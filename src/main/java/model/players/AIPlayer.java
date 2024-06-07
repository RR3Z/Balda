package model.players;

import model.*;
import model.ai.*;
import model.enums.PlayerState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class AIPlayer extends AbstractPlayer {

    private AbstractWordsSearchStrategy _wordsSearchStrategy;
    private AbstractWordSelectionStrategy _wordSelectionStrategy;

    public AIPlayer(@NotNull String name, @NotNull GameField field, @NotNull WordsDB wordsDB, @NotNull Alphabet alphabet) {
        _wordsDB = wordsDB;
        _alphabet = alphabet;
        _field = field;
        _word = new Word();
        _name = name;
        _scoreCounter = new ScoreCounter();

        _wordsSearchStrategy = new BruteForceWordsSearchStrategy(field, wordsDB, alphabet);
        _wordSelectionStrategy = new WordWithMostConsonantLettersSelectionStrategy(wordsDB);

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
        int delayStep = 500;
        int delayBetweenActions = delayStep;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                selectLetter(word.letter());
            }
        }, delayBetweenActions);
        delayBetweenActions += delayStep;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                selectCell(word.cellForLetter());
            }
        }, delayBetweenActions);
        delayBetweenActions += delayStep;

        for(Cell cell: word.cellsToSelect()) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    selectCell(cell);
                }
            }, delayBetweenActions);
            delayBetweenActions += delayStep;
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                submitWord();
            }
        }, delayBetweenActions);
    }
}
