package com.github.donkirkby.vograbulary.client;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.github.donkirkby.vograbulary.LetterDisplay;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;

public class GwtLetterDisplay extends LetterDisplay {
    private static final int TOP_MARGIN = 4;
    private static final float DEFAULT_TEXT_SIZE = 14;
    private AbsolutePanel panel;
    private Button button;
    private float textSize;
    
    public GwtLetterDisplay(AbsolutePanel panel, Button button) {
        this.panel = panel;
        this.button = button;
        setTextSize(DEFAULT_TEXT_SIZE);
        setLeft(0);
        setTop(TOP_MARGIN);
    }

    @Override
    public int getTop() {
        return panel.getWidgetTop(button);
    }
    
    @Override
    public void setTop(int top) {
        panel.setWidgetPosition(button, getLeft(), top);
    }

    @Override
    public int getLeft() {
        return panel.getWidgetLeft(button);
    }

    @Override
    public void setLeft(int left) {
        panel.setWidgetPosition(button, left, getTop());
    }
    
    @Override
    public void animateTo(int left, int top) {
        throw new NotImplementedException();
    }

    @Override
    public int getHeight() {
        return button.getOffsetHeight();
    }
    @Override
    public int getWidth() {
        return button.getOffsetWidth();
    }
    
    @Override
    public float getTextSize() {
        return textSize;
    }
    @Override
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        button.getElement().getStyle().setFontSize(textSize, Unit.PX);
    }

    @Override
    public String getLetter() {
        return button.getText();
    }
    
    public Button getButton() {
        return button;
    }
}
