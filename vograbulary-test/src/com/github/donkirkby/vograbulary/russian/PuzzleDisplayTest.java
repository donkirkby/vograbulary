package com.github.donkirkby.vograbulary.russian;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PuzzleDisplayTest {
    private Puzzle puzzle;
    private PuzzleDisplay display;
    
    @Parameter
    public int insertX;
    
    @Parameter(value=1)
    public int expectedWordIndex;
    
    @Parameter(value=2)
    public int expectedCharacterIndex;
    
    @Parameters(name="insert at {0} selects word {1} char {2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {  45, -1, 0 }, // between the two targets: no selection
                {  14,  0, 1 }, // each letter is 10 pixels wide
                {  15,  0, 2 }, // round up when we pass half a letter
                {  55,  1, 1 }  // second word
        });
    }

    @Before
    public void setUp() {
        puzzle = new Puzzle("LEFT RIGHT");
        display = new PuzzleDisplay();
        display.setPuzzle(puzzle);
        int word1Left = 0;
        int word1Width = 40;
        int word2Left = 50;
        int word2Width = 50;
        display.setTargetPositions(word1Left, word1Width, word2Left, word2Width);
    }

    @Test
    public void wordIndexNone() {
        display.calculateInsertion(insertX);
        
        assertThat(puzzle.getTargetWord(), is(expectedWordIndex));
        assertThat(puzzle.getTargetCharacter(), is(expectedCharacterIndex));
    }
}
