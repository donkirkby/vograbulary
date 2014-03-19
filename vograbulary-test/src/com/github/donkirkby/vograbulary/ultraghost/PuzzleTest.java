package com.github.donkirkby.vograbulary.ultraghost;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PuzzleTest {
    private static String INVALID_SOLUTION = "PITE";
    private static String VALID_SOLUTION = "PIECE";
    private static String SHORTER_RESPONSE = "PIPE";
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private Puzzle puzzle;
    private String letters;
    private WordList wordList;
    private Student owner;
    
    @Before
    public void setUp() {
        letters = "PIE";
        wordList = new WordList();
        wordList.read(new StringReader("PRICE\nPIECE\nPIPE\n"));
        owner = new Student("Student");
        puzzle = new Puzzle(letters, owner, wordList);
    }
    
    @Test 
    public void startingResult() {
        WordResult result = puzzle.getResult();
        assertThat("result", result, is(WordResult.UNKNOWN));
    }
    
    @Test
    public void validSolution() {
        puzzle.setSolution(VALID_SOLUTION);
        WordResult result = puzzle.getResult();
        assertThat("result", result, is(WordResult.VALID));
    }
    
    @Test
    public void invalidSolution() {
        puzzle.setSolution(INVALID_SOLUTION);
        WordResult result = puzzle.getResult();
        assertThat("result", result, is(WordResult.NOT_A_WORD));
    }
    
    @Test
    public void validResponse() {
        puzzle.setSolution(VALID_SOLUTION);
        puzzle.setResponse(SHORTER_RESPONSE);
        WordResult result = puzzle.getResult();
        assertThat("result", result, is(WordResult.SHORTER));
    }
    
    @Test
    public void skipped() {
        puzzle.setSolution(Puzzle.NO_SOLUTION);
        puzzle.setResponse(Puzzle.NO_SOLUTION);
        WordResult result = puzzle.getResult();
        assertThat("result", result, is(WordResult.SKIPPED));
    }
    
    @Test
    public void responseWithoutSolution() {
        puzzle.setResponse(SHORTER_RESPONSE);
        WordResult result = puzzle.getResult();
        assertThat("result", result, is(WordResult.UNKNOWN));
    }
    
    @Test
    public void nullLetters() {
        letters = null;
        
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Puzzle letters were null.");
        new Puzzle(letters, owner, wordList);
    }
    
    @Test
    public void nullOwner() {
        owner = null;
        
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Puzzle owner was null.");
        new Puzzle(letters, owner, wordList);
    }
    
    @Test
    public void nullWordList() {
        wordList = null;
        
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Puzzle word list was null.");
        new Puzzle(letters, owner, wordList);
    }
}
