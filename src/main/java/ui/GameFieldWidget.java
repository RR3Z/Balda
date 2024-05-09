package ui;

import model.Cell;
import model.GameField;
import ui.customElements.CellButton;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    private GameField _gameField; // TODO: Нужна ли постоянная ссылка?
    private Map<Cell, CellButton> _cells = new HashMap<>();

    public GameFieldWidget(GameField gameField) {
        super();
        _gameField = gameField;

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));

        fillWidget(gameField.height(), gameField.width());
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
}
