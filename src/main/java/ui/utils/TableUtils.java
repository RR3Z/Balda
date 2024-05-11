package ui.utils;

import ui.enums.ColorType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableUtils {
    public static DefaultTableCellRenderer HEADER_TABLE_CELL_RENDERER = new DefaultTableCellRenderer() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Border color
            g.setColor(GameWidgetUtils.getColor(ColorType.DEFAULT_BORDER));

            // Left vertical line
            g.fillRect(0, 0, 1, getHeight());
            // Top horizontal line
            g.fillRect(0, 0, getWidth(), 1);
            // Right vertical line
            g.fillRect(getWidth() - 1, 0, 1, getHeight());
            // Bottom horizontal line
            g.fillRect(0, getHeight() - 1, getWidth(), 1);

            // Text alignment
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    };

    public static DefaultTableCellRenderer DEFAULT_TABLE_CELL_RENDERER = new DefaultTableCellRenderer() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Border color
            g.setColor(GameWidgetUtils.getColor(ColorType.DEFAULT_BORDER));

            // Left vertical line
            g.fillRect(0, 0, 1, getHeight());
            // Top horizontal line
            g.fillRect(0, 0, getWidth(), 1);
            // Right vertical line
            g.fillRect(getWidth() - 1, 0, 1, getHeight());
            // Bottom horizontal line
            g.fillRect(0, getHeight() - 1, getWidth(), 1);

            // Text alignment
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    };
}
