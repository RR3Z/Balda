package ui.buttons;

import ui.enums.BorderType;
import ui.enums.CellButtonState;
import ui.enums.ColorType;
import ui.utils.ButtonUtils;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;

public class CellButton extends JButton {
    public static final int CELL_SIZE = 50;

    private final int FONT_SIZE = 26;

    private CellButtonState _state;

    public CellButton() {
        super();

        this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));

        this.setModel(new ButtonUtils.FixedStateButtonModel());

        changeState(CellButtonState.DEFAULT);

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

    public void changeState(CellButtonState state) {
        switch (state) {
            case DEFAULT -> {
                this.setOpaque(false);
                this.setContentAreaFilled(false);
                this.setBackground(GameWidgetUtils.color(ColorType.TRANSPARENT));
                _state = CellButtonState.DEFAULT;
            }
            case IN_WORD -> {
                this.setOpaque(true);
                this.setContentAreaFilled(true);
                this.setBackground(GameWidgetUtils.color(ColorType.CELL_IN_WORD));
                _state = CellButtonState.IN_WORD;
            }
            case CHANGED -> {
                this.setOpaque(true);
                this.setContentAreaFilled(true);
                this.setBackground(GameWidgetUtils.color(ColorType.CHANGED_CELL));
                _state = CellButtonState.CHANGED;
            }
        }
    }
}
