package model.events;

import model.Player;

import java.util.EventObject;

public class WordsDBEvent extends EventObject {
    public WordsDBEvent(Object source) {
        super(source);
    }

    private Player _player;

    public void setPlayer(Player player) {
        _player = player;
    }

    public Player player() {
        return _player;
    }

    private String _word;

    public void setWord(String word) {
        _word = word;
    }

    public String word() {
        return _word;
    }

    private boolean _isKnown;

    public void setIsKnown(boolean status) {
        _isKnown = status;
    }

    public boolean isKnown() {
        return _isKnown;
    }

    private boolean _isUsedAlready;

    public void setIsUsedAlready(boolean status) {
        _isUsedAlready = status;
    }

    public boolean isUsedAlready() {
        return _isUsedAlready;
    }
}
