package ui.buttons;

import model.Cell;
import model.GameModel;
import ui.enums.BorderType;
import ui.enums.CellButtonVisualState;
import ui.enums.ColorType;
import ui.utils.ButtonUtils;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellButton extends JButton {
    public static final int CELL_SIZE = 50;
    private final int FONT_SIZE = 26;

    private CellButtonVisualState _visualState;

    public CellButton(GameModel gameModel, Cell cell) {
        super();

        this.addMouseListener(new CellButtonMouseListener(gameModel, cell));

        this.setModel(new ButtonUtils.FixedStateButtonModel());

        this.setEnabled(false);
        this.setFocusable(false);

        this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.color(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);
        this.setFont(GameWidgetUtils.font(FONT_SIZE));

        changeVisualState(CellButtonVisualState.DEFAULT);
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

    public void changeVisualState(CellButtonVisualState state) {
        switch (state) {
            case DEFAULT -> {
                this.setOpaque(false);
                this.setContentAreaFilled(false);
                this.setBackground(GameWidgetUtils.color(ColorType.TRANSPARENT));
                _visualState = CellButtonVisualState.DEFAULT;
            }
            case IN_WORD -> {
                this.setOpaque(true);
                this.setContentAreaFilled(true);
                this.setBackground(GameWidgetUtils.color(ColorType.CELL_IN_WORD));
                _visualState = CellButtonVisualState.IN_WORD;
            }
            case CHANGED -> {
                this.setOpaque(true);
                this.setContentAreaFilled(true);
                this.setBackground(GameWidgetUtils.color(ColorType.CHANGED_CELL));
                _visualState = CellButtonVisualState.CHANGED;
            }
        }
    }

    private class CellButtonMouseListener extends MouseAdapter {
        private GameModel _gameModel;
        private Cell _cell;

        public CellButtonMouseListener(GameModel gameModel, Cell cell) {
            _gameModel = gameModel;
            _cell = cell;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(CellButton.this.isEnabled()) {
                _gameModel.activePlayer().chooseCell(_cell);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(CellButton.this.isEnabled()) {
                CellButton.this.highlight(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            CellButton.this.highlight(false);
        }
    }
}
