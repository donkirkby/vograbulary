package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.LetterDisplay;
import com.github.donkirkby.vograbulary.LetterDisplayFactory;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;

public class GwtLetterDisplayFactory extends LetterDisplayFactory{
    private AbsolutePanel panel;
    
    public GwtLetterDisplayFactory(AbsolutePanel panel) {
        this.panel = panel;
    }
    
    @Override
    public LetterDisplay create(String letter) {
        Button label = new Button(letter);
        panel.add(label);
        return new GwtLetterDisplay(panel, label);
    }

    @Override
    public void destroy(LetterDisplay letter) {
        panel.remove(((GwtLetterDisplay)letter).getButton());
    }
}
