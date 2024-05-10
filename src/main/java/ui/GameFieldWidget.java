package ui;

import model.Cell;
import model.GameField;
import ui.buttons.CellButton;
import ui.enums.ActivityState;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    ActivityState _widgetState;

    private GameField _gameField; // TODO: Нужна ли постоянная ссылка?
    private Map<Cell, CellButton> _cells = new HashMap<>();

    public GameFieldWidget(GameField gameField) {
        super();
        _widgetState = ActivityState.DISABLED;
        _gameField = gameField;

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));

        fillWidget(gameField.height(), gameField.width());

        this.setMaximumSize(new Dimension(
                gameField.width() * WidgetsViewCustomizations.CELL_BUTTON_SIZE,
                gameField.height() * WidgetsViewCustomizations.CELL_BUTTON_SIZE
                )
        );
    }

    private void fillWidget(int numberOfRows, int numberOfColumns) {
        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < numberOfColumns; j++){
                Cell cell = _gameField.cell(new Point(j, i));
                CellButton cellButton = new CellButton();

                if(cell.letter() != null) {
                    cellButton.setLetter(cell.letter());
                }

                _cells.put(cell, cellButton);

                this.add(cellButton);
            }
        }
    }

    private void changeActivity() {
        if(_widgetState == ActivityState.ENABLED) {
            for (CellButton button : _cells.values()) {
                button.setEnabled(false);
            }

            _widgetState = ActivityState.DISABLED;
        }
        else {
            for (CellButton button : _cells.values()) {
                button.setEnabled(true);
            }

            _widgetState = ActivityState.ENABLED;
        }

    }
}
