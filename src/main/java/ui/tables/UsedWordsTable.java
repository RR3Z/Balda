package ui.tables;

import model.GameModel;
import model.WordsDB;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsedWordsTable extends JTable {
    private DefaultTableModel _usedWordsTableModel;

    public UsedWordsTable(GameModel gameModel, WordsDB wordsDB) {
        super();
        //TODO
        //gameModel.addGameModelListener(new GameController());
        //wordsDB.addWordsDBListener(new WordsDBController());

        setupTableComponents();
        setupTableView();
    }

    private void setupTableComponents() {
        _usedWordsTableModel = new DefaultTableModel();
        _usedWordsTableModel.setColumnIdentifiers(WidgetsViewCustomizations.USED_WORDS_TABLE_HEADERS);
        this.setModel(_usedWordsTableModel);
    }

    private void setupTableView() {
        this.getTableHeader().setReorderingAllowed(false);
        this.getTableHeader().setResizingAllowed(false);
        this.getTableHeader().setOpaque(false);
        this.getTableHeader().setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        this.setCellSelectionEnabled(false);
        this.setOpaque(false);
        this.setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        this.setFocusable(false);
    }

    private void add(String playerName, String word) {
        _usedWordsTableModel.addRow(new Object[]{word, playerName});
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(super.getPreferredSize().width,
                getRowHeight() * getRowCount());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };
}
