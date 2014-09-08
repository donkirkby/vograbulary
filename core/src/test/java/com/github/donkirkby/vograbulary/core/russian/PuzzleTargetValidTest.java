package com.github.donkirkby.vograbulary.core.russian;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/* This doesn't test whether the puzzle is solved, just whether the target word
 * and character indexes are valid.
 */
@RunWith(Parameterized.class)
public class PuzzleTargetValidTest {
    @Parameter
    public int targetWord;
    
    @Parameter(value=1)
    public int targetCharacter;
    
    @Parameter(value=2)
    public boolean isValidTargetExpected;
    
    @Parameters(name="word {0} char {1}: {2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {0, 1, true},
                {0, 0, false},
                {0, 5, true},
                {0, 6, false},
                {1, 6, true},
                {1, 7, false},
                {2, 6, false},
                {-1, 1, false} });
    }

    @Test
    public void isTargetValid() {
        Puzzle puzzle = new Puzzle("unable comfort");
        
        puzzle.setTargetWord(targetWord);
        puzzle.setTargetCharacter(targetCharacter);
        boolean isTargetValid = puzzle.isTargetValid();
        
        assertThat("is valid", isTargetValid, is(isValidTargetExpected));
    }
}
