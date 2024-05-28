package ui.buttons;

import model.GameModel;
import org.jetbrains.annotations.NotNull;
import ui.enums.BorderType;
import ui.enums.ColorType;
import ui.enums.LetterButtonVisualState;
import ui.utils.configs.ButtonConfig;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LetterButton extends JButton {
    public static final int BUTTON_SIZE = 50;
    private final int FONT_SIZE = 28;

    private LetterButtonVisualState _visualState;

    public LetterButton(@NotNull GameModel gameModel) {
        super();

        this.addMouseListener(new LetterButtonMouseListener(gameModel));

        this.setModel(new ButtonConfig.FixedStateButtonModel());

        this.setEnabled(true);
        this.setFocusable(false);

        setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return GameWidgetUtils.color(ColorType.TEXT_COLOR);
            }
        });

        this.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.color(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);
        this.setFont(GameWidgetUtils.font(FONT_SIZE));

        changeVisualState(LetterButtonVisualState.UNSELECTED);
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

    public void changeVisualState(LetterButtonVisualState state) {
        switch (state) {
            case UNSELECTED -> {
                this.setOpaque(false);
                this.setContentAreaFilled(false);
                this.setBackground(GameWidgetUtils.color(ColorType.TRANSPARENT));
                _visualState = LetterButtonVisualState.UNSELECTED;
            }
            case SELECTED -> {
                this.setOpaque(true);
                this.setContentAreaFilled(true);
                this.setBackground(GameWidgetUtils.color(ColorType.ACTIVE_ALPHABET_BUTTON));
                _visualState = LetterButtonVisualState.SELECTED;
            }
        }
    }

    private class LetterButtonMouseListener extends MouseAdapter {
        private GameModel _gameModel;

        public LetterButtonMouseListener(@NotNull GameModel gameModel) {
            _gameModel = gameModel;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(LetterButton.this.isEnabled()) {
                _gameModel.activePlayer().chooseLetter(LetterButton.this.getText().charAt(0));
                LetterButton.this.highlight(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(LetterButton.this.isEnabled()) {
                LetterButton.this.highlight(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            LetterButton.this.highlight(false);
        }
    }
}
