package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.LetterDisplay;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;

public class GwtLetterDisplay extends LetterDisplay {
    private static final int TOP_MARGIN = 4;
    private AbsolutePanel panel;
    private Button button;
    
    public GwtLetterDisplay(AbsolutePanel panel, Button button) {
        this.panel = panel;
        this.button = button;
        setLeft(0);
    }

    @Override
    public int getLeft() {
        return panel.getWidgetLeft(button);
    }
    
    public int getTop() {
        return panel.getWidgetTop(button);
    }

    @Override
    public void setLeft(int left) {
        panel.setWidgetPosition(button, left, TOP_MARGIN);
    }

    @Override
    public int getWidth() {
        return button.getOffsetWidth();
    }

    @Override
    public String getLetter() {
        return button.getText();
    }
    
    public Button getButton() {
        return button;
    }
}
