package com.github.donkirkby.vograbulary.russian;

import static com.github.donkirkby.vograbulary.russian.LetterDisplayTest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TargetDisplayTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private class DummyTargetDisplay extends TargetDisplay {
        private int dragX;
        private boolean isDragVisible = true;
        
        public DummyTargetDisplay(
                String text,
                int x,
                LetterDisplayFactory factory) {
            super(x, factory);
            setText(text);
            this.dragX = x;
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
        public boolean isDragVisible() {
            return isDragVisible;
        }

        @Override
        public void setDragVisible(boolean isDragVisible) {
            this.isDragVisible = isDragVisible;
        }
    }

    private class DummyLetterDisplayFactory extends LetterDisplayFactory {
        @Override
        public LetterDisplay create(String letter, int x) {
            return new LetterDisplayTest.DummyLetterDisplay(letter, x);
        }
        
    }
    private TargetDisplay leftDisplay;
    private TargetDisplay rightDisplay;
    private List<LetterDisplay> leftLetters;
    private List<LetterDisplay> rightLetters;
    
    @Before
    public void setUp() {
        DummyLetterDisplayFactory factory = new DummyLetterDisplayFactory();
        leftDisplay = new DummyTargetDisplay("LEFT", 0, factory);
        rightDisplay = new DummyTargetDisplay("RIGHT", 100, factory);
        leftDisplay.setOther(rightDisplay);
        leftDisplay.setText("LEFT");
        rightDisplay.setText("RIGHT");
        leftLetters = leftDisplay.getLetters();
        rightLetters = rightDisplay.getLetters();
    }
    
    @Test
    public void leftLetterPositions() {
        assertThat("letter count", leftLetters.size(), is(4));
        assertThat("first letter", leftLetters.get(0).getLetter(), is("L"));
        assertThat("first x", leftLetters.get(0).getLeft(), is(0));
        assertThat("second letter", leftLetters.get(1).getLetter(), is("E"));
        assertThat("second x", leftLetters.get(1).getLeft(), is(LETTER_WIDTH));
    }
    
    @Test
    public void rightLetterPositions() {
        assertThat("first x", rightLetters.get(0).getLeft(), is(100));
    }
    
    @Test
    public void resetText() {
        leftDisplay.setText("SINISTER");
        
        assertThat("letter count", leftLetters.size(), is(8));
    }
    
    @Test
    public void lettersReadOnly() {
        thrown.expect(UnsupportedOperationException.class);
        leftLetters.clear();
    }
    
    @Test
    public void layout() {
        LetterDisplay letterDisplay = leftLetters.get(1);
        letterDisplay.setLeft(0);
        leftDisplay.layout();
        
        assertThat("left", letterDisplay.getLeft(), is(LETTER_WIDTH));
    }
    
    @Test
    public void drag() {
        int startX = 10;
        int dragX = 25;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("dragButton.x", leftDisplay.getDragX(), is(dragX - startX));
        assertThat(
                "first letter x",
                leftLetters.get(0).getLeft(),
                is(dragX - startX));
        assertThat(
                "second letter x",
                leftLetters.get(1).getLeft(),
                is(dragX - startX + LETTER_WIDTH));
        assertThat(
                "right word letter x",
                rightLetters.get(0).getLeft(),
                is(100));
    }
    
    @Test
    public void dragRight() {
        int startX = 100;
        int dragX = 75;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "left word letter x",
                leftLetters.get(0).getLeft(),
                is(0));
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
        
        assertThat(
                "target.x",
                leftLetters.get(0).getLeft(),
                is(dragX1 - startX + dragX2 - startX));
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
        int dragX = 104-20; // starts to push label, but doesn't split yet
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("target position", rightLetters.get(0).getLeft(), is(104));
    }
    
    @Test
    public void pushLeft() {
        int startX = 100;
        // starts to push label, but doesn't split yet
        int dragX = -4+20;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "target position",
                leftLetters.get(3).getLeft(),
                is(dragX - LETTER_WIDTH));
    }
    
    @Test
    public void splitRight() {
        int startX = 0;
        int dragX = 105-20; // split first letter
        int expectedRightTailX = 105; // start of right word
        int expectedRightHeadX = dragX - LETTER_WIDTH;
        int expectedDragX = dragX; // drag button position
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat(
                "right tail position",
                rightLetters.get(1).getLeft(),
                is(expectedRightTailX));
        assertThat(
                "right head position",
                rightLetters.get(0).getLeft(),
                is(expectedRightHeadX));
        assertThat("drag position", leftDisplay.getDragX(), is(expectedDragX));
    }

    @Test
    public void splitLeft() {
        int startX = 100;
        int dragX = 15; // split first letter
        int expectedDragX = dragX; // drag button position
        int expectedLeftTailX = dragX;
        int expectedLeftHeadX = dragX + 25;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "left tail position",
                leftLetters.get(2).getRight(),
                is(expectedLeftTailX));
        assertThat(
                "left head position",
                leftLetters.get(3).getLeft(),
                is(expectedLeftHeadX));
        assertThat("drag position", rightDisplay.getDragX(), is(expectedDragX));
    }
    
    @Test
    public void splitRightInTwoSteps() {
        int startX = 0;
        int dragX1 = 104-20; // push
        int dragX2 = 1; // split first letter
        int expectedRightTailX = 105; // start of right word
        int expectedRightHeadX = dragX1 + dragX2 - LETTER_WIDTH;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX1);
        leftDisplay.drag(dragX2);
        
        assertThat(
                "right tail position",
                rightLetters.get(1).getLeft(),
                is(expectedRightTailX));
        assertThat(
                "right head position",
                rightLetters.get(0).getLeft(),
                is(expectedRightHeadX));
    }

    @Test
    public void splitLeftInTwoSteps() {
        int startX = 100;
        int dragX1 = 16; // push
        int dragX2 = 99; // split first letter
        int expectedLeftTailX = 15;
        int expectedLeftHeadX = 15 + 25;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX1);
        rightDisplay.drag(dragX2);
        
        assertThat(
                "left tail position",
                leftLetters.get(2).getRight(),
                is(expectedLeftTailX));
        assertThat(
                "left head position",
                leftLetters.get(3).getLeft(),
                is(expectedLeftHeadX));
    }
    
    @Test
    public void getText() {
        String text = rightDisplay.getText();
        
        assertThat("text", text, is("RIGHT"));
    }
}
