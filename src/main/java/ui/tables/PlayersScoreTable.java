package ui.tables;

import model.GameModel;
import model.Player;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;
import ui.utils.TableUtils;

import javax.swing.*;
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

        this.setDefaultRenderer(Object.class, TableUtils.CUSTOM_TABLE_CELL_RENDERER);
        header.setDefaultRenderer(TableUtils.CUSTOM_TABLE_CELL_RENDERER);
        this.setIntercellSpacing(new Dimension(0, 0));

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        this.setCellSelectionEnabled(false);
        this.setFocusable(false);
    }

    private void update() {
        for (int i = 0; i < _playersScoreTableModel.getRowCount(); i++) {
            Player player = _rowIndexToPlayer.get(i);

            _playersScoreTableModel.setValueAt(player.scoreCounter().score(), i, getColumn(HEADERS[1]).getModelIndex());
        }
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
