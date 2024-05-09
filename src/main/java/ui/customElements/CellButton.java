package ui.customElements;

import ui.utils.WidgetsViewCustomizations;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellButton extends JButton {
    public CellButton() {
        super();
        setupCellButtonView(this);

        this.addMouseListener(new CellButtonMouseListener());
    }

    private void setupCellButtonView(JButton button) {
        button.setPreferredSize(new Dimension(WidgetsViewCustomizations.CELL_BUTTON_SIZE, WidgetsViewCustomizations.CELL_BUTTON_SIZE));

        button.setModel(new FixedStateButtonModel());

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBackground(WidgetsViewCustomizations.CLICKED_CELL_BUTTON_COLOR);

        button.setBorder(BorderFactory.createLineBorder(
                WidgetsViewCustomizations.STANDART_CELL_BUTTON_BORDER_COLOR,
                WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
        );
        button.setBorderPainted(true);

        button.setFocusable(false);
    }

    public void setLetter(Character letter) {
        this.setText(String.valueOf(letter));
    }

    public void removeLetter() {
        this.setText("");
    }

    private static class FixedStateButtonModel extends DefaultButtonModel {
        @Override
        public boolean isPressed() {
            return false;
        }

        @Override
        public boolean isRollover() {
            return false;
        }

        @Override
        public void setRollover(boolean b) {
        }
    }

    private class CellButtonMouseListener extends MouseAdapter {
        boolean isClicked = false;

        @Override
        public void mouseClicked(MouseEvent e) {
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

        @Override
        public void mouseEntered(MouseEvent e) {
            CellButton.this.setBorder(BorderFactory.createLineBorder(
                    WidgetsViewCustomizations.HOVERED_CELL_BUTTON_BORDER_COLOR,
                    WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS + 1)
            );
        }

        @Override
        public void mouseExited(MouseEvent e) {
            CellButton.this.setBorder(BorderFactory.createLineBorder(
                    WidgetsViewCustomizations.STANDART_CELL_BUTTON_BORDER_COLOR,
                    WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
            );
        }
    }
}
