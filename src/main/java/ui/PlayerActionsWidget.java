package ui;

import model.GameModel;
import model.Player;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import org.jetbrains.annotations.NotNull;
import ui.buttons.PlayerActionButton;
import ui.enums.BorderType;
import ui.enums.ColorType;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerActionsWidget extends JPanel {
    private GameModel _gameModel;

    private PlayerActionButton _cancelActionButton;
    private PlayerActionButton _skipTurnButton;
    private PlayerActionButton _submitWordButton;

    public PlayerActionsWidget(GameModel gameModel) {
        super();
        _gameModel = gameModel;

        for(Player player: _gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        fillWidget();
    }

    private void fillWidget() {
        _cancelActionButton = new PlayerActionButton("Отменить действие", GameWidgetUtils.сolor(ColorType.CANCEL_ACTION_BUTTON));
        _cancelActionButton.addMouseListener(new CancelActionButtonMouseListener(_cancelActionButton));
        _cancelActionButton.setEnabled(false);
        this.add(_cancelActionButton);

        _skipTurnButton = new PlayerActionButton("Пропустить ход", GameWidgetUtils.сolor(ColorType.SKIP_TURN_BUTTON));
        _skipTurnButton.addMouseListener(new SkipTurnButtonMouseListener(_skipTurnButton));
        _skipTurnButton.setEnabled(true);
        this.add(_skipTurnButton);

        _submitWordButton = new PlayerActionButton("Подтвердить слово", GameWidgetUtils.сolor(ColorType.SUBMIT_WORD_BUTTON));
        _submitWordButton.setEnabled(false);
        _submitWordButton.addMouseListener(new SubmitWordButtonMouseListener(_submitWordButton));
        this.add(_submitWordButton);
    }

    private class CancelActionButtonMouseListener extends MouseAdapter {
        private PlayerActionButton _button;

        public CancelActionButtonMouseListener(PlayerActionButton button) {
            _button = button;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(_button.isEnabled()) {
                // Logic
                _gameModel.activePlayer().cancelActionOnField();
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.сolor(ColorType.DEFAULT_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.DEFAULT))
                );
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(_button.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.сolor(ColorType.DEFAULT_HIGHLIGHTED_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.BOLD))
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.setBorder(BorderFactory.createLineBorder(
                    GameWidgetUtils.сolor(ColorType.DEFAULT_BORDER),
                    GameWidgetUtils.borderThickness(BorderType.DEFAULT))
            );
        }
    }

    private class SkipTurnButtonMouseListener extends MouseAdapter {
        private PlayerActionButton _button;

        public SkipTurnButtonMouseListener(PlayerActionButton button) {
            _button = button;
        }
        @Override
        public void mousePressed(MouseEvent e) {
            if(_button.isEnabled()) {
                // Logic
                _gameModel.activePlayer().skipTurn();
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.сolor(ColorType.DEFAULT_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.DEFAULT))
                );
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(_button.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.сolor(ColorType.HIGHLIGHTED_SKIP_TURN_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.BOLD))
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.setBorder(BorderFactory.createLineBorder(
                    GameWidgetUtils.сolor(ColorType.DEFAULT_BORDER),
                    GameWidgetUtils.borderThickness(BorderType.DEFAULT))
            );
        }
    }

    private class SubmitWordButtonMouseListener extends MouseAdapter {
        private PlayerActionButton _button;

        public SubmitWordButtonMouseListener(PlayerActionButton button) {
            _button = button;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(_button.isEnabled()) {
                // Logic
                _gameModel.activePlayer().submitWord();
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.сolor(ColorType.DEFAULT_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.DEFAULT))
                );
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(_button.isEnabled()) {
                _button.setBorder(BorderFactory.createLineBorder(
                        GameWidgetUtils.сolor(ColorType.DEFAULT_HIGHLIGHTED_BORDER),
                        GameWidgetUtils.borderThickness(BorderType.BOLD))
                );
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            _button.setBorder(BorderFactory.createLineBorder(
                    GameWidgetUtils.сolor(ColorType.DEFAULT_BORDER),
                    GameWidgetUtils.borderThickness(BorderType.DEFAULT))
            );
        }
    }

    private class PlayerController implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            if(event.player().state() == PlayerState.SELECTING_LETTER) {
                _cancelActionButton.setEnabled(false);
                _submitWordButton.setEnabled(false);
                _skipTurnButton.setEnabled(true);
            }

            if(event.player().state() == PlayerState.PLACES_LETTER) {
                _cancelActionButton.setEnabled(true);
                _submitWordButton.setEnabled(false);
                _skipTurnButton.setEnabled(true);
            }

            if(event.player().state() == PlayerState.FORMS_WORD) {
                _cancelActionButton.setEnabled(true);
                _submitWordButton.setEnabled(true);
                _skipTurnButton.setEnabled(true);
            }
        }

        @Override
        public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event) {
            // DON'T NEED IT HERE
        }

        @Override
        public void canceledActionOnField(@NotNull PlayerActionEvent event) {
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
        public void choseCell(@NotNull PlayerActionEvent event) {
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
    }
}
