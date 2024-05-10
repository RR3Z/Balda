package ui.buttons;

import org.jetbrains.annotations.NotNull;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class KeyboardButton extends JButton {
    public KeyboardButton(@NotNull Character letter) {
        super();
        setupKeyboardButtonView(this);
        this.setText(String.valueOf(letter));

        this.addMouseListener(new KeyboardButtonMouseListener());
    }

    private void setupKeyboardButtonView(JButton button) {
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
        boolean isClicked = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            isClicked = !isClicked;

            if(isClicked) {
                KeyboardButton.this.setOpaque(true);
                KeyboardButton.this.setContentAreaFilled(true);

                // TODO SMTH
            }
            else {
                KeyboardButton.this.setOpaque(false);
                KeyboardButton.this.setContentAreaFilled(false);

                // TODO SMTH
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            KeyboardButton.this.setBorder(BorderFactory.createLineBorder(
                    WidgetsViewCustomizations.HOVERED_KEYBOARD_BUTTON_BORDER_COLOR,
                    WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
            );
        }

        @Override
        public void mouseExited(MouseEvent e) {
            KeyboardButton.this.setBorder(BorderFactory.createLineBorder(
                    WidgetsViewCustomizations.STANDART_KEYBOARD_BUTTON_BORDER_COLOR,
                    WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
            );
        }
    }
}
