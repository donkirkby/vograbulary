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
    private static final int NO_SELECTION = Puzzle.NO_SELECTION;
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
                {  55, NO_SELECTION, NO_SELECTION }, // between the two targets
                {  24,  0, 1 }, // each letter is 10 pixels wide
                {  14,  0, NO_SELECTION }, // before the first letter
                {  25,  0, 2 }, // round up when we pass half a letter
                {  65,  1, 1 }  // second word
        });
    }

    @Before
    public void setUp() {
        puzzle = new Puzzle("LEFT RIGHT");
        display = new PuzzleDisplay();
        display.setPuzzle(puzzle);
        int word1Left = 10;
        int word1Width = 40;
        int word2Left = 60;
        int word2Width = 50;
        display.setTargetPositions(word1Left, word1Width, word2Left, word2Width);
    }

    @Test
    public void setTargets() {
        display.calculateInsertion(insertX);
        
        assertThat(puzzle.getTargetWord(), is(expectedWordIndex));
        assertThat(puzzle.getTargetCharacter(), is(expectedCharacterIndex));
    }
}
