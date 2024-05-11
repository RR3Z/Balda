import model.GameModel;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import ui.*;
import ui.panels.GameOverPanel;
import ui.panels.GameSettingsPanel;
import ui.tables.PlayersScoreTable;
import ui.tables.UsedWordsTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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

        JMenuItem startNewGameItem = new JMenuItem(new StartNewGameAction());
        JMenuItem exitGameItem = new JMenuItem(new ExitAction());

        mainMenu.add(startNewGameItem);
        mainMenu.add(exitGameItem);

        return mainMenu;
    }

    private void startNewGame(int width, int height) {
        _gameModel = new GameModel(width, height);
        _gameModel.addGameModelListener(new GameController());

        // Очистить содержимое панели
        JPanel content = (JPanel) this.getContentPane();
        content.removeAll();

        // ----------- Добавить виджеты -----------
        JScrollPane playersScoreTablePane = new JScrollPane(new PlayersScoreTable(_gameModel));
        playersScoreTablePane.setBorder(BorderFactory.createEmptyBorder());
        content.add(playersScoreTablePane);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(new GameFieldWidget(_gameModel));
        centerPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        centerPanel.add(new PlayerActionsWidget(_gameModel));
        centerPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        centerPanel.add(new KeyboardWidget(_gameModel));
        content.add(centerPanel);

        JScrollPane usedWordsTablePane = new JScrollPane(new UsedWordsTable(_gameModel.wordsDB()));
        usedWordsTablePane.setBorder(BorderFactory.createEmptyBorder());
        content.add(usedWordsTablePane);
        // ----------------------------------------

        _gameModel.startGame();

        this.pack();

        // Расположить окно посередине экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();
        this.setBounds(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2, windowSize.width, windowSize.height);
    }

    private void disableAllGameWidgets(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(false);

            if (!(component instanceof JMenuBar || component instanceof JMenu || component instanceof JMenuItem) && component instanceof Container) {
                disableAllGameWidgets((Container) component);
            }
        }
    }

    private class StartNewGameAction extends AbstractAction {
        public StartNewGameAction() {
            this.putValue(NAME, "Новая игра");
        }
        GameSettingsPanel _gameSettings = new GameSettingsPanel();

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(MainWindow.this, _gameSettings, "Настройки игры", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                MainWindow.this.startNewGame(_gameSettings.getWidthSpinnerValue(), _gameSettings.getHeightSpinnerValue());
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

    private class GameController implements GameModelListener {
        @Override
        public void gameIsFinished(GameModelEvent event) {
            MainWindow.this.disableAllGameWidgets(MainWindow.this);

            JOptionPane.showMessageDialog(MainWindow.this, new GameOverPanel(event.winners()), "Конец игры", JOptionPane.PLAIN_MESSAGE);
        }

        @Override
        public void playerExchanged(GameModelEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void definedStartWord(GameModelEvent event) {
            // DON'T NEED IT HERE
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}