import model.GameModel;
import model.events.GameModelEvent;
import model.events.GameModelListener;
import ui.*;
import ui.menus.MainMenu;
import ui.panels.GameOverPanel;
import ui.panels.GameSettingsPanel;
import ui.PlayersScoreTableWidget;
import ui.UsedWordsTableWidget;
import ui.utils.GameWidgetUtils;

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
        this.getContentPane().setLayout(new GridBagLayout());

        JMenuBar mainMenuBar = new JMenuBar();
        MainMenu mainMenu = new MainMenu();
        mainMenu.setStartNewGameAction(new StartNewGameAction());
        mainMenu.setExitGameAction(new ExitGameAction());
        mainMenuBar.add(mainMenu);
        this.setJMenuBar(mainMenuBar);
        
        UIManager.put("OptionPane.messageFont", GameWidgetUtils.getFont(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        UIManager.put("OptionPane.buttonFont", GameWidgetUtils.getFont(GameWidgetUtils.OPTION_PANE_FONT_SIZE));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = new Dimension(400, 200);
        this.setBounds(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2, windowSize.width, windowSize.height);
    }

    private void startNewGame(int width, int height) {
        _gameModel = new GameModel(width, height);
        _gameModel.addGameModelListener(new GameController());

        // Clear MainWindow
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
        // Field and keyboard
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(new GameFieldWidget(_gameModel, _gameModel.gameField()));
        centerPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        centerPanel.add(new PlayerActionsWidget(_gameModel));
        centerPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        centerPanel.add(new KeyboardWidget(_gameModel, _gameModel.alphabet()));
        content.add(centerPanel, constraints);

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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();
        this.setBounds(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2, windowSize.width, windowSize.height);
    }

    private void disableGameWidgets(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(false);

            if (!(component instanceof JMenuBar || component instanceof JMenu || component instanceof JMenuItem) && component instanceof Container) {
                disableGameWidgets((Container) component);
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
            int result = JOptionPane.showConfirmDialog(null, _gameSettings, "Настройки игры", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                MainWindow.this.startNewGame(_gameSettings.getWidthSpinnerValue(), _gameSettings.getHeightSpinnerValue());
            }
        }
    }

    private class ExitGameAction extends AbstractAction {
        public ExitGameAction() {
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
            MainWindow.this.disableGameWidgets(MainWindow.this);

            JOptionPane.showMessageDialog(null, new GameOverPanel(event.winners()), "Конец игры", JOptionPane.PLAIN_MESSAGE);
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