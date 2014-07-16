package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TargetDisplayTest {
    private Label leftTarget;
    private Label rightTarget;
    private TextButton leftDrag;
    private TextButton rightDrag;
    private TargetDisplay leftDisplay;
    private TargetDisplay rightDisplay;
    
    @Before
    public void setUp() {
        leftTarget = new DummyLabel("LEFT", 0);
        rightTarget = new DummyLabel("RIGHT", 100);
        leftDrag = mock(TextButton.class);
        rightDrag = mock(TextButton.class);
        leftDisplay = new TargetDisplay(leftTarget, leftDrag);
        rightDisplay = new TargetDisplay(rightTarget, rightDrag);
        leftDisplay.setOther(rightDisplay);
    }
    
    @Test
    public void drag() {
        float startX = 10;
        float dragX = 25;
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        leftDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        leftDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        verify(leftDrag).translate(dragX - startX, 0);
        assertThat("target.x", leftTarget.getX(), is(dragX - startX));
    }
    
    @Test
    public void hideRight() {
        float startX = 10;
        float dragX = 65;
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        leftDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        leftDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        verify(rightDrag).setVisible(false);
    }
    
    @Test
    public void showRight() {
        float startX = 10;
        float dragX = 35;
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        leftDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        leftDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        verify(rightDrag).setVisible(true);
    }
    
    @Test
    public void hideLeft() {
        float startX = 110;
        float dragX = 35;
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        rightDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        rightDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        verify(leftDrag).setVisible(false);
    }
    
    @Test
    public void hideThenShow() {
        float startX = 0;
        float drag1X = 70;
        float drag2X = 55;
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        leftDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        leftDisplay.drag(event, drag1X, ignoredY, ignoredPointer);
        leftDisplay.dragStart(event, drag1X, ignoredY, ignoredPointer);
        leftDisplay.drag(event, drag2X, ignoredY, ignoredPointer);
        
        verify(rightDrag, times(2)).setVisible(false);
    }
    
    @Test
    public void pushRight() {
        float startX = 0;
        float dragX = 104-leftTarget.getWidth(); // starts to push label, but doesn't split yet
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        leftDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        leftDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        assertThat("target position", rightTarget.getX(), is(104f));
    }
    
    @Test
    public void pushLeft() {
        float startX = 100;
        float dragX = -4+leftTarget.getWidth(); // starts to push label, but doesn't split yet
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        rightDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        rightDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        assertThat("target position", leftTarget.getX(), is(-4f));
    }
    
    @Test
    public void splitRight() {
        float startX = 0;
        float dragX = 105-leftTarget.getWidth(); // split first letter
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        leftDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        leftDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        assertThat("target position", rightTarget.getX(), is(105f));
        assertThat("left text", leftTarget.getText().toString(), is("RLEFT"));
        assertThat("right text", rightTarget.getText().toString(), is("IGHT"));
    }
    
    @Test
    public void splitLeft() {
        float startX = 100;
        float dragX = -5+leftTarget.getWidth(); // split first letter
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        rightDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        rightDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        assertThat("target position", leftTarget.getX(), is(-5f));
        assertThat("left text", leftTarget.getText().toString(), is("LEF"));
        assertThat("right text", rightTarget.getText().toString(), is("RIGHTT"));
    }
    
    @Test
    public void splitBeyondRight() {
        float startX = 0;
        float dragX = 130-leftTarget.getWidth(); // split first letter
        InputEvent event = new InputEvent();
        float ignoredY = 0;
        int ignoredPointer = 0;
        
        leftDisplay.dragStart(event, startX, ignoredY, ignoredPointer);
        leftDisplay.drag(event, dragX, ignoredY, ignoredPointer);
        
        assertThat("target position", rightTarget.getX(), is(130f));
        assertThat("left text", leftTarget.getText().toString(), is("RIGHLEFT"));
        assertThat("right text", rightTarget.getText().toString(), is("T"));
    }
    
    private class DummyLabel extends Label {
        public DummyLabel(String text, float x) {
            super(text, (LabelStyle)null);
            setX(x);
        }
        
        @Override
        public void setStyle(LabelStyle style) {
        }
        
        @Override
        public float getPrefWidth() {
            return getText().length()*5;
        }
        
        @Override
        public float getPrefHeight() {
            return 20;
        }
    }
}
