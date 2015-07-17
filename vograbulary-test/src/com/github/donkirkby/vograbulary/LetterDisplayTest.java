package com.github.donkirkby.vograbulary;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class LetterDisplayTest {
    public static final int LETTER_WIDTH = 5;
    
    public static class DummyLetterDisplay extends LetterDisplay {
        private int left;
        private String letter;
        
        public DummyLetterDisplay(String letter) {
            this.letter = letter;
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
        public int getWidth() {
            return LETTER_WIDTH;
        }

        @Override
        public String getLetter() {
            return letter;
        }
    }

    @Test
    public void right() {
        int left = 10;
        int expectedRight = left + LETTER_WIDTH;
        LetterDisplay display = new DummyLetterDisplay("P");
        display.setLeft(left);
        
        int right = display.getRight();
        
        assertThat("right", right, is(expectedRight));
    }
}
