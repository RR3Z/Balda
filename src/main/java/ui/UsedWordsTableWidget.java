package ui;

import model.WordsDB;
import model.events.WordsDBEvent;
import model.events.WordsDBListener;
import ui.tables.CustomJTable;

import javax.swing.table.DefaultTableModel;

public class UsedWordsTableWidget extends CustomJTable {
    public UsedWordsTableWidget(WordsDB wordsDB, Object[] headers) {
        super(headers);
        wordsDB.addWordsDBListener(new WordsDBController());
    }

    private void add(String playerName, String word) {
        ((DefaultTableModel)this.getModel()).addRow(new Object[]{word, playerName});
    }

    private class WordsDBController implements WordsDBListener {
        @Override
        public void addedToUsedWords(WordsDBEvent event) {
            if(event.player() != null) {
                UsedWordsTableWidget.this.add(event.player().name(), event.word());
            } else {
                UsedWordsTableWidget.this.add("-", event.word());
            }
        }

        @Override
        public void addedNewWordToDictionary(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void wordAlreadyUsed(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void wordNotAllowed(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }
    }
}
