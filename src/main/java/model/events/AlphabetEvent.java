package model.events;

import model.Alphabet;

import java.util.EventObject;

public class AlphabetEvent extends EventObject {
    public AlphabetEvent(Object source) {
        super(source);
    }

    private Alphabet _alphabet;
    public void setAlphabet(Alphabet alphabet) {
        _alphabet = alphabet;
    }
    public Alphabet alphabet() {
        return _alphabet;
    }

    private Character _letter;
    public void setLetter(Character letter) {
        _letter = letter;
    }
    public Character letter() {
        return _letter;
    }
}
