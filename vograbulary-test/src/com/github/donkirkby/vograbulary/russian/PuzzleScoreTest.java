package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PuzzleScoreTest {
    @Parameter
    public float seconds;
    
    @Parameter(value=1)
    public String expectedScore;
    
    @Parameters(name="after {0}s score {2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {  0,     "100"},   // Score starts at 100 
                { 10,      "50"},   // Divides in half every 10 seconds
                { 20,      "25"}, 
                { 30,      "12"},   // 12.5 rounds down
                { 40,       "6.2"}, // 6.25 rounds down
                { 33.21f,  "10"},   // always show 2 significant digits
                { 33.22f,   "9.9"}, // start fractional part below 10
                { 66.43f,   "1.0"},
                { 66.44f,   "0.99"}, // longer fraction below 1
                {197,       "0.00011"}, 
                {198,       "0.00010"}, // score bottoms out
                {300,       "0.00010"} }); // doesn't get any lower
    }

    @Test
    public void adjustmentsAddUp() {
        Puzzle puzzle = new Puzzle("not relevant");
        
        String scoreText = puzzle.adjustScore(seconds);
        BigDecimal score = puzzle.getScore();
        String scoreDisplay = puzzle.getScoreDisplay();
        
        assertThat("score", score, is(new BigDecimal(expectedScore)));
        assertThat("score text", scoreText, is(expectedScore));
        assertThat("score display", scoreDisplay, is(expectedScore));
    }
}
