package ui;

import model.Alphabet;
import ui.buttons.CellButton;
import ui.buttons.KeyboardButton;
import ui.enums.ActivityState;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardWidget extends JPanel {
    ActivityState _widgetState;

    private Alphabet _alphabet; // TODO: Нужна ли постоянная ссылка?
    private Map<Character, KeyboardButton> _letters = new HashMap<>();

    public KeyboardWidget(Alphabet alphabet) {
        super();
        _widgetState = ActivityState.ENABLED;
        _alphabet = alphabet;

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

    private void fillWidget(List<Character> letters) {
        for(Character letter: letters) {
            KeyboardButton keyboardButton = new KeyboardButton(letter);

            _letters.put(letter, keyboardButton);

            this.add(keyboardButton);
        }
    }

    private void changeActivity() {
        if(_widgetState == ActivityState.ENABLED) {
            for (KeyboardButton button : _letters.values()) {
                button.setEnabled(false);
            }

            _widgetState = ActivityState.DISABLED;
        }
        else {
            for (KeyboardButton button : _letters.values()) {
                button.setEnabled(true);
            }

            _widgetState = ActivityState.ENABLED;
        }
    }
}
