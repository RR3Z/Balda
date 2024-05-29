package ui.widgets;

import model.Alphabet;
import model.GameModel;
import model.enums.PlayerState;
import model.events.*;
import model.players.AbstractPlayer;
import model.players.UserPlayer;
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

        // Listeners
        gameModel.addGameModelListener(new GameModelController());

        Alphabet alphabet = gameModel.alphabet();
        alphabet.addAlphabetListener(new AlphabetController());

        for(AbstractPlayer player: gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        // Visual
        this.setEnabled(true);

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
            AlphabetWidget.this.changeActivity(event.player() instanceof UserPlayer && event.player().state() == PlayerState.SELECTING_LETTER);
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

    private class GameModelController implements GameModelListener {

        @Override
        public void gameIsFinished(GameModelEvent event) {
            AlphabetWidget.this.clearAllHighlights();
        }

        @Override
        public void playerExchanged(GameModelEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void placedStartWord(GameModelEvent event) {
            // DON'T NEED IT HERE
        }
    }
}
