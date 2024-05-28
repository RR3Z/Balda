package ui.utils.configs;

import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableConfig {

    public static DefaultTableCellRenderer DEFAULT_TABLE_HEADER_RENDERER = new DefaultTableCellRenderer() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Border color
            g.setColor(GameWidgetUtils.color(ColorType.DEFAULT_BORDER));

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

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            component.setBackground(GameWidgetUtils.color(ColorType.TABLE_HEADER));

            // Text alignment
            setHorizontalAlignment(SwingConstants.CENTER);

            return component;
        }
    };

    public static DefaultTableCellRenderer DEFAULT_CELL_RENDERER = new DefaultTableCellRenderer() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Border color
            g.setColor(GameWidgetUtils.color(ColorType.DEFAULT_BORDER));

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
