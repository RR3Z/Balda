package ui;

import model.Cell;
import model.GameField;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameFieldWidget extends JPanel {
    private GameField _gameField; // TODO: хз нужна ли мне постоянная ссылка
    private Map<Cell, CellWidget> _cells = new HashMap<>();

    public GameFieldWidget(GameField gameField) {
        super();
        _gameField = gameField;

        this.setLayout(new GridLayout(gameField.height(), gameField.width()));

        // Создать CellWidgets
        for(int i = 0; i < gameField.height(); i++) {
            this.add(createRow(i));
        }
    }

    private JPanel createRow(int rowIndex) {
        JPanel rowOfCellWidgets = new JPanel();
        rowOfCellWidgets.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));

        for(int i = 0; i < _gameField.width(); i++) {
            Cell cell = _gameField.cell(new Point(i, rowIndex));
            CellWidget cellWidget = new CellWidget();

            if(cell.letter() != null) {
                cellWidget.setCharacter(cell.letter());
            }

            _cells.put(cell, cellWidget);

            rowOfCellWidgets.add(cellWidget);
        }

        return rowOfCellWidgets;
    }
}
