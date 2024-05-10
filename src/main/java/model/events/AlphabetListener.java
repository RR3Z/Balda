package model.events;

import java.util.EventListener;

public interface AlphabetListener extends EventListener {
    public void forgetSelectedLetter(AlphabetEvent event);
}
