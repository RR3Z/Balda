package model.events;

import java.util.EventListener;

public interface GameModelListener extends EventListener {
    public void gameIsFinished(GameModelEvent event);

    public void playerExchanged(GameModelEvent event);

    public void definedStartWord(GameModelEvent event);

    public void playerSkippedTurn(GameModelEvent event);

    public void playerChoseCell(GameModelEvent event);

    public void playerFailedToAddNewWordToDictionary(GameModelEvent event);

    public void playerAddedNewWordToDictionary(GameModelEvent event);

    public void playerChoseWrongCell(GameModelEvent event);

    public void playerPlacedLetter(GameModelEvent event);

    public void playerSubmittedWordWithoutChangeableCell(GameModelEvent event);

    public void playerCanceledActionOnField(GameModelEvent event);

    public void playerSubmittedWord(GameModelEvent event);

    public void playerFailedToSubmitWord(GameModelEvent event);
}
