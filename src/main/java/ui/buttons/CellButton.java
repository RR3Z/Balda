package ui.buttons;

import ui.utils.WidgetsViewCustomizations;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellButton extends JButton {
    public CellButton() {
        super();
        setupButtonView();

        this.addMouseListener(new ButtonMouseListener());
    }

    private void setupButtonView() {
        this.setPreferredSize(new Dimension(WidgetsViewCustomizations.CELL_BUTTON_SIZE, WidgetsViewCustomizations.CELL_BUTTON_SIZE));

        this.setModel(new WidgetsViewCustomizations.FixedStateButtonModel());

        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBackground(WidgetsViewCustomizations.CLICKED_CELL_BUTTON_COLOR);

        this.setBorder(BorderFactory.createLineBorder(
                WidgetsViewCustomizations.STANDART_CELL_BUTTON_BORDER_COLOR,
                WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
        );
        this.setBorderPainted(true);

        this.setFocusable(false);
    }

    public void setLetter(Character letter) {
        this.setText(String.valueOf(letter));
    }

    public void removeLetter() {
        this.setText("");
    }

    private class ButtonMouseListener extends MouseAdapter {
        boolean isClicked = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(CellButton.this.isEnabled()) {
                isClicked = !isClicked;

                if(isClicked) {
                    CellButton.this.setOpaque(true);
                    CellButton.this.setContentAreaFilled(true);

                    // TODO SMTH
                }
                else {
                    CellButton.this.setOpaque(false);
                    CellButton.this.setContentAreaFilled(false);

                    // TODO SMTH
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(CellButton.this.isEnabled()) {
                CellButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.HOVERED_CELL_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(CellButton.this.isEnabled()) {
                CellButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.STANDART_CELL_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
                );
            }
        }
    }
}
