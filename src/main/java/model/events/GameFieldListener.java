package model.events;

import java.util.EventListener;

public interface GameFieldListener extends EventListener {
    public void forgetChangedCell(GameFieldEvent event);

    public void placedWord(GameFieldEvent event);
}
