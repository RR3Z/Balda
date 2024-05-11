package ui.utils;

import ui.enums.ColorType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableUtils {
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
}
