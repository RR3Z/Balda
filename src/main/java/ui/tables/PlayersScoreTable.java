package ui.tables;

import model.GameModel;
import model.Player;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersScoreTable extends JTable {
    private DefaultTableModel _playersScoreTableModel;
    private Map<Integer, Player> _rowIndexToPlayer = new HashMap<>();

    public PlayersScoreTable(GameModel gameModel) {
        super();
        gameModel.addGameModelListener(new GameController());

        // Задаю внешний вид
        setupTableView();

        // Заполняю таблицу данными
        _playersScoreTableModel = new DefaultTableModel();
        _playersScoreTableModel.setColumnIdentifiers(WidgetsViewCustomizations.PLAYERS_SCORE_TABLE_HEADERS);

        List<Player> players = gameModel.players();
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            _playersScoreTableModel.addRow(new Object[]{player.name(), player.scoreCounter().score()});
            _rowIndexToPlayer.put(i, player);
        }

        this.setModel(_playersScoreTableModel);
    }

    private void setupTableView() {
        JTableHeader header = this.getTableHeader();
        header.setOpaque(false);
        header.setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        this.setOpaque(false);
        this.setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
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
        this.setDefaultRenderer(Object.class, tableCellRenderer);
        header.setDefaultRenderer(tableCellRenderer);
        this.setIntercellSpacing(new Dimension(0, 0));

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        this.setCellSelectionEnabled(false);
        this.setFocusable(false);
    }

    private void update() {
        for (int i = 0; i < _playersScoreTableModel.getRowCount(); i++) {
            Player player = _rowIndexToPlayer.get(i);

            _playersScoreTableModel.setValueAt(player.scoreCounter().score(), i, getColumn(WidgetsViewCustomizations.PLAYERS_SCORE_TABLE_HEADERS[1]).getModelIndex());
        }
    }

    private class GameController implements GameModelListener {
        @Override
        public void playerExchanged(GameModelEvent event) {
            PlayersScoreTable.this.update();
        }

        @Override
        public void gameIsFinished(GameModelEvent event) {
            // DON'T NEED IT HERE
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };
}
