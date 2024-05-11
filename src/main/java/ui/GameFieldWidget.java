package ui;

import model.Cell;
import model.GameField;
import model.GameModel;
import model.Player;
import model.enums.PlayerState;
import model.events.*;
import org.jetbrains.annotations.NotNull;
import ui.utils.GameWidgetUtils;
import ui.utils.ButtonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    private final int CELL_SIZE = 50;

    private GameModel _gameModel;
    private Map<Cell, JButton> _cells = new HashMap<>();

    public GameFieldWidget(GameModel gameModel) {
        super();
        this.setEnabled(false);

        _gameModel = gameModel;
        GameField gameField = _gameModel.gameField();
        gameField.addGameFieldListener(new GameFieldController());

        for(Player player: _gameModel.players()) {
            player.addPlayerActionListener(new PlayerController()); // TODO: подписываться по мере активновсти игроков
        }

        fillWidget(gameField.height(), gameField.width());

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));
        this.setMaximumSize(new Dimension(
                gameField.width() * CELL_SIZE,
                gameField.height() * CELL_SIZE
                )
        );
    }

    private void fillWidget(int numberOfRows, int numberOfColumns) {
        GameField gameField = _gameModel.gameField();

        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < numberOfColumns; j++){
                Cell cell = gameField.cell(new Point(j, i));

                JButton cellButton = new JButton();
                setupButtonView(cellButton);
                cellButton.addMouseListener(new CellButtonMouseListener(cellButton));

                if(cell.letter() != null) {
                    cellButton.setText(String.valueOf(cell.letter()));
                }

                _cells.put(cell, cellButton);

                this.add(cellButton);
            }
        }
    }

    private void setupButtonView(JButton button) {
        button.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));

        button.setModel(new ButtonUtils.FixedStateButtonModel());

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBackground(GameWidgetUtils.getColor(ColorType.INACTIVE_CELL));

        button.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.getColor(ColorType.DEFAULT_BORDER)));
        button.setBorderPainted(true);

        button.setFocusable(false);
    }

    private class CellButtonMouseListener extends MouseAdapter {
        private JButton _button;

        public CellButtonMouseListener(JButton button) {
            _button = button;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(GameFieldWidget.this.isEnabled()) {
                _gameModel.activePlayer().chooseCell(GameWidgetUtils.getKeyByValue(_cells, _button));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(GameFieldWidget.this.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.getColor(ColorType.HOVERED_BORDER)));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.setBorder(BorderFactory.createLineBorder(GameWidgetUtils.getColor(ColorType.DEFAULT_BORDER)));
        }
    }

    private class PlayerController implements PlayerActionListener {

        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            GameFieldWidget.this.setEnabled(
                    event.player().state() == PlayerState.PLACES_LETTER || event.player().state() == PlayerState.FORMS_WORD
            );
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            for(JButton button: _cells.values()) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            Cell changedCell = event.cell();
            _cells.get(changedCell).setText(String.valueOf(changedCell.letter()));
            _cells.get(changedCell).setOpaque(true);
            _cells.get(changedCell).setContentAreaFilled(true);
            _cells.get(changedCell).setBackground(GameWidgetUtils.getColor(ColorType.CHANGED_CELL));
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            Cell selectedCell = event.cell();
            _cells.get(selectedCell).setOpaque(true);
            _cells.get(selectedCell).setContentAreaFilled(true);
            _cells.get(selectedCell).setBackground(GameWidgetUtils.getColor(ColorType.CELL_IN_WORD));
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            for(JButton button: _cells.values()) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }

            JButton selectedCell = _cells.get(event.cell());
            if(selectedCell != null) {
                selectedCell.setOpaque(true);
                selectedCell.setContentAreaFilled(true);
                selectedCell.setBackground(GameWidgetUtils.getColor(ColorType.CHANGED_CELL));
                selectedCell.setText(String.valueOf(event.cell().letter()));
            }
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void addedNewWordToDictionary(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void failedToAddNewWordToDictionary(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseWrongCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void submittedWord(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void failedToSubmitWord(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }
    }

    private class GameFieldController implements GameFieldListener {
        @Override
        public void forgetChangedCell(GameFieldEvent event) {
            JButton changedCell = _cells.get(event.cell());
            changedCell.setText("");
        }
    }
}
