package ui.tables;

import model.GameModel;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PlayersScoreTable extends JTable {
    private GameModel _gameModel;

    private DefaultTableModel _playersScoreTableModel;

    public PlayersScoreTable(GameModel gameModel) {
        super();
        _gameModel = gameModel;

        setupTableData();
        setupTableView();
    }

    private void setupTableData() {
        _playersScoreTableModel = new DefaultTableModel(2, 0);
        _playersScoreTableModel.setColumnIdentifiers(WidgetsViewCustomizations.PLAYERS_SCORE_TABLE_HEADERS);

        // Заполнить таблицу данными и отметить текущего активного игрока

        this.setModel(_playersScoreTableModel);
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

    private void update() {

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
