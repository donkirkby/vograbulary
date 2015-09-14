package com.github.donkirkby.vograbulary;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.donkirkby.vograbulary.LetterDisplay.LetterDisplayListener;

public class LetterDisplayTest implements LetterDisplayListener {
    public static final int LETTER_WIDTH = 5;
    public static final int LETTER_HEIGHT = 8;
    
    public static class DummyLetterDisplay extends LetterDisplay {
        private int left;
        private int top;
        private float textSize;
        private String letter;
        
        public DummyLetterDisplay(String letter) {
            this.letter = letter;
        }
        
        @Override
        public int getTop() {
            return top;
        }
        
        @Override
        public void setTop(int top) {
            this.top = top;
        }

        @Override
        public int getLeft() {
            return left;
        }

        @Override
        public void setLeft(int left) {
            this.left = left;
        }
        
        @Override
        public void animateTo(int left, int top) {
            setLeft(left);
            setTop(top);
        }
        
        @Override
        public float getTextSize() {
            return textSize;
        }
        @Override
        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }
        
        @Override
        public int getHeight() {
            return LETTER_HEIGHT;
        }

        @Override
        public int getWidth() {
            return LETTER_WIDTH;
        }

        @Override
        public String getLetter() {
            return letter;
        }
    }

    public static class DummyLetterDisplayFactory extends LetterDisplayFactory {
        private List<DummyLetterDisplay> active = new ArrayList<>();
        
        @Override
        public LetterDisplay create(String letter) {
            LetterDisplayTest.DummyLetterDisplay display =
                    new LetterDisplayTest.DummyLetterDisplay(letter);
            active.add(display);
            return display;
        }

        @Override
        public void destroy(LetterDisplay letter) {
            active.remove(letter);
        }
        
        public List<DummyLetterDisplay> getActive() {
            return active;
        }
    }

    private LetterDisplay display;
    private int clickCount;
    
    @Before
    public void setUp() {
        display = new DummyLetterDisplay("P");
        display.addClickListener(this);
        clickCount = 0;
    }

    @Override
    public void onClick(LetterDisplay letter) {
        clickCount++;
    }

    @Test
    public void right() {
        int left = 10;
        int expectedRight = left + LETTER_WIDTH;
        display.setLeft(left);
        
        int right = display.getRight();
        
        assertThat("right", right, is(expectedRight));
    }
    
    @Test
    public void getCentreY() {
        int top = 10;
        int expectedCentrey = top + LETTER_HEIGHT/2;
        display.setTop(top);
        
        assertThat("centre y", display.getCentreY(), is(expectedCentrey));
    }
    
    @Test
    public void setCentreY() {
        int centreY = 10;
        int expectedTop = centreY - LETTER_HEIGHT/2;
        display.setCentreY(centreY);
        
        assertThat("top", display.getTop(), is(expectedTop));
    }
    
    @Test
    public void getCentreX() {
        int left = 10;
        int expectedCentreX = left + LETTER_WIDTH/2;
        display.setLeft(left);
        
        assertThat("centre x", display.getCentreX(), is(expectedCentreX));
    }
    
    @Test
    public void setCentreX() {
        int centreX = 10;
        int expectedLeft = centreX - LETTER_WIDTH/2;
        display.setCentreX(centreX);
        
        assertThat("left", display.getLeft(), is(expectedLeft));
    }
    
    @Test
    public void click() {
        display.click();
        
        assertThat("click count", clickCount, is(1));
    }
}
