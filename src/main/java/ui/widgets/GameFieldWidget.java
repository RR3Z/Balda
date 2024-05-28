package ui.widgets;

import model.Cell;
import model.GameField;
import model.GameModel;
import model.enums.PlayerState;
import model.events.*;
import model.players.AbstractPlayer;
import org.jetbrains.annotations.NotNull;
import ui.buttons.CellButton;
import ui.enums.CellButtonVisualState;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    private Map<Cell, CellButton> _cells = new HashMap<>();

    public GameFieldWidget(@NotNull GameModel gameModel) {
        super();

        GameField gameField = gameModel.gameField();
        gameField.addGameFieldListener(new GameFieldController());

        for(AbstractPlayer player: gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        fillWidget(gameModel);

        changeActivity(false);

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));
        this.setMaximumSize(new Dimension(
                gameField.width() * CellButton.CELL_SIZE,
                gameField.height() * CellButton.CELL_SIZE
                )
        );
    }

    private void fillWidget(@NotNull GameModel gameModel) {
        GameField gameField = gameModel.gameField();

        for(int i = 0; i < gameField.height(); i++) {
            for(int j = 0; j < gameField.width(); j++){
                Cell cell = gameField.cell(new Point(j, i));

                CellButton cellButton = new CellButton(gameModel, cell);

                if(cell.letter() != null) {
                    cellButton.setText(String.valueOf(cell.letter()));
                }

                _cells.put(cell, cellButton);

                this.add(cellButton);
            }
        }
    }

    private void changeActivity(boolean widgetActivity) {
        GameFieldWidget.this.setEnabled(widgetActivity);

        for(CellButton cellButton: _cells.values()) {
            cellButton.setEnabled(widgetActivity);
        }
    }

    private void clearAllHighlights() {
        for(CellButton button: _cells.values()) {
            button.changeVisualState(CellButtonVisualState.DEFAULT);
        }
    }

    private class PlayerController implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            changeActivity(event.player().state() == PlayerState.PLACES_LETTER || event.player().state() == PlayerState.FORMS_WORD);
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            if(event.player().state() == PlayerState.WAITING_TURN) {
                GameFieldWidget.this.clearAllHighlights();
            }
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            if(GameFieldWidget.this.isEnabled() && event.player().state() == PlayerState.PLACES_LETTER) {
                Cell changedCell = event.cell();
                CellButton button = _cells.get(changedCell);

                button.setText(String.valueOf(changedCell.letter()));
                button.changeVisualState(CellButtonVisualState.CHANGED);
            }
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            if(GameFieldWidget.this.isEnabled()  && (event.player().state() == PlayerState.PLACES_LETTER || event.player().state() == PlayerState.FORMS_WORD)) {
                CellButton selectedCell = _cells.get(event.cell());
                selectedCell.changeVisualState(CellButtonVisualState.IN_WORD);
            }
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            if(event.player().state() != PlayerState.SKIPPED_TURN && event.player().state() != PlayerState.WAITING_TURN) {
                GameFieldWidget.this.clearAllHighlights();

                CellButton selectedCell = _cells.get(event.cell());
                if(selectedCell != null) {
                    selectedCell.setText(String.valueOf(event.cell().letter()));
                    selectedCell.changeVisualState(CellButtonVisualState.CHANGED);
                }

                GameFieldWidget.this.paintImmediately(getVisibleRect());
            }
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            if(event.player().state() == PlayerState.SKIPPED_TURN) {
                GameFieldWidget.this.clearAllHighlights();

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
            if(GameFieldWidget.this.isEnabled()) {
                _cells.get(event.cell()).setText("");
                _cells.get(event.cell()).changeVisualState(CellButtonVisualState.DEFAULT);
            }
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
