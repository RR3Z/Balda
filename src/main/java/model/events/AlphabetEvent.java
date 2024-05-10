package model.events;

import model.Player;

import java.util.EventObject;

public class AlphabetEvent extends EventObject {
    public AlphabetEvent(Object source) {
        super(source);
    }

    private Character _letter;
    public void setLetter(Character letter) {
        _letter = letter;
    }
    public Character letter() {
        return _letter;
    }
}
