package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TargetDisplayTest {
    private static final int LETTER_WIDTH = 5;
    private class DummyTargetDisplay extends TargetDisplay {
        private String text;
        private int x;
        private int dragX;
        private boolean isDragVisible = true;
        
        public DummyTargetDisplay(String text, int x) {
            this.text = text;
            this.x = x;
            this.dragX = x;
        }
        
        @Override
        public String getText() {
            return text;
        }
        
        @Override
        public void setText(String text) {
            this.text = text;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public void setX(int x) {
            this.x = x;
        }
        
        @Override
        public int getDragX() {
            return dragX;
        }
        
        @Override
        public void setDragX(int dragX) {
            this.dragX = dragX;
        }
        
        @Override
        public int getWidth() {
            return text.length()*LETTER_WIDTH;
        }

        @Override
        public boolean isDragVisible() {
            return isDragVisible;
        }

        @Override
        public void setDragVisible(boolean isDragVisible) {
            this.isDragVisible = isDragVisible;
        }
    }
    private TargetDisplay leftDisplay;
    private TargetDisplay rightDisplay;
    
    @Before
    public void setUp() {
        leftDisplay = new DummyTargetDisplay("LEFT", 0);
        rightDisplay = new DummyTargetDisplay("RIGHT", 100);
        leftDisplay.setOther(rightDisplay);
    }
    
    @Test
    public void drag() {
        int startX = 10;
        int dragX = 25;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("target.x", leftDisplay.getX(), is(dragX - startX));
        assertThat("dragButton.x", leftDisplay.getDragX(), is(dragX - startX));
    }
    
    @Test
    public void dragTwice() {
        // The drag position is always the pointer position relative to the
        // text field.
        int startX = 10;
        int dragX1 = 25;
        int dragX2 = 23;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX1);
        leftDisplay.drag(dragX2);
        
        assertThat("target.x", leftDisplay.getX(), is(dragX1 - startX + dragX2 - startX));
    }
    
    @Test
    public void hideRight() {
        int startX = 10;
        int dragX = 65;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat(rightDisplay.isDragVisible(), is(false));
    }
    
    @Test
    public void showRight() {
        int startX = 10;
        int dragX = 35;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat(rightDisplay.isDragVisible(), is(true));
    }
    
    @Test
    public void hideLeft() {
        int startX = 110;
        int dragX = 35;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(leftDisplay.isDragVisible(), is(false));
    }
    
    @Test
    public void hideThenShow() {
        int startX = 0;
        int drag1X = 70;
        int drag2X = 45;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(drag1X);
        assertThat(rightDisplay.isDragVisible(), is(false));

        leftDisplay.dragStart(drag1X);
        leftDisplay.drag(drag2X);
        assertThat(rightDisplay.isDragVisible(), is(true));
    }
    
    @Test
    public void pushRight() {
        int startX = 0;
        int dragX = 104-leftDisplay.getWidth(); // starts to push label, but doesn't split yet
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("target position", rightDisplay.getX(), is(104));
    }
    
    @Test
    public void pushLeft() {
        int startX = 100;
        int dragX = -4+leftDisplay.getWidth(); // starts to push label, but doesn't split yet
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat("target position", leftDisplay.getX(), is(-4));
    }
    
    @Test
    public void splitRight() {
        int startX = 0;
        int dragX = 105-leftDisplay.getWidth(); // split first letter
        int expectedRightX = 105; // start of right word
        int expectedDragX = dragX; // drag button position
        int expectedLeftX = dragX - LETTER_WIDTH; // added one letter to start, so move left
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("right position", rightDisplay.getX(), is(expectedRightX));
        assertThat("drag position", leftDisplay.getDragX(), is(expectedDragX));
        assertThat("left position", leftDisplay.getX(), is(expectedLeftX));
        assertThat("left text", leftDisplay.getText().toString(), is("RLEFT"));
        assertThat("right text", rightDisplay.getText().toString(), is("IGHT"));
    }
    
    @Test
    public void splitMoreRight() {
        int startX = 0;
        int dragX = 110-leftDisplay.getWidth(); // split first two letters
        int expectedRightX = 110; // start of right word
        int expectedDragX = dragX; // drag button position
        int expectedLeftX = dragX - 2*LETTER_WIDTH; // added two letters to start, so move left
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("right position", rightDisplay.getX(), is(expectedRightX));
        assertThat("drag position", leftDisplay.getDragX(), is(expectedDragX));
        assertThat("left position", leftDisplay.getX(), is(expectedLeftX));
        assertThat("left text", leftDisplay.getText().toString(), is("RILEFT"));
        assertThat("right text", rightDisplay.getText().toString(), is("GHT"));
    }
    
    @Test
    public void splitMoreRightInTwoSteps() {
        int startX = 0;
        int dragX1 = 110-leftDisplay.getWidth(); // split first letter
        int dragX2 = 112-leftDisplay.getWidth(); // split first two letters
        int expectedRightX = 112; // start of right word
        int expectedDragX = dragX2; // drag button position
        int expectedLeftX = dragX2 - 2*LETTER_WIDTH; // added two letters to start, so move left
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX1);
        leftDisplay.drag(dragX2 - dragX1);
        
        assertThat("right position", rightDisplay.getX(), is(expectedRightX));
        assertThat("drag position", leftDisplay.getDragX(), is(expectedDragX));
        assertThat("left position", leftDisplay.getX(), is(expectedLeftX));
        assertThat("left text", leftDisplay.getText().toString(), is("RILEFT"));
        assertThat("right text", rightDisplay.getText().toString(), is("GHT"));
    }
    
    @Test
    public void splitLeft() {
        int startX = 100;
        int dragX = -5+leftDisplay.getWidth(); // split first letter
        int expectedLeftX = -5; // start of left word
        int expectedDragX = dragX; // drag button position
        int expectedRightX = dragX;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat("left position", leftDisplay.getX(), is(expectedLeftX));
        assertThat("drag position", rightDisplay.getDragX(), is(expectedDragX));
        assertThat("right position", rightDisplay.getX(), is(expectedRightX));
        assertThat("left text", leftDisplay.getText().toString(), is("LEF"));
        assertThat("right text", rightDisplay.getText().toString(), is("RIGHTT"));
    }
    
    @Test
    public void splitBeyondRight() {
        int startX = 0;
        int dragX = 130-leftDisplay.getWidth();
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("target position", rightDisplay.getX(), is(130));
        assertThat("left text", leftDisplay.getText().toString(), is("RIGHLEFT"));
        assertThat("right text", rightDisplay.getText().toString(), is("T"));
    }
}
