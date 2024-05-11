package ui.buttons;

import ui.enums.ColorType;
import ui.utils.ButtonUtils;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;

public class CellButton extends JButton {
    public static final int CELL_SIZE = 50;

    public CellButton() {
        super();

        this.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));

        this.setModel(new ButtonUtils.FixedStateButtonModel());

        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBackground(GameWidgetUtils.getColor(ColorType.INACTIVE_CELL));

        this.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.getColor(ColorType.DEFAULT_BORDER)));
        this.setBorderPainted(true);

        this.setFocusable(false);
    }
}
