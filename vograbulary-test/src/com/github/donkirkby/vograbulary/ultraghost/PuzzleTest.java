package com.github.donkirkby.vograbulary.ultraghost;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PuzzleTest {
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
    public void noWordListSolution() {
        puzzle = new Puzzle(letters, owner);
        puzzle.setSolution("PIXXE"); //Won't check that solution is a real word
        
        assertThat("result", puzzle.getResult(), is(WordResult.VALID));
    }
    
    @Test
    public void noWordListResponse() {
        puzzle = new Puzzle(letters, owner);
        puzzle.setSolution("PIXXE"); //Won't check that solution is a real word
        puzzle.setResponse("PIXE"); //Won't check that solution is a real word
        
        assertThat("result", puzzle.getResult(), is(WordResult.SHORTER));
    }
    
    @Test
    public void string() {
        String string = puzzle.toString();
        
        assertThat("string", string, is("Puzzle(PIE, Student)"));
    }
}
