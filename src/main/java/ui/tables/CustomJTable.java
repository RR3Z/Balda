package ui.tables;

import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;
import ui.utils.TableUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomJTable extends JTable {
    private final int FONT_SIZE = 20;

    private final int TABLE_ROW_HEIGHT = this.getRowHeight() + this.getFont().getSize();

    public CustomJTable(Object[] headers) {
        super();

        setupTableView();

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(headers);
        this.setModel(tableModel);
    }

    private void setupTableView() {
        JTableHeader header = this.getTableHeader();
        header.setDefaultRenderer(TableUtils.DEFAULT_TABLE_HEADER_RENDERER);
        header.setOpaque(false);
        header.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        this.setDefaultRenderer(Object.class, TableUtils.DEFAULT_CELL_RENDERER);
        this.setOpaque(false);
        this.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setCellSelectionEnabled(false);
        this.setFocusable(false);

        this.setRowHeight(TABLE_ROW_HEIGHT);

        this.setFont(GameWidgetUtils.getFont(FONT_SIZE));
    }

    public void highlightRow(int rowIndex, Color highlightColor, Color defaultColor) {
        DefaultTableCellRenderer highlightCellRenderer = new DefaultTableCellRenderer() {
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

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row == rowIndex) {
                    component.setBackground(highlightColor);
                } else {
                    component.setBackground(defaultColor);
                }

                // Text alignment
                setHorizontalAlignment(SwingConstants.CENTER);

                return component;
            }
        };

        for (int i = 0; i < this.getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(highlightCellRenderer);
        }

        this.repaint();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };
}