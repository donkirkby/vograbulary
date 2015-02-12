package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.russian.TargetDisplay;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class GwtTargetDisplay
extends TargetDisplay
implements MouseDownHandler, MouseMoveHandler, MouseUpHandler,
TouchStartHandler, TouchMoveHandler, TouchEndHandler {
    private Label label;
    private Image dragButton;
    private AbsolutePanel panel;
    private boolean isDragging;
    private int labelY;
    private int buttonY;
    private boolean isInitialized;
    
    public GwtTargetDisplay(Label label, Image dragButton, AbsolutePanel panel) {
        this.label = label;
        this.dragButton = dragButton;
        this.panel = panel;
        dragButton.addMouseDownHandler(this);
        dragButton.addMouseMoveHandler(this);
        dragButton.addMouseUpHandler(this);
        dragButton.addTouchStartHandler(this);
        dragButton.addTouchMoveHandler(this);
        dragButton.addTouchEndHandler(this);
    }
    
    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public int getX() {
        return panel.getWidgetLeft(label);
    }

    @Override
    public void setX(int x) {
        checkOffset();
        panel.setWidgetPosition(label, x, labelY);
        panel.setWidgetPosition(dragButton, x, buttonY);
    }
    
    private void checkOffset() {
        if ( ! isInitialized) {
            labelY = panel.getWidgetTop(label);
            buttonY = panel.getWidgetTop(dragButton);
            isInitialized = true;
        }
    }

    @Override
    public int getWidth() {
        return label.getOffsetWidth();
    }

    @Override
    public boolean isDragVisible() {
        return dragButton.isVisible();
    }

    @Override
    public void setDragVisible(boolean isDragVisible) {
        dragButton.setVisible(isDragVisible);
    }
    
    @Override
    public void onTouchStart(TouchStartEvent event) {
        event.preventDefault();
        onStart(event.getTouches().get(0).getClientX());
    }
    
    @Override
    public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
        onStart(event.getClientX());
    }

    private void onStart(int x) {
        Event.setCapture(dragButton.getElement());
        isDragging = true;
        dragStart(x - panel.getWidgetLeft(label));
    }
    
    @Override
    public void onMouseMove(MouseMoveEvent event) {
        onMove(event.getClientX());
    }
    
    @Override
    public void onTouchMove(TouchMoveEvent event) {
        onMove(event.getTouches().get(0).getClientX());
    }

    private void onMove(int x) {
        if (isDragging) {
            drag(x - panel.getWidgetLeft(label));
        }
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        onStop();
    }
    
    @Override
    public void onTouchEnd(TouchEndEvent event) {
        onStop();
    }

    private void onStop() {
        Event.releaseCapture(dragButton.getElement());
        isDragging = false;
    }
}
