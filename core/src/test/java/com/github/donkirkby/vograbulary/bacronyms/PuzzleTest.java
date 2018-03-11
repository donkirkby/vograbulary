package com.github.donkirkby.vograbulary.bacronyms;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PuzzleTest {
    private Puzzle puzzle;
    
    @Before
    public void setup() {
        puzzle = new Puzzle();
        puzzle.addWord("left");
        puzzle.addWord("middle");
        puzzle.addWord("right");
    }
    
    @Test
    public void addWord() {
        String expectedWord = "potato";
        puzzle = new Puzzle();
        puzzle.addWord(expectedWord);
        String word = puzzle.getWord(0);
        
        assertThat("word", word, is(expectedWord));
    }
    
    @Test
    public void getWord() {
        String word = puzzle.getWord(1);
        assertThat("word", word, is("middle"));
    }
    
    @Test
    public void wordReversed() {
        puzzle.setSelectedIndex(1);
        String word = puzzle.getWord(1);
        assertThat("word", word, is("elddim"));
    }
    
    @Test
    public void selectFirstWord() {
        String expectedWord = "tfel";
        
        puzzle.setSelectedIndex(0);
        String word = puzzle.getSelectedWord();
        
        assertThat("word", word, is(expectedWord));
    }
    
    @Test
    public void selectLastWord() {
        String expectedWord = "thgir";
        
        puzzle.setSelectedIndex(2);
        String word = puzzle.getSelectedWord();
        
        assertThat("word", word, is(expectedWord));
    }
    
    @Test
    public void isSelected() {
        assertThat("isSelected before", puzzle.isSelected(), is(false));
        puzzle.setSelectedIndex(0);
        assertThat("isSelected after", puzzle.isSelected(), is(true));
    }
}
