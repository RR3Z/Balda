import model.GameField;
import model.GameModel;
import model.Player;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import ui.*;
import ui.tables.PlayersScoreTable;
import ui.tables.UsedWordsTable;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainWindow extends JFrame {
    private GameModel _gameModel;

    public MainWindow() {
        super("Балда");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.getContentPane().setLayout(new FlowLayout());

        // Создание главного меню
        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(createMainMenu());
        this.setJMenuBar(mainMenuBar);

        // Расположить окно посередине экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = new Dimension(400, 200);
        this.setBounds(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2, windowSize.width, windowSize.height);
    }

    private JMenu createMainMenu() {
        JMenu mainMenu = new JMenu("Главное меню");

        JMenuItem start = new JMenuItem(new StartNewGameAction());
        JMenuItem exit = new JMenuItem(new ExitAction());

        mainMenu.add(start);
        mainMenu.add(exit);

        return mainMenu;
    }

    private void startNewGame(int width, int height) {
        _gameModel = new GameModel(width, height);
        _gameModel.addGameModelListener(new GameController());
        _gameModel.startGame();

        // Очистить содержимое панели
        JPanel content = (JPanel) this.getContentPane();
        content.removeAll();

        // ----------- Добавить виджеты -----------
        //content.add(new JScrollPane(new PlayersScoreTable(_gameModel)));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(new GameFieldWidget(_gameModel));
        centerPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        centerPanel.add(new PlayerActionsWidget(_gameModel));
        centerPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        centerPanel.add(new KeyboardWidget(_gameModel));
        content.add(centerPanel);

        //content.add(new JScrollPane(new UsedWordsTable(_gameModel, _gameModel.wordsDB())));
        // ----------------------------------------

        this.pack();

        // Расположить окно посередине экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();
        this.setBounds(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2, windowSize.width, windowSize.height);
    }

    private class GameController implements GameModelListener {
        private JPanel createGameOverPanel(List<Player> winners) {
            JPanel gameOverPanel = new JPanel();
            gameOverPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

            JLabel label = new JLabel();
            String message = "";
            if(winners.size() == 1) {
                message = "<html>" + "<div style='text-align: center;'>" + "Игра окончена.<br>Победитель: " +
                        winners.get(winners.size() - 1) + "<br>Желаете начать новую игру?" + "</div></html>";

                label.setText(message);
            } else if (winners.size() > 1) {
                message = "<html><div style='text-align: center;'>" + "Игра окончена.<br>Победители: ";
                for(Player player: winners) {
                    message += "<br>" + player.name();
                }
                message += "<br>Желаете начать новую игру?" + "</div></html>";

                label.setText(message);
            }

            gameOverPanel.add(label);

            return gameOverPanel;
        }

        @Override
        public void gameIsFinished(GameModelEvent event) {
            // TODO: Выводить окошко о победе, блокировать все виджеты, выводить кто победил
            int result = JOptionPane.showConfirmDialog(MainWindow.this, createGameOverPanel(event.winners()), "Настройки игры", JOptionPane.YES_NO_OPTION);

            if(result == JOptionPane.YES_OPTION) {
                // restart game
            }
        }

        @Override
        public void playerExchanged(GameModelEvent event) {
            // DON'T NEED IT HERE
        }
    }

    private class StartNewGameAction extends AbstractAction {
        private JSpinner _widthSpinner;
        private JSpinner _heightSpinner;

        public StartNewGameAction() {
            this.putValue(NAME, "Новая игра");
        }

        private JPanel createGameSettingsPanel() {
            JPanel gameSettingsPanel = new JPanel();

            // --------- Панель с размерами поля ---------
            JPanel fieldSizesPanel = new JPanel();

            JLabel fieldSizesLabel = new JLabel("Выберите размеры поля:");

            _widthSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_FIELD_SIZE, GameWidgetUtils.MAX_FIELD_SIZE, 1));
            _heightSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_FIELD_SIZE, GameWidgetUtils.MAX_FIELD_SIZE, 1));

            fieldSizesPanel.add(fieldSizesLabel);
            fieldSizesPanel.add(_widthSpinner);
            fieldSizesPanel.add(_heightSpinner);
            // -----------------------------------------

            gameSettingsPanel.add(fieldSizesPanel);

            return gameSettingsPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(MainWindow.this, createGameSettingsPanel(), "Настройки игры", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                MainWindow.this.startNewGame((int)_widthSpinner.getValue(), (int)_heightSpinner.getValue());
            }
        }
    }

    private class ExitAction extends AbstractAction {
        public ExitAction() {
            this.putValue(NAME, "Выход");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}