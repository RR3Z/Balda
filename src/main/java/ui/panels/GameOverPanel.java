package ui.panels;

import model.GameModel;
import model.Player;
import org.jetbrains.annotations.NotNull;
import ui.utils.GameWidgetUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameOverPanel extends JPanel {


    public GameOverPanel(@NotNull List<Player> winners) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        String message = "<html>" + "<div style='text-align: center;'>" + GameWidgetUtils.getHtmlFont(GameWidgetUtils.OPTION_PANE_FONT_SIZE);

        if(winners.size() == 1) {
            message += "Игра окончена.<br>Победитель: " + winners.get(winners.size() - 1).name();
        }

        else if (winners.size() > 1) {
            message += "Игра окончена.<br>Победители:";

            for(Player player: winners) {
                message += "<br>" + player.name();
            }
        }

        else {
            message += "Игра окончена.<br>Победитель не определен";
        }

        message += "</div></html>";

        JLabel text = new JLabel(message);
        this.add(text);
    }
}
