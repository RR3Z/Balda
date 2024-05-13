package ui;

import model.GameModel;
import model.Player;
import model.enums.PlayerState;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import ui.enums.ColorType;
import ui.tables.CustomJTable;
import ui.utils.GameWidgetUtils;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersScoreTableWidget extends CustomJTable {
    private Map<Integer, Player> _rowIndexToPlayer = new HashMap<>();

    public PlayersScoreTableWidget(GameModel gameModel, Object[] headers) {
        super(headers);
        gameModel.addGameModelListener(new GameController());

        DefaultTableModel playersScoreTableModel = (DefaultTableModel)this.getModel();
        List<Player> players = gameModel.players();
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            playersScoreTableModel.addRow(new Object[]{ player.name(), player.scoreCounter().score() });
            _rowIndexToPlayer.put(i, player);
        }
    }

    private void update() {
        DefaultTableModel playersScoreTableModel = (DefaultTableModel)this.getModel();

        for (int i = 0; i < playersScoreTableModel.getRowCount(); i++) {
            Player player = _rowIndexToPlayer.get(i);

            if(player.state() == PlayerState.SELECTING_LETTER) {
                highlightRow(i, GameWidgetUtils.color(ColorType.ACTIVE_PLAYER), GameWidgetUtils.color(ColorType.TRANSPARENT));
            }

            playersScoreTableModel.setValueAt(player.scoreCounter().score(), i, playersScoreTableModel.getColumnCount() - 1);
        }
    }

    private class GameController implements GameModelListener {
        @Override
        public void playerExchanged(GameModelEvent event) {
            PlayersScoreTableWidget.this.update();
        }

        @Override
        public void placedStartWord(GameModelEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void gameIsFinished(GameModelEvent event) {
            PlayersScoreTableWidget.this.update();
        }
    }
}
