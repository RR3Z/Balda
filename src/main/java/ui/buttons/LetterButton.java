package ui.buttons;

import ui.enums.ColorType;
import ui.utils.ButtonUtils;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;

public class LetterButton extends JButton {
    public static final int BUTTON_SIZE = 50;

    private final int FONT_SIZE = 28;

    public LetterButton() {
        super();

        this.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        this.setModel(new ButtonUtils.FixedStateButtonModel());

        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBackground(GameWidgetUtils.color(ColorType.ACTIVE_KEYBOARD_BUTTON));

        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.color(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);

        this.setFont(GameWidgetUtils.font(FONT_SIZE));

        this.setFocusable(false);
    }
}
