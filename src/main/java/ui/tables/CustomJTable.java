package ui.tables;

import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;
import ui.utils.TableUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomJTable extends JTable {
    public CustomJTable(Object[] headers) {
        super();

        setupTableView();

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(headers);
        this.setModel(tableModel);
    }

    private void setupTableView() {
        JTableHeader header = this.getTableHeader();
        header.setDefaultRenderer(TableUtils.HEADER_TABLE_CELL_RENDERER);
        header.setOpaque(false);
        header.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        this.setDefaultRenderer(Object.class, TableUtils.DEFAULT_TABLE_CELL_RENDERER);
        this.setOpaque(false);
        this.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setCellSelectionEnabled(false);
        this.setFocusable(false);

        this.setFont(GameWidgetUtils.getFont());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };
}
