package ui;

import model.Alphabet;
import model.GameModel;
import model.Player;
import model.enums.PlayerState;
import model.events.AlphabetEvent;
import model.events.AlphabetListener;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import org.jetbrains.annotations.NotNull;
import ui.buttons.LetterButton;
import ui.enums.LetterButtonState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphabetWidget extends JPanel {
    private final int ALPHABET_MAX_ROWS_COUNT = 3;

    private GameModel _gameModel;

    private Map<Character, LetterButton> _letters = new HashMap<>();

    public AlphabetWidget(GameModel gameModel) {
        super();
        this.setEnabled(true);

        _gameModel = gameModel;

        Alphabet alphabet = _gameModel.alphabet();
        alphabet.addAlphabetListener(new AlphabetController());

        for(Player player: gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        int numberOfColumns = Math.ceilDiv(alphabet.availableLetters().size(), ALPHABET_MAX_ROWS_COUNT);
        this.setLayout(new GridLayout(ALPHABET_MAX_ROWS_COUNT, numberOfColumns));

        fillWidget(alphabet.availableLetters());

        this.setMaximumSize(new Dimension(
                numberOfColumns * LetterButton.BUTTON_SIZE,
                ALPHABET_MAX_ROWS_COUNT * LetterButton.BUTTON_SIZE
        ));
    }

    private void fillWidget(@NotNull List<Character> letters) {
        for(Character letter: letters) {
            LetterButton letterButton = new LetterButton();

            letterButton.setText(String.valueOf(letter));
            letterButton.addMouseListener(new LetterButtonMouseListener(letterButton));

            _letters.put(letter, letterButton);

            this.add(letterButton);
        }
    }

    private class LetterButtonMouseListener extends MouseAdapter {
        private LetterButton _button;

        public LetterButtonMouseListener(@NotNull LetterButton button) {
            _button = button;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(AlphabetWidget.this.isEnabled()) {
                _gameModel.activePlayer().chooseLetter(_button.getText().charAt(0));
                _button.highlight(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(AlphabetWidget.this.isEnabled()) {
                _button.highlight(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(AlphabetWidget.this.isEnabled()) {
                _button.highlight(false);
            }
        }
    }

    private class PlayerController implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            AlphabetWidget.this.setEnabled(event.player().state() == PlayerState.SELECTING_LETTER);
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            if(event.player() == _gameModel.activePlayer()) {
                _letters.get(event.letter()).changeState(LetterButtonState.SELECTED);
            }
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
        public void forgetSelectedLetter(AlphabetEvent event) {
            _letters.get(event.letter()).changeState(LetterButtonState.UNSELECTED);
        }
    }
}
