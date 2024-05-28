package model.events;

import model.Cell;
import model.players.AbstractPlayer;

import java.util.EventObject;
import java.util.List;

public class GameModelEvent extends EventObject {
    public GameModelEvent(Object source) {
        super(source);
    }

    private AbstractPlayer _player;

    public void setPlayer(AbstractPlayer player) {
        _player = player;
    }

    public AbstractPlayer player() {
        return _player;
    }

    private List<AbstractPlayer> _winners;

    public void setWinners(List<AbstractPlayer> winners) {
        _winners = winners;
    }

    public List<AbstractPlayer> winners() {
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

}
