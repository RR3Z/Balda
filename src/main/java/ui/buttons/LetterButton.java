package ui.buttons;

import ui.enums.BorderType;
import ui.enums.ColorType;
import ui.enums.LetterButtonState;
import ui.utils.ButtonUtils;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;

public class LetterButton extends JButton {
    public static final int BUTTON_SIZE = 50;

    private final int FONT_SIZE = 28;

    private LetterButtonState _state;

    public LetterButton() {
        super();

        this.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        this.setModel(new ButtonUtils.FixedStateButtonModel());

        changeState(LetterButtonState.UNSELECTED);

        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.color(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);

        this.setFont(GameWidgetUtils.font(FONT_SIZE));

        this.setFocusable(false);
    }

    public void highlight(boolean isHighlighted) {
        if(isHighlighted) {
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

    public void changeState(LetterButtonState state) {
        switch (state) {
            case UNSELECTED -> {
                this.setOpaque(false);
                this.setContentAreaFilled(false);
                this.setBackground(GameWidgetUtils.color(ColorType.TRANSPARENT));
                _state = LetterButtonState.UNSELECTED;
            }
            case SELECTED -> {
                this.setOpaque(true);
                this.setContentAreaFilled(true);
                this.setBackground(GameWidgetUtils.color(ColorType.ACTIVE_ALPHABET_BUTTON));
                _state = LetterButtonState.SELECTED;
            }
        }
    }
}
