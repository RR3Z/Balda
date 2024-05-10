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
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardWidget extends JPanel {
    private GameModel _gameModel;

    private Map<Character, JButton> _letters = new HashMap<>();

    public KeyboardWidget(GameModel gameModel) {
        super();
        this.setEnabled(true);

        _gameModel = gameModel;
        Alphabet alphabet = _gameModel.alphabet();

        alphabet.addAlphabetListener(new AlphabetController());

        for(Player player: gameModel.players()) { // TODO: подписываться не сразу на всех, а постепенно на каждого активного
            player.addPlayerActionListener(new PlayerController());
        }

        int numberOfColumns = Math.ceilDiv(alphabet.availableLetters().size(), WidgetsViewCustomizations.KEYBOARD_ROW_COUNT);
        int numberOfRows = WidgetsViewCustomizations.KEYBOARD_ROW_COUNT;
        this.setLayout(new GridLayout(numberOfRows, numberOfColumns));

        fillWidget(alphabet.availableLetters());

        this.setMaximumSize(new Dimension(
                numberOfColumns * WidgetsViewCustomizations.KEYBOARD_BUTTON_SIZE,
                numberOfRows * WidgetsViewCustomizations.KEYBOARD_BUTTON_SIZE
                )
        );
    }

    private void fillWidget(@NotNull List<Character> letters) {
        for(Character letter: letters) {
            JButton keyboardButton = new JButton();

            setupButtonView(keyboardButton);
            keyboardButton.setText(String.valueOf(letter));
            keyboardButton.addMouseListener(new KeyboardButtonMouseListener(keyboardButton));

            _letters.put(letter, keyboardButton);

            this.add(keyboardButton);
        }
    }

    private void setupButtonView(@NotNull JButton button) {
        button.setPreferredSize(new Dimension(WidgetsViewCustomizations.KEYBOARD_BUTTON_SIZE, WidgetsViewCustomizations.KEYBOARD_BUTTON_SIZE));

        button.setModel(new WidgetsViewCustomizations.FixedStateButtonModel());

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBackground(WidgetsViewCustomizations.CLICKED_KEYBOARD_BUTTON_COLOR);

        button.setBorder(BorderFactory.createLineBorder(
                WidgetsViewCustomizations.STANDART_KEYBOARD_BUTTON_BORDER_COLOR,
                WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
        );
        button.setBorderPainted(true);

        button.setFocusable(false);
    }

    private class KeyboardButtonMouseListener extends MouseAdapter {
        private JButton _button;

        public KeyboardButtonMouseListener(@NotNull JButton button) {
            _button = button;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(KeyboardWidget.this.isEnabled()) {
                // Logic
                _gameModel.activePlayer().chooseLetter(_button.getText().charAt(0));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(KeyboardWidget.this.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.HOVERED_KEYBOARD_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.setBorder(BorderFactory.createLineBorder(
                    WidgetsViewCustomizations.STANDART_KEYBOARD_BUTTON_BORDER_COLOR,
                    WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
            );
        }
    }

    private class PlayerController implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            KeyboardWidget.this.setEnabled(event.player().state() == PlayerState.SELECTING_LETTER);
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            JButton selectedLetter = _letters.get(event.letter());
            selectedLetter.setOpaque(true);
            selectedLetter.setOpaque(true);
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            for(JButton button: _letters.values()) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void addedNewWordToDictionary(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void failedToAddNewWordToDictionary(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseWrongCell(@NotNull PlayerActionEvent event) {
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

        @Override
        public void submittedWord(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void failedToSubmitWord(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }
    }

    private class AlphabetController implements AlphabetListener {
        @Override
        public void forgetSelectedLetter(AlphabetEvent event) {
            JButton selectedLetter = _letters.get(event.letter());
            selectedLetter.setOpaque(false);
            selectedLetter.setContentAreaFilled(false);
        }
    }
}
