package ui.widgets;

import model.GameModel;
import model.enums.PlayerState;
import model.events.PlayerActionEvent;
import model.events.PlayerActionListener;
import model.players.AIPlayer;
import model.players.AbstractPlayer;
import model.players.UserPlayer;
import org.jetbrains.annotations.NotNull;
import ui.buttons.CancelActionButton;
import ui.buttons.SkipTurnButton;
import ui.buttons.SubmitWordButton;
import javax.swing.*;

public class PlayerActionsWidget extends JPanel {
    private CancelActionButton _cancelActionButton;
    private SkipTurnButton _skipTurnButton;
    private SubmitWordButton _submitWordButton;

    public PlayerActionsWidget(@NotNull GameModel gameModel) {
        super();

        for(AbstractPlayer player: gameModel.players()) {
            player.addPlayerActionListener(new PlayerController());
        }

        fillWidget(gameModel);
    }

    private void fillWidget(GameModel gameModel) {
        _cancelActionButton = new CancelActionButton(gameModel, "Отменить действие");
        _cancelActionButton.setEnabled(false);
        this.add(_cancelActionButton);

        _skipTurnButton = new SkipTurnButton(gameModel, "Пропустить ход");
        _skipTurnButton.setEnabled(true);
        this.add(_skipTurnButton);

        _submitWordButton = new SubmitWordButton(gameModel, "Подтвердить слово");
        _submitWordButton.setEnabled(false);
        this.add(_submitWordButton);
    }

    private class PlayerController implements PlayerActionListener {
        @Override
        public void changedState(@NotNull PlayerActionEvent event) {
            if(event.player() instanceof UserPlayer) {
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

            if(event.player() instanceof AIPlayer) {
                _cancelActionButton.setEnabled(false);
                _submitWordButton.setEnabled(false);
                _skipTurnButton.setEnabled(false);
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
