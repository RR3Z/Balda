package ui.buttons;

import model.GameModel;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SubmitWordButton extends JButton {
    private GameModel _gameModel;

    public SubmitWordButton(GameModel gameModel) {
        super("Подтвердить слово");
        _gameModel = gameModel;

        setupButtonView();

        this.addMouseListener(new SubmitWordButton.ButtonMouseListener());
    }

    private void setupButtonView() {
        this.setPreferredSize(new Dimension(WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_WIDTH, WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_HEIGHT));

        this.setBackground(WidgetsViewCustomizations.SUBMIT_WORD_BUTTON_COLOR);

        this.setBorder(BorderFactory.createLineBorder(
                WidgetsViewCustomizations.STANDART_PLAYER_ACTION_BUTTON_BORDER_COLOR,
                WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_BORDER_THICKNESS)
        );
        this.setBorderPainted(true);

        this.setFocusable(false);
    }

    private class ButtonMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(SubmitWordButton.this.isEnabled()) {
                // TODO SMTH
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(SubmitWordButton.this.isEnabled()) {
                SubmitWordButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.HOVERED_PLAYER_ACTION_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_BORDER_THICKNESS)
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(SubmitWordButton.this.isEnabled()) {
                SubmitWordButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.STANDART_PLAYER_ACTION_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_BORDER_THICKNESS)
                );
            }
        }
    }
}
