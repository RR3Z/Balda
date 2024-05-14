package model.events;

import java.util.EventListener;

public interface AlphabetListener extends EventListener {
    public void forgotSelectedLetter(AlphabetEvent event);
    public void selectedLetter(AlphabetEvent event);
}
