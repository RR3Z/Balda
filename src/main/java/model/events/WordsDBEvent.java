package model.events;

import model.players.AbstractPlayer;

import java.util.EventObject;

public class WordsDBEvent extends EventObject {
    public WordsDBEvent(Object source) {
        super(source);
    }

    private AbstractPlayer _player;

    public void setPlayer(AbstractPlayer player) {
        _player = player;
    }

    public AbstractPlayer player() {
        return _player;
    }

    private String _word;

    public void setWord(String word) {
        _word = word;
    }

    public String word() {
        return _word;
    }
}
