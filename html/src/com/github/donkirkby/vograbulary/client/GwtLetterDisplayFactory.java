package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.russian.LetterDisplay;
import com.github.donkirkby.vograbulary.russian.LetterDisplayFactory;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;

public class GwtLetterDisplayFactory extends LetterDisplayFactory{
    private AbsolutePanel panel;
    
    public GwtLetterDisplayFactory(AbsolutePanel panel) {
        this.panel = panel;
    }
    
    @Override
    public LetterDisplay create(String letter) {
        Label label = new Label(letter);
        panel.add(label);
        return new GwtLetterDisplay(panel, label);
    }
}
