package ui.tables;

import model.GameModel;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsedWordsTable extends JTable {
    private GameModel _gameModel;

    private DefaultTableModel _usedWordsTableModel;

    public UsedWordsTable(GameModel gameModel) {
        super();
        _gameModel = gameModel;

        setupTableData();
        setupTableView();
    }

    private void setupTableData() {
        _usedWordsTableModel = new DefaultTableModel();
        _usedWordsTableModel.setColumnIdentifiers(WidgetsViewCustomizations.USED_WORDS_TABLE_HEADERS);

        // Заполнить таблицу данными и отметить текущего активного игрока

        this.setModel(_usedWordsTableModel);
    }

    private void setupTableView() {
        this.getTableHeader().setReorderingAllowed(false);
        this.getTableHeader().setResizingAllowed(false);
        this.getTableHeader().setOpaque(false);
        this.getTableHeader().setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        this.setCellSelectionEnabled(false);
        this.setOpaque(false);
        this.setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        this.setFocusable(false);
    }

    private void add() {

    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(super.getPreferredSize().width,
                getRowHeight() * getRowCount());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };
}
