package ui.tables;

import model.WordsDB;
import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;
import ui.utils.TableUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UsedWordsTable extends JTable {
    private final Object[] HEADERS = new Object[] {"Слово", "Игрок"};

    private DefaultTableModel _usedWordsTableModel;

    public UsedWordsTable(WordsDB wordsDB) {
        super();
        wordsDB.addWordsDBListener(new WordsDBController());

        setupTableView();

        _usedWordsTableModel = new DefaultTableModel();
        _usedWordsTableModel.setColumnIdentifiers(HEADERS);

        this.setModel(_usedWordsTableModel);
    }

    private void setupTableView() {
        JTableHeader header = this.getTableHeader();
        header.setOpaque(false);
        header.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));

        this.setOpaque(false);
        this.setBackground(GameWidgetUtils.getColor(ColorType.TRANSPARENT));

        this.setDefaultRenderer(Object.class, TableUtils.CUSTOM_TABLE_CELL_RENDERER);
        header.setDefaultRenderer(TableUtils.CUSTOM_TABLE_CELL_RENDERER);
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
            if(event.player() != null) {
                UsedWordsTable.this.add(event.player().name(), event.word());
            } else {
                UsedWordsTable.this.add("-", event.word());
            }
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
