package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class LetterDisplayTest {
    public static final int LETTER_WIDTH = 5;
    
    public static class DummyLetterDisplay extends LetterDisplay {
        private int left;
        private String letter;
        
        public DummyLetterDisplay(String letter, int x) {
            this.left = x;
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
        int x = 10;
        int expectedRight = x + LETTER_WIDTH;
        LetterDisplay display = new DummyLetterDisplay("P", x);
        
        int right = display.getRight();
        
        assertThat("right", right, is(expectedRight));
    }
}
