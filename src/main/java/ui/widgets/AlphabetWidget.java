package ui.widgets;

import model.Alphabet;
import model.GameModel;
import model.enums.PlayerState;
import model.events.AlphabetEvent;
import model.events.AlphabetListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import model.players.AbstractPlayer;
import org.jetbrains.annotations.NotNull;
import ui.buttons.LetterButton;
import ui.enums.LetterButtonVisualState;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphabetWidget extends JPanel {
    private final int ALPHABET_MAX_ROWS_COUNT = 3;

    private Map<Character, LetterButton> _letters = new HashMap<>();

    public AlphabetWidget(@NotNull GameModel gameModel) {
        super();
        this.setEnabled(true);

        Alphabet alphabet = gameModel.alphabet();
        alphabet.addAlphabetListener(new AlphabetController());

        for(AbstractPlayer player: gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        fillWidget(gameModel, alphabet.availableLetters());

        int numberOfColumns = Math.ceilDiv(alphabet.availableLetters().size(), ALPHABET_MAX_ROWS_COUNT);
        this.setLayout(new GridLayout(ALPHABET_MAX_ROWS_COUNT, numberOfColumns));
        this.setMaximumSize(new Dimension(
                numberOfColumns * LetterButton.BUTTON_SIZE,
                ALPHABET_MAX_ROWS_COUNT * LetterButton.BUTTON_SIZE
        ));
    }

    private void fillWidget(@NotNull GameModel gameModel, @NotNull List<Character> letters) {
        for(Character letter: letters) {
            LetterButton letterButton = new LetterButton(gameModel);

            letterButton.setText(String.valueOf(letter));

            _letters.put(letter, letterButton);

            this.add(letterButton);
        }
    }

    private void changeActivity(boolean widgetActivity) {
        AlphabetWidget.this.setEnabled(widgetActivity);

        for(LetterButton letterButton: _letters.values()) {
            letterButton.setEnabled(widgetActivity);
        }
    }

    private void clearAllHighlights() {
        for(LetterButton letterButton: _letters.values()) {
            letterButton.changeVisualState(LetterButtonVisualState.UNSELECTED);
        }
    }

    private class PlayerController implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            AlphabetWidget.this.changeActivity(event.player().state() == PlayerState.SELECTING_LETTER);
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }


        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }


        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }
    }

    private class AlphabetController implements AlphabetListener {
        @Override
        public void forgotSelectedLetter(AlphabetEvent event) {
            AlphabetWidget.this.clearAllHighlights();
        }

        @Override
        public void selectedLetter(AlphabetEvent event) {
            _letters.get(event.letter()).changeVisualState(LetterButtonVisualState.SELECTED);
        }
    }
}
