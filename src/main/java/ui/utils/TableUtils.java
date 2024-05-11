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
            g.setColor(Color.BLACK); // Цвет линий
            g.fillRect(0, 0, 1, getHeight()); // Левая вертикальная линия
            g.fillRect(0, 0, getWidth(), 1); // Верхняя горизонтальная линия
            g.fillRect(getWidth() - 1, 0, 1, getHeight()); // Правая вертикальная линия
            g.fillRect(0, getHeight() - 1, getWidth(), 1); // Нижняя горизонтальная линия

            setHorizontalAlignment(SwingConstants.CENTER);
        }
    };

    public static DefaultTableCellRenderer DEFAULT_TABLE_CELL_RENDERER = new DefaultTableCellRenderer() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK); // Цвет линий
            g.fillRect(0, 0, 1, getHeight()); // Левая вертикальная линия
            g.fillRect(0, 0, getWidth(), 1); // Верхняя горизонтальная линия
            g.fillRect(getWidth() - 1, 0, 1, getHeight()); // Правая вертикальная линия
            g.fillRect(0, getHeight() - 1, getWidth(), 1); // Нижняя горизонтальная линия
        }
    };

    public static void highlightRow(JTable table, int rowIndex, Color highlightColor, Color defaultColor) {
            DefaultTableCellRenderer highlightCellRenderer = new DefaultTableCellRenderer() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, 1, getHeight());
                    g.fillRect(0, 0, getWidth(), 1);
                    g.fillRect(getWidth() - 1, 0, 1, getHeight());
                    g.fillRect(0, getHeight() - 1, getWidth(), 1);
                }

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (row == rowIndex) {
                        component.setBackground(highlightColor);
                    } else {
                        component.setBackground(defaultColor);
                    }

                    return component;
                }
            };

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(highlightCellRenderer);
            }

            table.repaint();
    }
}
