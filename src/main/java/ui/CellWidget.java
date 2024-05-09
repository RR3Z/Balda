package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellWidget extends JPanel {
    private final int BORDER_THICKNESS = 1; // TODO: надо перенести в UTIl файл

    private final JButton _cellButton;

    public CellWidget() {
        super();
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        _cellButton = new JButton();
        setupCellButtonView(_cellButton);
        _cellButton.addMouseListener(new CellButtonMouseListener());

        this.add(_cellButton);
    }

    private void setupCellButtonView(JButton button) {
        int CELL_SIZE = 50;
        button.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));

        button.setModel(new FixedStateButtonModel());

        button.setOpaque(false);
        button.setContentAreaFilled(false);

        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER_THICKNESS));
        button.setBorderPainted(true);

        button.setFocusable(false);

        // TODO: надо брать цвет из util файла
        button.setBackground(new Color(167, 201, 87));
    }

    public void setCharacter(Character letter) {
        _cellButton.setText(String.valueOf(letter));
    }

    public void removeCharacter() {
        _cellButton.setText("");
    }

    private class CellButtonMouseListener extends MouseAdapter {
        boolean isClicked = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            isClicked = !isClicked;

            if(isClicked) {
                _cellButton.setOpaque(true);
                _cellButton.setContentAreaFilled(true);

                // TODO SMTH
            }
            else {
                _cellButton.setOpaque(false);
                _cellButton.setContentAreaFilled(false);

                // TODO SMTH
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO: надо брать цвет из util файла
            _cellButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, BORDER_THICKNESS + 1));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER_THICKNESS));
        }
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
}
