package model.events;

import java.util.EventListener;

public interface WordsDBListener extends EventListener {
    public void addedUsedWord(WordsDBEvent event);
    public void failedToAddUsedWord(WordsDBEvent event);
    public void addedNewWordToDictionary(WordsDBEvent event);
}
