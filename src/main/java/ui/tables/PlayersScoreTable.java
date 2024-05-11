package ui.tables;

import model.GameModel;
import model.Player;
import model.enums.PlayerState;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;
import ui.utils.TableUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersScoreTable extends JTable {
    private final Object[] HEADERS = new Object[] {"Игрок", "Очки"};

    private DefaultTableModel _playersScoreTableModel;
    private Map<Integer, Player> _rowIndexToPlayer = new HashMap<>();

    public PlayersScoreTable(GameModel gameModel) {
        super();
        gameModel.addGameModelListener(new GameController());

        // Задаю внешний вид
        setupTableView();

        // Заполняю таблицу данными
        _playersScoreTableModel = new DefaultTableModel();
        _playersScoreTableModel.setColumnIdentifiers(HEADERS);

        List<Player> players = gameModel.players();
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            _playersScoreTableModel.addRow(new Object[]{ player.name(), player.scoreCounter().score() });
            _rowIndexToPlayer.put(i, player);
        }

        this.setModel(_playersScoreTableModel);
    }

    private void setupTableView() {
        JTableHeader header = this.getTableHeader();
        header.setOpaque(false);
        header.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));

        this.setOpaque(false);
        this.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));

        this.setDefaultRenderer(Object.class, TableUtils.DEFAULT_TABLE_CELL_RENDERER);
        header.setDefaultRenderer(TableUtils.HEADER_TABLE_CELL_RENDERER);
        this.setIntercellSpacing(new Dimension(0, 0));

        this.setFont(GameWidgetUtils.getFont());

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        this.setCellSelectionEnabled(false);
        this.setFocusable(false);
    }

    private void update() {
        for (int i = 0; i < _playersScoreTableModel.getRowCount(); i++) {
            Player player = _rowIndexToPlayer.get(i);

            if(player.state() == PlayerState.SELECTING_LETTER) {
                TableUtils.highlightRow(this, i, GameWidgetUtils.getColor(ColorType.ACTIVE_PLAYER), GameWidgetUtils.getColor(ColorType.TRANSPARENT));
            }

            _playersScoreTableModel.setValueAt(player.scoreCounter().score(), i, getColumn(HEADERS[1]).getModelIndex());
        }
    }

    private void highlightRow(int rowIndex) {
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
                    component.setBackground(GameWidgetUtils.getColor(ColorType.ACTIVE_PLAYER));
                } else {
                    component.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));
                }

                return component;
            }
        };

        for (int i = 0; i < this.getColumnCount(); i++) {
            this.getColumnModel().getColumn(i).setCellRenderer(highlightCellRenderer);
        }

        _playersScoreTableModel.fireTableDataChanged();
    }

    private class GameController implements GameModelListener {
        @Override
        public void playerExchanged(GameModelEvent event) {
            PlayersScoreTable.this.update();
        }

        @Override
        public void definedStartWord(GameModelEvent event) {
            // DON'T NEED IT HERE
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
