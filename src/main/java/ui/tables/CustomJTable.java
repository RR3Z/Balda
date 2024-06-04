package ui.tables;

import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;
import ui.utils.configs.TableConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CustomJTable extends JTable {
    private final int FONT_SIZE = 20;

    public CustomJTable(Object[] headers) {
        super();

        setupTableView();

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(headers);
        this.setModel(tableModel);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                changeSelection(getRowCount() - 1, 0,false,false);
            }
        });
    }

    private void setupTableView() {
        this.setFont(GameWidgetUtils.font(FONT_SIZE));

        int tableRowHeight = this.getRowHeight() + this.getFont().getSize();
        this.setRowHeight(tableRowHeight);

        JTableHeader header = this.getTableHeader();
        header.setDefaultRenderer(TableConfig.DEFAULT_TABLE_HEADER_RENDERER);
        header.setOpaque(false);
        header.setBackground(GameWidgetUtils.color(ColorType.TRANSPARENT));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        this.setDefaultRenderer(Object.class, TableConfig.DEFAULT_CELL_RENDERER);
        this.setOpaque(false);
        this.setBackground(GameWidgetUtils.color(ColorType.TRANSPARENT));
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setCellSelectionEnabled(false);
        this.setFocusable(false);
    }

    public void highlightRow(int rowIndex, Color highlightColor) {
        DefaultTableCellRenderer highlightCellRenderer = new DefaultTableCellRenderer() {
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

                if (row == rowIndex) {
                    component.setBackground(highlightColor);
                } else {
                    component.setBackground(GameWidgetUtils.color(ColorType.TRANSPARENT));
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

    public void scrollToRow(int rowIndex) {
        changeSelection(rowIndex, 0, false, false);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };
}
