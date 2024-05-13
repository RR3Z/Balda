package ui.buttons;

import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;

public class PlayerActionButton extends JButton {
    public final int BUTTON_WIDTH = 200;
    public final int BUTTON_HEIGHT = 40;

    private final int FONT_SIZE = 22;

    public PlayerActionButton(String buttonText, Color color) {
        super(buttonText);

        this.setBackground(color);

        this.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.сolor(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);

        this.setFont(GameWidgetUtils.font(FONT_SIZE));

        this.setFocusable(false);
    }
}
