package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface Assets extends ClientBundle {
    public static final Assets INSTANCE = GWT.create(Assets.class);
    
    @Source("com/github/donkirkby/vograbulary/assets/bacronyms.txt")
    TextResource bacronyms();

    @Source("com/github/donkirkby/vograbulary/assets/russianDolls.txt")
    TextResource russianDolls();

    @Source("com/github/donkirkby/vograbulary/assets/wordlist.txt")
    TextResource wordList();
    
    @Source("com/github/donkirkby/vograbulary/images/drag.png")
    ImageResource drag();
    
    @Source("com/github/donkirkby/vograbulary/images/insert.png")
    ImageResource insert();
}
