package model.events;

import java.util.EventListener;

public interface WordsDBListener extends EventListener {
    public void addedToUsedWords(WordsDBEvent event);
    public void addedNewWordToDictionary(WordsDBEvent event);
    public void wordAlreadyUsed(WordsDBEvent event);
    public void wordNotAllowed(WordsDBEvent event);
}
