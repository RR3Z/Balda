package model.events;

import model.GameModel;

import java.util.EventListener;

public interface GameModelListener extends EventListener {
    public void gameIsFinished(GameModelEvent event);

    public void playerExchanged(GameModelEvent event);

    public void definedStartWord(GameModelEvent event);
}
