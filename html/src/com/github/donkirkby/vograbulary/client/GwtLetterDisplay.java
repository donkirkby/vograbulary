package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.russian.LetterDisplay;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;

public class GwtLetterDisplay extends LetterDisplay {
    private static final int TOP_MARGIN = 4;
    private AbsolutePanel panel;
    private Label label;
    
    public GwtLetterDisplay(AbsolutePanel panel, Label label) {
        this.panel = panel;
        this.label = label;
        setLeft(0);
    }

    @Override
    public int getLeft() {
        return panel.getWidgetLeft(label);
    }

    @Override
    public void setLeft(int left) {
        panel.setWidgetPosition(label, left, TOP_MARGIN);
    }

    @Override
    public int getWidth() {
        return label.getOffsetWidth();
    }

    @Override
    public String getLetter() {
        return label.getText();
    }
}
