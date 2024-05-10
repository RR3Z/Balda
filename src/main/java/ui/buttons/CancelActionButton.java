package ui.buttons;

import model.GameModel;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

public class CancelActionButton extends JButton {
    private GameModel _gameModel;

    public CancelActionButton(GameModel gameModel) {
        super("Отменить действие");
        _gameModel = gameModel;

        setupButtonView();
        
        this.addMouseListener(new ButtonMouseListener());
    }

    private void setupButtonView() {
        this.setPreferredSize(new Dimension(WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_WIDTH, WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_HEIGHT));

        this.setBackground(WidgetsViewCustomizations.CANCEL_ACTION_BUTTON_COLOR);

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
            if(CancelActionButton.this.isEnabled()) {
                // TODO SMTH
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(CancelActionButton.this.isEnabled()) {
                CancelActionButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.HOVERED_PLAYER_ACTION_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_BORDER_THICKNESS)
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(CancelActionButton.this.isEnabled()) {
                CancelActionButton.this.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.STANDART_PLAYER_ACTION_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.PLAYER_ACTION_BUTTON_BORDER_THICKNESS)
                );
            }
        }
    }
}
