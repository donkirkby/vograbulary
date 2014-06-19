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
    private int changeCount;
    private int completionCount;
    private String changedSolution;
    
    @Before
    public void setUp() {
        letters = "PIE";
        wordList = new WordList();
        wordList.read(new StringReader("PRICE\nPIECE\nPIPE\n"));
        owner = new Student("Student");
        changeCount = 0;
        completionCount = 0;
        changedSolution = null;
        puzzle = new Puzzle(letters, owner, wordList);
        puzzle.addListener(new Puzzle.Listener() {
            @Override
            public void changed() {
                changeCount++;
                changedSolution = puzzle.getSolution();
            }
            
            @Override
            public void completed() {
                completionCount++;
            }
        });
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
    
    @Test
    public void getLettersDisplaySimple() {
        assertThat(
                "letters display", 
                puzzle.getLettersDisplay(), 
                is("PIE"));
    }
    
    @Test
    public void getLettersDisplayPrevious() {
        puzzle.setPreviousWord("PIPE");
        
        assertThat(
                "letters display", 
                puzzle.getLettersDisplay(), 
                is("PIE after PIPE"));
    }
    
    @Test
    public void getNextBetterAfterPrevious() {
        puzzle.setPreviousWord("PRICE");
        puzzle.setSolution(Puzzle.NO_SOLUTION);
        puzzle.setResponse(Puzzle.NO_SOLUTION);
        
        assertThat("hint", puzzle.findNextBetter(), nullValue());
    }
    
    @Test
    public void solutionChangeEvent() {
        String expectedSolution = "XXXX";
        puzzle.setSolution(expectedSolution);
        
        assertThat("change count", changeCount, is(1));
        assertThat("solution", changedSolution, is(expectedSolution));
    }
    
    @Test
    public void responseChangeEvent() {
        puzzle.setResponse("XXXX");
        
        assertThat("change count", changeCount, is(1));
    }
    
    @Test
    public void hintChangeEvent() {
        puzzle.setHint("XXXX");
        
        assertThat("change count", changeCount, is(1));
    }
    
    @Test
    public void completionEvent() {
        puzzle.setSolution("YYYY");
        puzzle.setResponse("XXXX");
        
        assertThat("completion count", completionCount, is(1));
    }
    
    @Test
    public void completionEventHappensOnlyOnce() {
        puzzle.setSolution("YYYY");
        puzzle.setResponse("XXXX");
        puzzle.setHint("ZZZZ");
        
        assertThat("completion count", completionCount, is(1));
    }
}
