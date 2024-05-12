package ui;

import model.Cell;
import model.GameField;
import model.GameModel;
import model.Player;
import model.enums.PlayerState;
import model.events.*;
import org.jetbrains.annotations.NotNull;
import ui.buttons.CellButton;
import ui.enums.BorderType;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    private GameModel _gameModel;
    private Map<Cell, CellButton> _cells = new HashMap<>();

    public GameFieldWidget(GameModel gameModel, GameField gameField) {
        super();
        this.setEnabled(false);

        _gameModel = gameModel;
        gameField.addGameFieldListener(new GameFieldController());

        for(Player player: _gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        fillWidget(gameField);

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));

        this.setMaximumSize(new Dimension(
                gameField.width() * CellButton.CELL_SIZE,
                gameField.height() * CellButton.CELL_SIZE
                )
        );
    }

    private void fillWidget(GameField gameField) {
        for(int i = 0; i < gameField.height(); i++) {
            for(int j = 0; j < gameField.width(); j++){
                Cell cell = gameField.cell(new Point(j, i));

                CellButton cellButton = new CellButton();
                cellButton.addMouseListener(new CellButtonMouseListener(cellButton));

                if(cell.letter() != null) {
                    cellButton.setText(String.valueOf(cell.letter()));
                }

                _cells.put(cell, cellButton);

                this.add(cellButton);
            }
        }
    }

    private class CellButtonMouseListener extends MouseAdapter {
        private CellButton _button;

        public CellButtonMouseListener(CellButton button) {
            _button = button;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(GameFieldWidget.this.isEnabled()) {
                _gameModel.activePlayer().chooseCell(GameWidgetUtils.getKeyByValue(_cells, _button));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(GameFieldWidget.this.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.getColor(ColorType.DEFAULT_HIGHLIGHTED_BORDER),
                        GameWidgetUtils.getBorderThickness(BorderType.BOLD))
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.setBorder(BorderFactory.createLineBorder(
                    GameWidgetUtils.getColor(ColorType.DEFAULT_BORDER),
                    GameWidgetUtils.getBorderThickness(BorderType.DEFAULT))
            );
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
            for(CellButton button: _cells.values()) {
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
            for(CellButton button: _cells.values()) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }

            CellButton selectedCell = _cells.get(event.cell());
            if(selectedCell != null) {
                selectedCell.setOpaque(true);
                selectedCell.setContentAreaFilled(true);
                selectedCell.setBackground(GameWidgetUtils.getColor(ColorType.CHANGED_CELL));
                selectedCell.setText(String.valueOf(event.cell().letter()));
            }

            GameFieldWidget.this.paintImmediately(getVisibleRect());
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            for(CellButton button: _cells.values()) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }

            GameFieldWidget.this.paintImmediately(getVisibleRect());
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void addedNewWordToDictionary(@NotNull PlayerActionEvent event) {
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
            CellButton changedCell = _cells.get(event.cell());
            changedCell.setText("");
        }

        @Override
        public void placedStartWord(GameFieldEvent event) {
            GameField gameField = event.gameField();

            for(int i = 0; i < gameField.height(); i++) {
                for(int j = 0; j < gameField.width(); j++){
                    Cell cell = gameField.cell(new Point(j, i));

                    if(cell.letter() != null) {
                        GameFieldWidget.this._cells.get(cell).setText(String.valueOf(cell.letter()));
                    }
                }
            }

            GameFieldWidget.this.repaint();
        }
    }
}
