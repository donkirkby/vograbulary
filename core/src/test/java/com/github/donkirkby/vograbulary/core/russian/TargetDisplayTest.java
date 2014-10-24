package com.github.donkirkby.vograbulary.core.russian;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import playn.core.Events.Flags;
import playn.core.Pointer.Event;

public class TargetDisplayTest {
    private StubbedTargetDisplay left;
    private StubbedTargetDisplay right;
    private Puzzle puzzle;

    @Before
    public void setUp() {
        left = new StubbedTargetDisplay();
        right = new StubbedTargetDisplay();
        right.setOpposite(left);
        puzzle = new Puzzle("left right");
        left.setPuzzle(puzzle);
        right.setPuzzle(puzzle);
    }
    
    @Test
    public void withoutOverlap() {
        dragFromTo(right, 110, 45);
        
        assertThat("visible", left.isVisible(), is(true));
    }
    
    @Test
    public void withOverlap() {
        float txDragFrom = 110;
        float txDragTo = 40;
        float txExpected = txDragTo - 4*StubbedTargetDisplay.LETTER_WIDTH;

        dragFromTo(right, txDragFrom, txDragTo);

        float txAfter = right.getTx();
        
        assertThat("visible", left.isVisible(), is(false));
        assertThat("text", right.getText(), is("LEFTRIGHT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void passOneLetter() {
        float txDragFrom = 100;
        float txDragTo = 30;
        float txExpected = txDragTo - 3*StubbedTargetDisplay.LETTER_WIDTH;

        dragFromTo(right, txDragFrom, txDragTo);
        float txAfter = right.getTx();
        
        assertThat("text", right.getText(), is("LEFRIGHTT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void passOneLetterAndPause() {
        float txDragFrom = 100;
        float txDragTo = 30;
        float txExpected = txDragTo - 3*StubbedTargetDisplay.LETTER_WIDTH;
        
        dragFromTo(right, txDragFrom, txDragTo - 1);
        dragTo(right, txDragTo); // should be stable
        float txAfter = right.getTx();
        
        assertThat("text", right.getText(), is("LEFRIGHTT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void passTwoLettersThenBackOne() {
        float txDragStart = 100;
        float txDragMiddle = 20;
        float txDragEnd = 30;
        float txExpected = txDragEnd - 3*StubbedTargetDisplay.LETTER_WIDTH;
        
        dragFromTo(right, txDragStart, txDragMiddle);
        dragTo(right, txDragEnd);
        float txAfter = right.getTx();
        
        assertThat("text", right.getText(), is("LEFRIGHTT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void passTwoLetters() {
        dragFromTo(right, 100, 20);
        
        assertThat("text", right.getText(), is("LERIGHTFT"));
    }
    
    @Test
    public void startWithOverlap() {
        float txDragStart = 100;
        float txDragMiddle = 20;
        float txDragEnd = 30;
        float txExpected = txDragEnd - 3*StubbedTargetDisplay.LETTER_WIDTH;
        
        dragFromTo(right, txDragStart, txDragMiddle);
        right.onPointerStart(new DummyPointerEvent(
                txDragMiddle,
                2*StubbedTargetDisplay.LETTER_WIDTH));
        dragTo(right, txDragEnd);
        float txAfter = right.getTx();
        
        assertThat("text", right.getText(), is("LEFRIGHTT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void withOverlapThenNone() {
        float txDragFrom = 110;
        float txDragMiddle = 40;
        float txDragEnd = 41;
        float txExpected = txDragEnd;

        dragFromTo(right, txDragFrom, txDragMiddle);
        dragTo(right, txDragEnd);
        float txAfter = right.getTx();
        
        assertThat("visible", left.isVisible(), is(true));
        assertThat("text", right.getText(), is("RIGHT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void updatePuzzle() {
        float txDragFrom = 100;
        float txDragTo = 30;

        dragFromTo(right, txDragFrom, txDragTo);
        
        assertThat(puzzle.getCombination(), is("LEFRIGHTT"));
    }
    
    @Test
    public void replacePuzzle() {
        float txDragFrom = 100;
        float txDragTo = 70;

        dragFromTo(right, txDragFrom, txDragTo);
        Puzzle puzzle2 = new Puzzle("leftmost right");
        left.setPuzzle(puzzle2);
        right.setPuzzle(puzzle2);
        left.setVisible(true);
        dragFromTo(right, txDragFrom, txDragTo);
        
        assertThat("text", right.getText(), is("LEFTMOSRIGHTT"));
    }
    
    @Test
    @Ignore
    public void dragLeftPassOneLetter() {
        right.setTx(100);
        float txDragFrom = 0;
        float txDragTo = 70;
        float txExpected = txDragTo + 4*StubbedTargetDisplay.LETTER_WIDTH;

        dragFromTo(left, txDragFrom, txDragTo);
        float txAfter = left.getTx();
        
        assertThat("text", left.getText(), is("RLEFTIGHT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    private void dragFromTo(
            StubbedTargetDisplay target,
            float txDragFrom,
            float txDragTo) {
        target.setTx(txDragFrom);
        target.onPointerStart(new DummyPointerEvent(txDragFrom, 0));
        dragTo(target, txDragTo);
    }
    
    private void dragTo(
            StubbedTargetDisplay target,
            float txDragTo) {
        target.onPointerDrag(new DummyPointerEvent(
                txDragTo,
                txDragTo - target.getTx()));
    }
    
    private static class DummyPointerEvent extends Event.Impl {
        private float localX;
        
        public DummyPointerEvent(float x, float localX) {
            super(new Flags.Impl(), 0, x, 0, true);
            this.localX = localX;
        }
        
        @Override
        public float localX() {
            return localX;
        }
    }
    
    private static class StubbedTargetDisplay extends TargetDisplay {
        private float tx;
        private boolean isVisible = true;
        public static float LETTER_WIDTH = 10; // pretend it's monospace
        
        @Override
        protected void createLayer() {
        }
        
        @Override
        protected float calculateDifferenceBetweenTargetPositions() {
            return ((StubbedTargetDisplay)this).tx - 
                    ((StubbedTargetDisplay)getOpposite()).tx;
        }
        
        @Override
        protected float calculateTextWidth(String text) {
            return text.length() * LETTER_WIDTH;
        }
        
        public void setTx(float tx) {
            this.tx = tx;
        }
        public float getTx() {
            return tx;
        }

        @Override
        protected void translateX(float tx) {
            this.tx += tx;
        }
        
        @Override
        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
        }
        
        @Override
        public boolean isVisible() {
            return isVisible;
        }
        
        @Override
        protected void drawText() {
        }
    }
}
