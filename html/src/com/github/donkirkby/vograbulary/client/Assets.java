package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Assets extends ClientBundle {
    public static final Assets INSTANCE = GWT.create(Assets.class);
    
    @Source("com/github/donkirkby/vograbulary/assets/bacronyms.txt")
    TextResource bacronyms();

    @Source("com/github/donkirkby/vograbulary/assets/russianDolls.txt")
    TextResource russianDolls();

    @Source("com/github/donkirkby/vograbulary/assets/wordlist.txt")
    TextResource wordList();
}
