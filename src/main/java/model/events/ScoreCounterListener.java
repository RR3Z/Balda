package model.events;

import java.util.EventListener;

public interface ScoreCounterListener extends EventListener {
    public void scoreChanged(ScoreCounterEvent event);
}
