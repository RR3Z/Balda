package ui;

import model.Alphabet;
import ui.customElements.KeyboardButton;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardWidget extends JPanel {
    private Alphabet _alphabet; // TODO: Нужна ли постоянная ссылка?
    private Map<Character, KeyboardButton> _letters = new HashMap<>();

    public KeyboardWidget(Alphabet alphabet) {
        super();
        _alphabet = alphabet;

        this.setLayout(new GridLayout(
                WidgetsViewCustomizations.KEYBOARD_ROW_COUNT,
                Math.ceilDiv(alphabet.availableLetters().size(), WidgetsViewCustomizations.KEYBOARD_ROW_COUNT))
        );

        fillWidget(alphabet.availableLetters());
    }

    private void fillWidget(List<Character> letters) {
        for(Character letter: letters) {
            KeyboardButton keyboardButton = new KeyboardButton(letter);

            _letters.put(letter, keyboardButton);

            this.add(keyboardButton);
        }
    }
}
