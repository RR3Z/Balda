package ui;

import model.GameModel;
import ui.buttons.CancelActionButton;
import ui.buttons.SkipTurnButton;
import ui.buttons.SubmitWordButton;
import ui.utils.WidgetsViewCustomizations;

import javax.swing.*;
import java.awt.*;

public class PlayerActionsWidget extends JPanel {
    private CancelActionButton _cancelActionButton;
    private SkipTurnButton _skipTurnButton;
    private SubmitWordButton _submitWordButton;

    public PlayerActionsWidget(GameModel gameModel) {
        super();

        fillWidget(gameModel);
    }

    private void fillWidget(GameModel gameModel) {
        _cancelActionButton = new CancelActionButton(gameModel);
        this.add(_cancelActionButton);
        _skipTurnButton = new SkipTurnButton(gameModel);
        this.add(_skipTurnButton);
        _submitWordButton = new SubmitWordButton(gameModel);
        this.add(_submitWordButton);
    }
}
