package com.github.donkirkby.vograbulary.core.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MatchPreviousWordTest {
    @Parameter
    public String solution;
    
    @Parameter(value=1)
    public String response;
    
    @Parameter(value=2)
    public String expectedDisplay;

    private Puzzle puzzle1;

    private Match match;

    private WordList wordList;
    
    @Parameters(name="{0}/{1} => {2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] { 
                {"PRIZE", "", "PIE after PRIZE"},
                {"PIERCE", "PRIZE", "PIE after PIERCE"},
                {"PRIZE", "PIERCE", "PIE after PIERCE"},
                {"PRIDE", "PRIZE", "PIE after PRIZE"},
                {"", "PRIZE", "PIE after PRIZE"},
                {"", "", "XRZ"},
                {"", "PIXXXE", "XRZ"}, // Not a word
                {"", "PRICE", "XRZ"} // Too soon
        });
    }
    
    @Before
    public void setUp() {
        wordList = new WordList();
        wordList.read("PIERCE\nPRIZE\nPRICE\nPRIDE\nPIPE");
        int matchScore = 30;
        match = new Match(
                matchScore, 
                new Student("Alice"), 
                new Student("Bob"));
        DummyRandom random = new DummyRandom();
        random.setPuzzles("PIE", "XRZ");
        match.setRandom(random);
        match.setHyperghost(true);
        
        puzzle1 = match.createPuzzle(wordList);
    }
    
    @Test
    public void hyperghost() {
        puzzle1.setPreviousWord("PRICE");
        puzzle1.setSolution(solution);
        puzzle1.setResponse(response);
        
        Puzzle puzzle2 = match.createPuzzle(wordList);
        
        assertThat("letters", puzzle2.getLettersDisplay(), is(expectedDisplay));
    }
    
    @Test
    public void tooShort() {
        puzzle1.setMinimumWordLength(5);
        puzzle1.setSolution("");
        puzzle1.setResponse("PIPE");
        
        Puzzle puzzle2 = match.createPuzzle(wordList);
        
        assertThat("letters", puzzle2.getLettersDisplay(), is("XRZ"));
    }
}
