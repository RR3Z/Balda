package model.events;

import model.Cell;
import model.Player;

import java.util.EventObject;
import java.util.List;

public class GameModelEvent extends EventObject {
    public GameModelEvent(Object source) {
        super(source);
    }

    private Player _player;

    public void setPlayer(Player player) {
        _player = player;
    }

    public Player player() {
        return _player;
    }

    private List<Player> _winners;

    public void setWinners(List<Player> winners) {
        _winners = winners;
    }

    public List<Player> winners() {
        return _winners;
    }

    private String _word;

    public void setWord(String word) {
        _word = word;
    }

    public String word() {
        return _word;
    }

    private Cell _cell;

    public void setCell(Cell cell) {
        _cell = cell;
    }

    public Cell cell() {
        return _cell;
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

    private boolean _isNotNeighborOfLastCell;
}
