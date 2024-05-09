import model.GameModel;
import ui.CellWidget;
import ui.GameFieldWidget;

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

        // TODO: REMOVE IT
        startNewGame(5,5);
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
        //_gameModel.addGameModelListener();
        _gameModel.startGame();

        // Очистить содержимое панели
        JPanel content = (JPanel) this.getContentPane();
        content.setLayout(new FlowLayout());
        content.removeAll();

        // Добавить игровое поле
        content.add(new GameFieldWidget(_gameModel.gameField()));
        this.pack();

        // Расположить окно посередине экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = this.getSize();
        this.setBounds(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2, windowSize.width, windowSize.height);
    }

    private class StartNewGameAction extends AbstractAction {
        /*
        Не хочу, чтобы gameSettingsPanel создавалась внутри actionPerformed, но доступ к значениям спиннеров
        надо получить внутри actionPerformed после вызова диалогового меню.

        Решение: вынести спиннеры в качестве свойства.
        */
        private JSpinner _widthSpinner;
        private JSpinner _heightSpinner;

        public StartNewGameAction() {
            putValue(NAME, "Новая игра");
        }

        private JPanel createGameSettingsPanel() {
            JPanel gameSettingsPanel = new JPanel();

            // --------- Панель с размерами поля ---------
            JPanel fieldSizesPanel = new JPanel();

            JLabel fieldSizesLabel = new JLabel("Выберите размеры поля:");

            int MIN_VALUE = 2;
            int MAX_VALUE = 15; // TODO: наверное стоит выделить тоже в отдельный файл эти значения (мин и макс)
            int STEP = 1;
            _widthSpinner = new JSpinner(new SpinnerNumberModel(MIN_VALUE, MIN_VALUE, MAX_VALUE, STEP));
            _heightSpinner = new JSpinner(new SpinnerNumberModel(MIN_VALUE, MIN_VALUE, MAX_VALUE, STEP));

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

    private static class ExitAction extends AbstractAction {
        public ExitAction() {
            putValue(NAME, "Выход");
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