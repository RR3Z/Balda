package model.events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface PlayerActionListener extends EventListener {
    public void changedState(@NotNull PlayerActionEvent event);

    public void skippedTurn(@NotNull PlayerActionEvent event);

    public void finishedTurn(@NotNull PlayerActionEvent event);

    public void placedLetter(@NotNull PlayerActionEvent event);

    public void choseLetter(@NotNull PlayerActionEvent event);
    // -------- НИЖНИЕ НЕ НУЖНЫ ---------
    public void addedNewWordToDictionary(@NotNull PlayerActionEvent event);

    public void choseCell(@NotNull PlayerActionEvent event);

    public void submittedWordWithoutChangeableCell(@NotNull PlayerActionEvent event);

    public void submittedWord(@NotNull PlayerActionEvent event);

    public void failedToSubmitWord(@NotNull PlayerActionEvent event);

    public void canceledActionOnField(@NotNull PlayerActionEvent event);
}
