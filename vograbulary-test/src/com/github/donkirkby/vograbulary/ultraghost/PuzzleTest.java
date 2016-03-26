package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.donkirkby.vograbulary.SerializableTools;

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
        wordList.read(Arrays.asList("PRICE", "PIECE", "PIPE"));
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
        puzzle.setSolution("PRICE");
        puzzle.setResponse("PIECE");
        
        assertThat("completion count", completionCount, is(1));
    }
    
    @Test
    public void completionEventHappensOnlyOnce() {
        puzzle.setSolution("PRICE");
        puzzle.setResponse("PIECE");
        puzzle.setHint("ZZZZ");
        
        assertThat("completion count", completionCount, is(1));
    }
    
    @Test
    public void adjustScoreAtStart() {
        float seconds = 0;
        puzzle.adjustScore(seconds);
        int score = puzzle.getScore(WordResult.NOT_IMPROVED);
        
        assertThat("score", score, is(100));
    }
    
    @Test
    public void adjustScoreAfterDelay() {
        // Lose 2% every second.
        float seconds = 10;
        puzzle.adjustScore(seconds);
        int score = puzzle.getScore(WordResult.NOT_IMPROVED);
        
        assertThat("score", score, is(98));
    }
    
    @Test
    public void adjustScoreAfterSecondDelay() {
        float seconds = 10;
        puzzle.adjustScore(seconds);
        puzzle.adjustScore(seconds);
        int score = puzzle.getScore(WordResult.NOT_IMPROVED);
        
        assertThat("score", score, is(95));
    }
    
    @Test
    public void adjustScoreAfterTimeout() {
        float seconds = 51;
        puzzle.adjustScore(seconds);
        int score = puzzle.getScore(WordResult.NOT_IMPROVED);
        
        assertThat("score", score, is(0));
    }
    
    @Test
    public void adjustScoreWhenPaused() {
        float seconds = 10;
        puzzle.setPaused(true);
        puzzle.adjustScore(seconds);
        int score = puzzle.getScore(WordResult.NOT_IMPROVED);
        
        assertThat("score", score, is(100));
    }

    @Test
    public void togglePause() {
        boolean isPaused1 = puzzle.isPaused();
        puzzle.togglePause();
        boolean isPaused2 = puzzle.isPaused();
        int startCount = changeCount;
        puzzle.togglePause();
        boolean isPaused3 = puzzle.isPaused();
        
        assertThat("paused at start", isPaused1, is(false));
        assertThat("paused after toggle", isPaused2, is(true));
        assertThat("paused after toggle back", isPaused3, is(false));
        assertThat("changeCount", changeCount, is(startCount+1));
    }
    
    @Test
    public void getDisplayAtStart() {
        float seconds = 0;
        puzzle.adjustScore(seconds);
        String display = puzzle.getResultDisplay();
        
        assertThat("display", display, is("100"));
    }
    
    @Test
    public void getDisplayAfterSolution() {
        float seconds = 0;
        puzzle.adjustScore(seconds);
        puzzle.setSolution("PRICE");
        String display = puzzle.getResultDisplay();
        
        assertThat("display", display, is("valid 33 of 100"));
    }
    
    @Test
    public void getDisplayAfterSkip() {
        // Drops slowly at first, to 60 after 40.2s.
        float seconds = 40.2f;
        puzzle.adjustScore(seconds);
        puzzle.setSolution("");
        String display = puzzle.getResultDisplay();
        
        assertThat("display", display, is("skipping -20 of 20"));
    }
    
    @Test
    public void getDisplayAfterResponse() {
        float seconds = 40.2f;
        puzzle.adjustScore(seconds);
        puzzle.setSolution("PRICE");
        puzzle.setResponse("PIECE");
        String display = puzzle.getResultDisplay();
        
        assertThat("display", display, is("earlier (40 of 60)"));
    }
    
    @Test
    public void getDisplayAfterInvalidResponse() {
        float seconds = 40.2f;
        puzzle.adjustScore(seconds);
        puzzle.setSolution("");
        puzzle.setResponse("X");
        String display = puzzle.getResultDisplay();
        
        assertThat("display", display, is("not a word -12 of 20"));
    }
    
    @Test
    public void getScoreNotComplete() {
        int score = puzzle.getScore();
        
        assertThat("display", score, is(0));
    }
    
    @Test
    public void getScoreAfterSolution() {
        puzzle.setSolution("PRICE");
        puzzle.setResponse("");
        int score = puzzle.getScore();
        
        assertThat("display", score, is(100));
    }
    
    @Test
    public void getScoreAfterDelayedSolution() {
        float seconds = 10;
        puzzle.adjustScore(seconds);
        puzzle.setSolution("PRICE");
        puzzle.setResponse("");
        int score = puzzle.getScore();
        
        assertThat("display", score, is(98));
    }
    
    @Test
    public void getScoreAfterShorterResponse() {
        puzzle.setSolution("PRICE");
        puzzle.setResponse("PIPE");
        int score = puzzle.getScore();
        
        assertThat("display", score, is(33));
    }
    
    @Test
    public void getPotentialScore() {
        // Drops slowly at first, to 95 after 20s.
        float solutionSeconds = 20;
        
        puzzle.adjustScore(solutionSeconds);
        int notImprovedScore = puzzle.getScore(WordResult.NOT_IMPROVED);
        int earlierScore = puzzle.getScore(WordResult.EARLIER);
        int shorterScore = puzzle.getScore(WordResult.SHORTER);
        
        assertThat("display", notImprovedScore, is(95));
        assertThat("display", earlierScore, is(63)); // 2/3
        assertThat("display", shorterScore, is(32)); // 1/3
    }
    
    @Test
    public void getScoreAfterInvalidSolution() {
        // Lose 5 seconds for invalid response
        float solutionSeconds = 12;
        float moreSolutionSeconds = 3;
        
        puzzle.adjustScore(solutionSeconds);
        puzzle.setSolution("PRIXE");
        puzzle.adjustScore(moreSolutionSeconds);
        int notImprovedScore = puzzle.getScore(WordResult.NOT_IMPROVED);
        
        assertThat("display", notImprovedScore, is(95));
    }
    
    @Test
    public void getScoreAfterDelayedResponse() {
        // Drops slowly at first, to 60 after 40.2s.
        float solutionSeconds = 40.2f;
        // Gain 4% of difference every second until response 
        float responseSeconds = 5; // time between solution and response
        
        puzzle.adjustScore(solutionSeconds);
        puzzle.setSolution("PRICE");
        puzzle.adjustScore(responseSeconds);
        int notImprovedScore = puzzle.getScore(WordResult.NOT_IMPROVED);
        int earlierScore = puzzle.getScore(WordResult.EARLIER);
        int shorterScore = puzzle.getScore(WordResult.SHORTER);
        
        assertThat("display", notImprovedScore, is(60));
        assertThat("display", earlierScore, is(40 + 4));
        assertThat("display", shorterScore, is(20 + 8));
    }
    
    @Test
    public void getScoreAfterInvalidResponse() {
        // Drops slowly at first, to 60 after 40.2s.
        float solutionSeconds = 40.2f;
        // Gain 4% of difference every second until response,
        // also lose 5 seconds for invalid response
        float responseSeconds = 5; // time between solution and response
        
        puzzle.adjustScore(solutionSeconds);
        puzzle.setSolution("PRICE");
        puzzle.adjustScore(responseSeconds);
        puzzle.setResponse("PCICE");
        int notImprovedScore = puzzle.getScore(WordResult.NOT_IMPROVED);
        int earlierScore = puzzle.getScore(WordResult.EARLIER);
        int shorterScore = puzzle.getScore(WordResult.SHORTER);
        
        assertThat("not improved score", notImprovedScore, is(60));
        assertThat("earlier score", earlierScore, is(40 + 8));
        assertThat("shorter score", shorterScore, is(20 + 16));
        assertThat("completionCount", completionCount, is(0));
    }
    
    @Test
    public void getScoreAfterSkip() {
        // Drops slowly at first, to 60 after 40.2s.
        float solutionSeconds = 40.2f;
        // Gain 4% of difference every second until response 
        float responseSeconds = 5; // time between solution and response
        
        puzzle.adjustScore(solutionSeconds);
        puzzle.setSolution("");
        puzzle.adjustScore(responseSeconds);
        int skippedScore = puzzle.getScore(WordResult.SKIP_NOT_IMPROVED);
        int wordFoundScore = puzzle.getScore(WordResult.WORD_FOUND);
        
        assertThat("skipped score", skippedScore, is(20)); // 1/3 for skip
        assertThat("word found score", wordFoundScore, is(-20 + 8));
    }
    
    @Test
    public void solutionTimeout() {
        // Out of time after 50s.
        float solutionSeconds = 50;
        
        puzzle.adjustScore(solutionSeconds);
        
        assertThat("score", puzzle.getScore(), is(0));
        assertThat("result", puzzle.getResultDisplay(), is("out of time (0)"));
        assertThat("completion count", completionCount, is(1));
    }
    
    @Test
    public void invalidSolutionTimeout() {
        // Lose 2% every second until solution
        float solutionSeconds = 45;
        
        puzzle.adjustScore(solutionSeconds);
        puzzle.setSolution("X");
        
        assertThat("score", puzzle.getScore(), is(0));
        assertThat(
                "result",
                puzzle.getResultDisplay(),
                is("not a word and out of time (0)"));
    }
    
    @Test
    public void responseTimeout() {
        // Gain 4% of difference every second until response 
        float responseSeconds = 25; // time between solution and response
        
        puzzle.setSolution("");
        puzzle.adjustScore(responseSeconds);
        
        assertThat("score", puzzle.getScore(), is(33));
        assertThat(
                "result",
                puzzle.getResultDisplay(),
                is("skipping and out of time (33)"));
        assertThat("completion count", completionCount, is(1));
    }
    
    @Test
    public void invalidResponseTimeout() {
        // Gain 4% of difference every second until response 
        float responseSeconds = 20; // time between solution and response
        
        puzzle.setSolution("");
        puzzle.adjustScore(responseSeconds);
        puzzle.setResponse("X");
        
        assertThat("score", puzzle.getScore(), is(33));
        assertThat(
                "result",
                puzzle.getResultDisplay(),
                is("not a word and out of time (33)"));
        assertThat("completion count", completionCount, is(1));
    }
    
    @Test
    public void serialize() throws Exception {
        puzzle.setSolution("BAD");
        puzzle.getResult();
        
        byte[] bytes = SerializableTools.serialize(puzzle);
        Puzzle puzzle2 = SerializableTools.deserialize(bytes, Puzzle.class);
        
        assertThat("letters", puzzle2.getLetters(), is(puzzle.getLetters()));
    }
}
