package com.github.donkirkby.vograbulary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class UltraghostControllerTest {
    private UltraghostController controller;
    private Random random;
    
    @Before
    public void setUp() {
        random = mock(Random.class);
        controller = new UltraghostController();
        controller.setRandom(random);
    }
    
    @Test
    public void nextPuzzle() {
        String expectedLetters = "AXQ";
        setUpNextLetters(expectedLetters);
        
        String puzzle = controller.next();
        
        assertThat("puzzle", puzzle, is(expectedLetters));
    }
    
    @Test
    public void nextSolution() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PICKLE";
        setUpWordList(expectedSolution);
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionThenPuzzle() {
        String expectedLetters = "PIEAPE";
        setUpNextLetters(expectedLetters);
        String expectedPuzzle = "APE";
        
        controller.next(); // get puzzle
        controller.next(); // get solution
        String puzzle = controller.next();
        
        assertThat("puzzle", puzzle, is(expectedPuzzle));
    }
    
    @Test
    public void nextSolutionMatchesLastLetter() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PICKLE";
        setUpWordList("PINT\nPICKLE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionMatchesFirstLetter() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PICKLE";
        setUpWordList("LIME\nPICKLE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionMatchesInteriorLetter() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PICKLE";
        setUpWordList("PASTE\nPICKLE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionMatchesInteriorLetterNotLast() {
        String expectedLetters = "PEE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PEACE";
        setUpWordList("PASTE\nPEACE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionShortest() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PIPE";
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionEarliest() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PILE";
        setUpWordList("PILE\nPIPE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionAtLeastFourLetters() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PILE";
        setUpWordList("PIE\nPILE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionNoneFound() {
        String expectedLetters = "AFR";
        setUpNextLetters(expectedLetters);
        String expectedSolution = UltraghostController.NO_MATCH_MESSAGE;
        setUpWordList("ABDICATE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void wordListLowerCase() {
        String expectedLetters = "PIE";
        setUpNextLetters(expectedLetters);
        String expectedSolution = "PICKLE";
        setUpWordList("pickle");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }

    private void setUpWordList(String expectedSolution) {
        controller.readWordList(new StringReader(expectedSolution));
    }

    private void setUpNextLetters(String expectedLetters) {
        Integer first = expectedLetters.charAt(0) - 'A';
        Integer[] others = new Integer[expectedLetters.length() - 1];
        for (int i = 1; i < expectedLetters.length(); i++) {
            char c = expectedLetters.charAt(i);
            others[i-1] = c - 'A';
        }
        int alphabetSize = 26;
        when(random.nextInt(alphabetSize)).thenReturn(first, others);
    }
}
