package ui.menus;

import ui.MainWindow;
import ui.panels.GameSettingsPanel;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainMenu extends JMenu {
    private final int FONT_SIZE = 14;

    private MainWindow _parent;

    private JMenuItem _startNewGameItem;
    private JMenuItem _exitGameItem;

    public MainMenu(MainWindow parent) {
        super("Главное меню");
        _parent = parent;

        _startNewGameItem = new JMenuItem();
        _exitGameItem = new JMenuItem();

        _startNewGameItem.setAction(new StartNewGameAction());
        _exitGameItem.setAction(new ExitGameAction());

        this.add(_startNewGameItem);
        this.add(_exitGameItem);

        this.setFont(GameWidgetUtils.font(FONT_SIZE));
        _startNewGameItem.setFont(GameWidgetUtils.font(FONT_SIZE));
        _exitGameItem.setFont(GameWidgetUtils.font(FONT_SIZE));
    }

    private class StartNewGameAction extends AbstractAction {
        GameSettingsPanel _gameSettings;

        public StartNewGameAction() {
            this.putValue(NAME, "Новая игра");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            _gameSettings = new GameSettingsPanel();

            int result = JOptionPane.showConfirmDialog(null, _gameSettings, "Настройки игры", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                _parent.startNewGame(_gameSettings.widthSpinnerValue(), _gameSettings.heightSpinnerValue(), _gameSettings.isAIPlayer());
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
}
