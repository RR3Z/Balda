package model.events;

import java.util.EventListener;

public interface WordListener extends EventListener {
    public void failedToAddLetter(WordEvent event);
}
