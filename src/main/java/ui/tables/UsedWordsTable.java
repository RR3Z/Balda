package ui.tables;

import model.GameModel;
import model.WordsDB;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UsedWordsTable extends JTable {
    private DefaultTableModel _usedWordsTableModel;

    public UsedWordsTable(WordsDB wordsDB) {
        super();
        wordsDB.addWordsDBListener(new WordsDBController());

        setupTableView();

        _usedWordsTableModel = new DefaultTableModel();
        _usedWordsTableModel.setColumnIdentifiers(WidgetsViewCustomizations.USED_WORDS_TABLE_HEADERS);

        this.setModel(_usedWordsTableModel);
    }

    private void setupTableView() {
        JTableHeader header = this.getTableHeader();
        header.setOpaque(false);
        header.setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        this.setOpaque(false);
        this.setBackground(WidgetsViewCustomizations.TRANSPARENT_COLOR);

        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK); // Цвет линий
                g.fillRect(0, 0, 1, getHeight()); // Левая вертикальная линия
                g.fillRect(0, 0, getWidth(), 1); // Верхняя горизонтальная линия
                g.fillRect(getWidth() - 1, 0, 1, getHeight()); // Правая вертикальная линия
                g.fillRect(0, getHeight() - 1, getWidth(), 1); // Нижняя горизонтальная линия
            }
        };
        this.setDefaultRenderer(Object.class, tableCellRenderer);
        header.setDefaultRenderer(tableCellRenderer);
        this.setIntercellSpacing(new Dimension(0, 0));

        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        this.setCellSelectionEnabled(false);
        this.setFocusable(false);
    }

    private void add(String playerName, String word) {
        _usedWordsTableModel.addRow(new Object[]{word, playerName});
    }

    private class WordsDBController implements WordsDBListener {
        @Override
        public void addedUsedWord(WordsDBEvent event) {
            UsedWordsTable.this.add(event.player().name(), event.word());
        }

        @Override
        public void failedToAddUsedWord(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void addedNewWordToDictionary(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void failedToAddNewWordToDictionary(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };
}
