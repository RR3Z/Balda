package ui;

import model.Cell;
import model.GameField;
import model.GameModel;
import model.Player;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import org.jetbrains.annotations.NotNull;
import ui.utils.GameWidgetUtils;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    private GameModel _gameModel;
    private Map<Cell, JButton> _cells = new HashMap<>();

    public GameFieldWidget(GameModel gameModel) {
        super();
        this.setEnabled(false);

        _gameModel = gameModel;
        GameField gameField = _gameModel.gameField();

        for(Player player: _gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));

        fillWidget(gameField.height(), gameField.width());

        this.setMaximumSize(new Dimension(
                gameField.width() * WidgetsViewCustomizations.CELL_BUTTON_SIZE,
                gameField.height() * WidgetsViewCustomizations.CELL_BUTTON_SIZE
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
        button.setPreferredSize(new Dimension(WidgetsViewCustomizations.CELL_BUTTON_SIZE, WidgetsViewCustomizations.CELL_BUTTON_SIZE));

        button.setModel(new WidgetsViewCustomizations.FixedStateButtonModel());

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBackground(WidgetsViewCustomizations.CLICKED_CELL_BUTTON_COLOR);

        button.setBorder(BorderFactory.createLineBorder(
                WidgetsViewCustomizations.STANDART_CELL_BUTTON_BORDER_COLOR,
                WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
        );
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
                // Logic
                _gameModel.activePlayer().chooseCell(GameWidgetUtils.getKeyByValue(_cells, _button));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(GameFieldWidget.this.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        WidgetsViewCustomizations.HOVERED_CELL_BUTTON_BORDER_COLOR,
                        WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.setBorder(BorderFactory.createLineBorder(
                    WidgetsViewCustomizations.STANDART_CELL_BUTTON_BORDER_COLOR,
                    WidgetsViewCustomizations.CELL_BUTTON_BORDER_THICKNESS)
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
            _cells.get(changedCell).setBackground(Color.RED);// TODO: CHANGE COLOR
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            if(event.player() == _gameModel.activePlayer()) {
                Cell selectedCell = event.cell();
                _cells.get(selectedCell).setOpaque(true);
                _cells.get(selectedCell).setContentAreaFilled(true);
                _cells.get(selectedCell).setBackground(WidgetsViewCustomizations.CLICKED_CELL_BUTTON_COLOR);
            }
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            if(event.player().state() == PlayerState.SELECTING_LETTER) {
                // TODO: ЭТО НЕ РАБОТАЕТ!!!! Ячейка очищается раньше, чем отправляется ивент
                JButton selectedCell = _cells.get(event.cell());
                if(selectedCell != null) {
                    selectedCell.setOpaque(false);
                    selectedCell.setContentAreaFilled(false);
                    selectedCell.setText(String.valueOf(event.cell().letter()));
                }
            }

            if(event.player().state() == PlayerState.PLACES_LETTER) {
                for(JButton button: _cells.values()) {
                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                }

                JButton selectedCell = _cells.get(event.cell());
                if(selectedCell != null) {
                    selectedCell.setOpaque(true);
                    selectedCell.setContentAreaFilled(true);
                    selectedCell.setBackground(Color.RED);
                    selectedCell.setText(String.valueOf(event.cell().letter()));
                }
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
}
