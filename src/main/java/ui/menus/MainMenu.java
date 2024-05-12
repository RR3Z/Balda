package ui.menus;

import ui.utils.GameWidgetUtils;

import javax.swing.*;

public class MainMenu extends JMenu {
    private final int FONT_SIZE = 14;

    private JMenuItem _startNewGameItem;
    private JMenuItem _exitGameItem;

    public MainMenu() {
        super("Главное меню");

        _startNewGameItem = new JMenuItem();
        _exitGameItem = new JMenuItem();

        this.add(_startNewGameItem);
        this.add(_exitGameItem);

        this.setFont(GameWidgetUtils.getFont(FONT_SIZE));
        _startNewGameItem.setFont(GameWidgetUtils.getFont(FONT_SIZE));
        _exitGameItem.setFont(GameWidgetUtils.getFont(FONT_SIZE));
    }

    public void setStartNewGameAction(AbstractAction action) {
        _startNewGameItem.setAction(action);
    }

    public void setExitGameAction(AbstractAction action) {
        _exitGameItem.setAction(action);
    }
}
