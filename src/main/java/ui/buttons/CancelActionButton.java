package ui.buttons;

import model.GameModel;
import org.jetbrains.annotations.NotNull;
import ui.enums.BorderType;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CancelActionButton extends JButton {
    public final int BUTTON_WIDTH = 200;
    public final int BUTTON_HEIGHT = 40;
    private final int FONT_SIZE = 22;

    public CancelActionButton(@NotNull GameModel gameModel, String buttonText) {
        super(buttonText);

        this.addMouseListener(new CancelActionButtonMouseListener(gameModel));

        this.setEnabled(true);
        this.setFocusable(false);

        this.setBackground(GameWidgetUtils.color(ColorType.CANCEL_ACTION_BUTTON));
        this.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.color(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);
        this.setFont(GameWidgetUtils.font(FONT_SIZE));
    }

    private void highlight(boolean isHighlightActive) {
        if(isHighlightActive) {
            this.setBorder(BorderFactory.createLineBorder(
                    GameWidgetUtils.color(ColorType.HIGHLIGHTED_BORDER),
                    GameWidgetUtils.borderThickness(BorderType.EXTRA_BOLD))
            );
        } else {
            this.setBorder(BorderFactory.createLineBorder(
                    GameWidgetUtils.color(ColorType.DEFAULT_BORDER),
                    GameWidgetUtils.borderThickness(BorderType.DEFAULT))
            );
        }
    }

    private class CancelActionButtonMouseListener extends MouseAdapter {
        private GameModel _gameModel;

        public CancelActionButtonMouseListener(@NotNull GameModel gameModel) {
            _gameModel = gameModel;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(CancelActionButton.this.isEnabled()) {
                _gameModel.activePlayer().cancelActionOnField();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(CancelActionButton.this.isEnabled()) {
                CancelActionButton.this.highlight(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            CancelActionButton.this.highlight(false);
        }
    }
}
