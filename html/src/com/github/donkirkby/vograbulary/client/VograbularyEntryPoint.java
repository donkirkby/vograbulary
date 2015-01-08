package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VograbularyEntryPoint implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RussianDollsComposite russianDolls = new RussianDollsComposite();
        RootPanel root = RootPanel.get();
        root.clear();
        root.add(russianDolls);
    }
}
