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
import ui.buttons.KeyboardButton;
import ui.enums.BorderType;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;

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

    private Map<Character, KeyboardButton> _letters = new HashMap<>();

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
                numberOfColumns * KeyboardButton.BUTTON_SIZE,
                ALPHABET_MAX_ROWS_COUNT * KeyboardButton.BUTTON_SIZE
        ));
    }

    private void fillWidget(@NotNull List<Character> letters) {
        for(Character letter: letters) {
            KeyboardButton keyboardButton = new KeyboardButton();

            keyboardButton.setText(String.valueOf(letter));
            keyboardButton.addMouseListener(new KeyboardButtonMouseListener(keyboardButton));

            _letters.put(letter, keyboardButton);

            this.add(keyboardButton);
        }
    }

    private class KeyboardButtonMouseListener extends MouseAdapter {
        private KeyboardButton _button;

        public KeyboardButtonMouseListener(@NotNull KeyboardButton button) {
            _button = button;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(AlphabetWidget.this.isEnabled()) {
                // Logic
                _gameModel.activePlayer().chooseLetter(_button.getText().charAt(0));
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.color(ColorType.DEFAULT_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.DEFAULT))
                );
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(AlphabetWidget.this.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.color(ColorType.DEFAULT_HIGHLIGHTED_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.BOLD))
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(AlphabetWidget.this.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.color(ColorType.DEFAULT_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.DEFAULT))
                );
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
            KeyboardButton selectedLetter = _letters.get(event.letter());
            selectedLetter.setOpaque(true);
            selectedLetter.setContentAreaFilled(true);
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            for(KeyboardButton button: _letters.values()) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            for(KeyboardButton button: _letters.values()) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
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
            KeyboardButton selectedLetter = _letters.get(event.letter());
            selectedLetter.setOpaque(false);
            selectedLetter.setContentAreaFilled(false);
        }
    }
}
