package com.github.donkirkby.vograbulary.core.russian;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import playn.core.Events.Flags;
import playn.core.Pointer.Event;

public class TargetDisplayTest {
    private StubbedTargetDisplay left;
    private StubbedTargetDisplay right;

    @Before
    public void setUp() {
        left = new StubbedTargetDisplay();
        right = new StubbedTargetDisplay();
        right.withLeftSide(left);
        left.setText("LEFT");
        right.setText("RIGHT");
    }
    
    @Test
    public void withoutOverlap() {
        dragFromTo(right, 110, 105);
        
        assertThat("visible", left.isVisible(), is(true));
    }
    
    @Test
    public void withOverlap() {
        float txDragFrom = 110;
        float txDragTo = 99;
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
        float txDragTo = 90;
        float txExpected = txDragTo - 3*StubbedTargetDisplay.LETTER_WIDTH;

        dragFromTo(right, txDragFrom, txDragTo);
        float txAfter = right.getTx();
        
        assertThat("text", right.getText(), is("LEFRIGHTT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void passOneLetterAndPause() {
        float txDragFrom = 100;
        float txDragTo = 90;
        float txExpected = txDragTo - 3*StubbedTargetDisplay.LETTER_WIDTH;
        right.setTx(txDragFrom);
        
        dragFromTo(right, txDragFrom, txDragTo - 1);
        dragTo(right, txDragTo); // should be stable
        float txAfter = right.getTx();
        
        assertThat("text", right.getText(), is("LEFRIGHTT"));
        assertThat("tx after", txAfter, is(txExpected));
    }
    
    @Test
    public void passTwoLetters() {
        dragFromTo(right, 100, 80);
        
        assertThat("text", right.getText(), is("LERIGHTFT"));
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
            return ((StubbedTargetDisplay)getRightSide()).tx - 
                    ((StubbedTargetDisplay)getLeftSide()).tx;
        }
        
        @Override
        protected float getLeftSideWidth() {
            return 100;
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
        protected void setVisible(boolean isVisible) {
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
