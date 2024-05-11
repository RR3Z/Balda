package ui.buttons;

import ui.enums.ColorType;
import ui.utils.ButtonUtils;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;

public class KeyboardButton extends JButton {
    public static final int BUTTON_SIZE = 50;

    public KeyboardButton() {
        this.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        this.setModel(new ButtonUtils.FixedStateButtonModel());

        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBackground(GameWidgetUtils.getColor(ColorType.ACTIVE_KEYBOARD_BUTTON));

        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.getColor(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);

        this.setFocusable(false);
    }
}
