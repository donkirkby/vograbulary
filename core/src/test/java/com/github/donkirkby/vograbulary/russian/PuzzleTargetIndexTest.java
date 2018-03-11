package com.github.donkirkby.vograbulary.russian;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PuzzleTargetIndexTest {
    @Parameter
    public int targetWord;
    
    @Parameter(value=1)
    public int targetCharacter;
    
    @Parameter(value=2)
    public String expectedErrorMessage;
    
    @Parameters(name="{0}, {1} expects {2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {-1, 1, "Target word index -1 is invalid." },
                { 0, 1, null },
                { 1, 1, null },
                { 2, 1, "Target word index 2 is invalid." },
                { 0, 0, "Target character index 0 is invalid." },
                { 0, 5, null },
                { 0, 6, "Target character index 6 is invalid." },
                { 1, 6, null },
                { 1, 7, "Target character index 7 is invalid." }
        });
    }
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void setTargets() {
        Puzzle puzzle = new Puzzle("unable comfort");
        if (expectedErrorMessage != null) {
            thrown.expect(ArrayIndexOutOfBoundsException.class);
            thrown.expectMessage(expectedErrorMessage);
        }
        
        puzzle.setTargetWord(targetWord);
        puzzle.setTargetCharacter(targetCharacter);
    }
}
