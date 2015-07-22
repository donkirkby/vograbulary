package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.LetterDisplay;
import com.github.donkirkby.vograbulary.LetterDisplayFactory;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;

public class GwtLetterDisplayFactory extends LetterDisplayFactory{
    private AbsolutePanel panel;
    
    public GwtLetterDisplayFactory(AbsolutePanel panel) {
        this.panel = panel;
    }
    
    @Override
    public LetterDisplay create(String letter) {
        Button button = new Button(letter);
        panel.add(button);
        final GwtLetterDisplay letterDisplay =
                new GwtLetterDisplay(panel, button);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                letterDisplay.click();
            }
        });
        return letterDisplay;
    }

    @Override
    public void destroy(LetterDisplay letter) {
        panel.remove(((GwtLetterDisplay)letter).getButton());
    }
}
