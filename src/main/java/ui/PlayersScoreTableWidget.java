package ui;

import model.GameModel;
import model.Player;
import model.ScoreCounter;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import model.events.ScoreCounterEvent;
import model.events.ScoreCounterListener;
import org.jetbrains.annotations.NotNull;
import ui.enums.ColorType;
import ui.tables.CustomJTable;
import ui.utils.GameWidgetUtils;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersScoreTableWidget extends CustomJTable {
    private Map<ScoreCounter, Integer> _scoreCounterToRowIndex = new HashMap<>();

    public PlayersScoreTableWidget(GameModel gameModel, Object[] headers) {
        super(headers);

        DefaultTableModel playersScoreTableModel = (DefaultTableModel)this.getModel();

        List<Player> players = gameModel.players();
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.addPlayerActionListener(new PlayerActionController());

            ScoreCounter scoreCounter = player.scoreCounter();
            scoreCounter.addScoreCounterListener(new ScoreCounterController());

            playersScoreTableModel.addRow(new Object[]{ player.name(), scoreCounter.score() });

            _scoreCounterToRowIndex.put(scoreCounter, i);
        }

    }

    private void updateScoreValue(int rowIndex, int newValue) {
        DefaultTableModel playersScoreTableModel = (DefaultTableModel)this.getModel();

        playersScoreTableModel.setValueAt(newValue, rowIndex, playersScoreTableModel.getColumnCount() - 1);
    }

    private class ScoreCounterController implements ScoreCounterListener {
        @Override
        public void scoreChanged(ScoreCounterEvent event) {
            ScoreCounter scoreCounter = event.scoreCounter();
            PlayersScoreTableWidget.this.updateScoreValue(_scoreCounterToRowIndex.get(scoreCounter), scoreCounter.score());
        }
    }

    private class PlayerActionController implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            Player player = event.player();

            if(player.state() == PlayerState.SELECTING_LETTER) {
                highlightRow(_scoreCounterToRowIndex.get(player.scoreCounter()), GameWidgetUtils.color(ColorType.ACTIVE_PLAYER));
            }

            if(player.state() == PlayerState.WAITING_TURN) {
                highlightRow(_scoreCounterToRowIndex.get(player.scoreCounter()), GameWidgetUtils.color(ColorType.TRANSPARENT));
            }
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }
    }
}
