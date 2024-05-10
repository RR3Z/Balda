package ui.buttons;

import org.jetbrains.annotations.NotNull;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class KeyboardButton extends JButton {
    public KeyboardButton(@NotNull Character letter) {
        super(String.valueOf(letter));
        setupButtonView();

        this.addMouseListener(new ButtonMouseListener());
    }

    private void setupButtonView() {
        this.setPreferredSize(new Dimension(WidgetsViewCustomizations.KEYBOARD_BUTTON_SIZE, WidgetsViewCustomizations.KEYBOARD_BUTTON_SIZE));

        this.setModel(new WidgetsViewCustomizations.FixedStateButtonModel());

        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBackground(WidgetsViewCustomizations.CLICKED_KEYBOARD_BUTTON_COLOR);

        this.setBorder(BorderFactory.createLineBorder(
                WidgetsViewCustomizations.STANDART_KEYBOARD_BUTTON_BORDER_COLOR,
                WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
        );
        this.setBorderPainted(true);

        this.setFocusable(false);
    }

    private class ButtonMouseListener extends MouseAdapter {
        boolean isClicked = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(KeyboardButton.this.isEnabled()) {
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
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(KeyboardButton.this.isEnabled()) {
                KeyboardButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.HOVERED_KEYBOARD_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(KeyboardButton.this.isEnabled()) {
                KeyboardButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.STANDART_KEYBOARD_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.KEYBOARD_BUTTON_BORDER_THICKNESS)
                );
            }
        }
    }
}
