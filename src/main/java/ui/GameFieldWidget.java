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
import ui.enums.CellButtonState;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;
import ui.utils.MapUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    private GameModel _gameModel;
    private Map<Cell, CellButton> _cells = new HashMap<>();

    public GameFieldWidget(GameModel gameModel) {
        super();
        this.setEnabled(false);

        _gameModel = gameModel;

        GameField gameField = _gameModel.gameField();
        gameField.addGameFieldListener(new GameFieldController());

        for(Player player: _gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        fillWidget();

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));

        this.setMaximumSize(new Dimension(
                gameField.width() * CellButton.CELL_SIZE,
                gameField.height() * CellButton.CELL_SIZE
                )
        );
    }

    private void fillWidget() {
        GameField gameField = _gameModel.gameField();

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
                _gameModel.activePlayer().chooseCell(MapUtils.getKeyByValue(_cells, _button));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(GameFieldWidget.this.isEnabled()) {
                _button.highlight(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.highlight(false);
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
            if(event.player().state() == PlayerState.WAITING_TURN) {
                for(CellButton button: _cells.values()) {
                    button.changeState(CellButtonState.DEFAULT);
                }
            }
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            if(GameFieldWidget.this.isEnabled() && event.player() == _gameModel.activePlayer()) {
                Cell changedCell = event.cell();
                CellButton button = _cells.get(changedCell);

                button.setText(String.valueOf(changedCell.letter()));
                button.changeState(CellButtonState.CHANGED);
            }
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            if(GameFieldWidget.this.isEnabled()  && event.player() == _gameModel.activePlayer()) {
                CellButton selectedCell = _cells.get(event.cell());
                selectedCell.changeState(CellButtonState.IN_WORD);
            }
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            if(event.player() == _gameModel.activePlayer()) {
                for(CellButton button: _cells.values()) {
                    button.changeState(CellButtonState.DEFAULT);
                }

                CellButton selectedCell = _cells.get(event.cell());
                if(selectedCell != null) {
                    selectedCell.setText(String.valueOf(event.cell().letter()));
                    selectedCell.changeState(CellButtonState.CHANGED);
                }

                GameFieldWidget.this.paintImmediately(getVisibleRect());
            }
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            if(event.player().state() == PlayerState.SKIPPED_TURN) {
                for(CellButton button: _cells.values()) {
                    button.changeState(CellButtonState.DEFAULT);
                }

                GameFieldWidget.this.paintImmediately(getVisibleRect());
            }
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }
    }

    private class GameFieldController implements GameFieldListener {
        @Override
        public void undoChangesOfChangedCell(GameFieldEvent event) {
            _cells.get(event.cell()).setText("");
        }

        @Override
        public void placedWord(GameFieldEvent event) {
            GameField gameField = event.gameField();

            for(int i = 0; i < gameField.height(); i++) {
                for(int j = 0; j < gameField.width(); j++){
                    Cell cell = gameField.cell(new Point(j, i));

                    if(cell.letter() != null) {
                        GameFieldWidget.this._cells.get(cell).setText(String.valueOf(cell.letter()));
                    }
                }
            }
        }
    }
}
