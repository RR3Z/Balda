package ui;

import model.GameModel;
import model.Player;
import model.enums.GameState;
import model.events.*;
import org.jetbrains.annotations.NotNull;
import ui.menus.MainMenu;
import ui.panels.GameOverPanel;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private GameModel _gameModel;

    public MainWindow() {
        super("Балда");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.getContentPane().setLayout(new GridBagLayout());

        JMenuBar mainMenuBar = new JMenuBar();
        MainMenu mainMenu = new MainMenu(this);
        mainMenuBar.add(mainMenu);
        this.setJMenuBar(mainMenuBar);

        JLabel text = new JLabel("Игра \"Балда\"");
        text.setFont(GameWidgetUtils.font(24));
        this.add(text);

        UIManager.put("OptionPane.messageFont", GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        UIManager.put("OptionPane.buttonFont", GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));

        this.setMinimumSize(new Dimension(400, 200));

        GameWidgetUtils.placeContainerInCenter(this);
    }

    public void startNewGame(int width, int height) {
        _gameModel = new GameModel(width, height);
        _gameModel.addGameModelListener(new GameModelController());

        _gameModel.wordsDB().addWordsDBListener(new WordsDBController());

        for(Player player: _gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        // Clear ui.MainWindow
        JPanel content = (JPanel) this.getContentPane();
        content.removeAll();

        // Widgets
        // ============================================================================================================
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(5,5,5,5);
        // Left table
        JScrollPane playersScoreTablePane = new JScrollPane(new PlayersScoreTableWidget(_gameModel, new Object[]{ "Игрок", "Очки" }));
        playersScoreTablePane.setBorder(BorderFactory.createEmptyBorder());
        content.add(playersScoreTablePane, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        // GameField widget
        content.add(new GameFieldWidget(_gameModel), constraints);
        content.add(Box.createRigidArea(new Dimension(5, 5)));

        constraints.gridx = 1;
        constraints.gridy = 1;
        // PlayerActions widget
        content.add(new PlayerActionsWidget(_gameModel), constraints);
        content.add(Box.createRigidArea(new Dimension(5, 5)));

        constraints.gridx = 1;
        constraints.gridy = 2;
        // Alphabet widget
        content.add(new AlphabetWidget(_gameModel), constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        // Right table
        JScrollPane usedWordsTablePane = new JScrollPane(new UsedWordsTableWidget(_gameModel.wordsDB(), new Object[]{ "Слово", "Игрок" }));
        usedWordsTablePane.setBorder(BorderFactory.createEmptyBorder());
        content.add(usedWordsTablePane, constraints);
        // ============================================================================================================

        _gameModel.startGame();

        this.pack();

        GameWidgetUtils.placeContainerInCenter(this);
    }

    private void disableAllGameWidgets(@NotNull Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(false);

            if (!(component instanceof JMenuBar || component instanceof JMenu || component instanceof JMenuItem) && component instanceof Container) {
                disableAllGameWidgets((Container) component);
            }
        }
    }

    private class GameModelController implements GameModelListener {
        @Override
        public void gameIsFinished(GameModelEvent event) {
            if(_gameModel.state() == GameState.FINISHED) {
                MainWindow.this.disableAllGameWidgets(MainWindow.this);

                JOptionPane.showMessageDialog(null, new GameOverPanel(event.winners()), "Конец игры", JOptionPane.PLAIN_MESSAGE);
            }
        }

        @Override
        public void playerExchanged(GameModelEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void placedStartWord(GameModelEvent event) {
            // DON'T NEED IT HERE
        }
    }

    private class PlayerController implements PlayerActionListener {
        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            if(event.player() == _gameModel.activePlayer()) {
                String message = "<html><div style='text-align: center;'>" + GameWidgetUtils.htmlFont(GameWidgetUtils.OPTION_PANE_FONT_SIZE) + "В составленном слове отсутствует измененная ячейка" + "</div></html>";
                JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        }

        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void skippedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void finishedTurn(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void placedLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseLetter(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void choseCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }
    }

    private class WordsDBController implements WordsDBListener {
        @Override
        public void addedNewWordToDictionary(WordsDBEvent event) {
            if(event.player() == _gameModel.activePlayer()) {
                String message = "<html><div style='text-align:center;'>" + GameWidgetUtils.htmlFont(GameWidgetUtils.OPTION_PANE_FONT_SIZE) +
                        "Слово \"" + event.word() + "\" успешно добавлено" + "</div></html>";
                JOptionPane.showMessageDialog(null, message, "Новое слово", JOptionPane.INFORMATION_MESSAGE);
                _gameModel.activePlayer().submitWord();
            }
        }

        @Override
        public void wordAlreadyUsed(WordsDBEvent event) {
            if(event.player() == _gameModel.activePlayer()) {
                String htmlFont = GameWidgetUtils.htmlFont(GameWidgetUtils.OPTION_PANE_FONT_SIZE);
                String message = "<html><div style='text-align: center;'>" + htmlFont + "Слово уже было использовано.</div></html>";
                    JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        }

        @Override
        public void wordNotAllowed(WordsDBEvent event) {
            if(event.player() == _gameModel.activePlayer()) {
                String htmlFont = GameWidgetUtils.htmlFont(GameWidgetUtils.OPTION_PANE_FONT_SIZE);
                String message = "<html><div style='text-align: center;'>" + htmlFont + "Было составлено неизвестное слово \"" + event.word() + "\". <br>Добавить в словарь?</div></html>";

                int result = JOptionPane.showConfirmDialog(null, message, "Неизвестное слово", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.OK_OPTION) {
                    _gameModel.activePlayer().addNewWordToDictionary();
                } else {
                    _gameModel.activePlayer().cancelActionOnField();
                }
            }
        }

        @Override
        public void addedToUsedWords(WordsDBEvent event) {
            // DON'T NEED IT HERE
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}