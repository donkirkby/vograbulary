package com.github.donkirkby.vograbulary;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class UltraghostControllerTest {
    private UltraghostController controller;
    private Random random;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        random = mock(Random.class);
        controller = new UltraghostController();
        controller.setRandom(random);
    }
    
    @Test
    public void nextPuzzle() {
        String expectedLetters = "AXQ";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        
        String puzzle = controller.next();
        
        assertThat("puzzle", puzzle, is(expectedLetters));
    }
    
    @Test
    public void nextSolution() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList(expectedSolution);
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionThenPuzzle() {
        controller.setGenerator(new UltraghostDummyGenerator(
                "PIE",
                "APE"));
        String expectedPuzzle = "APE";
        
        controller.next(); // get puzzle
        controller.next(); // get solution
        String puzzle = controller.next();
        
        assertThat("puzzle", puzzle, is(expectedPuzzle));
    }
    
    @Test
    public void nextSolutionMatchesLastLetter() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("PINT\nPICKLE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionMatchesFirstLetter() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("LIME\nPICKLE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionMatchesInteriorLetter() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("PASTE\nPICKLE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionMatchesInteriorLetterNotLast() {
        String expectedLetters = "PEE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PEACE";
        setUpWordList("PASTE\nPEACE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionShortest() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PIPE";
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionEarliest() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PILE";
        setUpWordList("PILE\nPIPE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionAtLeastFourLetters() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PILE";
        setUpWordList("PIE\nPILE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void nextSolutionNoneFound() {
        String expectedLetters = "AFR";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = UltraghostController.NO_MATCH_MESSAGE;
        setUpWordList("ABDICATE");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void wordListLowerCase() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("pickle");
        
        controller.next(); // get puzzle
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void createSearchTaskWithNoCalls() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = UltraghostController.NO_MATCH_MESSAGE;
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        controller.createSearchTask(); // Never called, so no solution found.
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void createSearchTaskCancelsAfterNext() {
        Gdx.app = mock(Application.class);
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        Task searchTask = controller.createSearchTask();
        Timer.schedule(searchTask, Float.MAX_VALUE, 1);
        searchTask.run();
        boolean isScheduledBeforeSolution = searchTask.isScheduled();
        String solution = controller.next();
        boolean isScheduledAfterSolution = searchTask.isScheduled();
        
        assertThat("solution", solution, is(expectedSolution));
        assertThat(
                "is scheduled before solution", 
                isScheduledBeforeSolution, 
                is(true));
        assertThat(
                "is scheduled after solution",
                isScheduledAfterSolution,
                is(false));
    }
    
    @Test
    public void createSearchTaskCancelsAfterLastWord() {
        Gdx.app = mock(Application.class);
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PIPE";
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        Task searchTask = controller.createSearchTask();
        Timer.schedule(searchTask, Float.MAX_VALUE, 1);
        searchTask.run();
        boolean isScheduledBeforeLastWord = searchTask.isScheduled();
        searchTask.run();
        boolean isScheduledAfterLastWord = searchTask.isScheduled();
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
        assertThat(
                "is scheduled before last word", 
                isScheduledBeforeLastWord, 
                is(true));
        assertThat(
                "is scheduled after last word",
                isScheduledAfterLastWord,
                is(false));
    }
    
    @Test
    public void createSearchTaskForTwoWordsEachRun() {
        Gdx.app = mock(Application.class);
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("AIRBAG\nPICKLE\nPIPE");
        
        controller.next(); // get puzzle
        Task searchTask = controller.createSearchTask(2);
        Timer.schedule(searchTask, Float.MAX_VALUE, 1);
        searchTask.run();
        String solution = controller.next();
        
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void createSearchTaskTwice() {
        setUpWordList("ARBITRARY\nWORDS");
        controller.next(); // get puzzle
        controller.createSearchTask();
        
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(
                "A search task has already been created for this puzzle.");
        controller.createSearchTask();
    }
    
    @Test
    public void createSearchTaskForSecondPuzzle() {
        setUpWordList("PIPE\nPIECE");
        controller.setGenerator(new UltraghostDummyGenerator(
                "PIE",
                "XKR"));
        
        controller.next(); // get puzzle
        Task searchTask = controller.createSearchTask();
        searchTask.run();
        String solution1 = controller.next();
        controller.next(); // get next puzzle
        controller.createSearchTask(); // shouldn't throw.
        String solution2 = controller.next();
        
        assertThat("solution 1", solution1, is("PIPE"));
        assertThat(
                "solution 2", 
                solution2, 
                is(UltraghostController.NO_MATCH_MESSAGE));
    }
    
    @Test
    public void createSearchTaskWithoutPuzzle() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(
                "No puzzle to search.");
        controller.createSearchTask();
    }
    
    @Test
    public void passWordsToGenerator() {
        setUpWordList("ARBITRARY\nWORDS");
        UltraghostDummyGenerator generator = new UltraghostDummyGenerator();
        controller.setGenerator(generator);
        
        assertThat("word list", generator.getWordList(), hasItem("ARBITRARY"));
    }
    
    @Test
    public void passWordsToGeneratorWhenWordsSetLater() {
        UltraghostDummyGenerator generator = new UltraghostDummyGenerator();
        controller.setGenerator(generator);
        setUpWordList("ARBITRARY\nWORDS");
        
        assertThat("word list", generator.getWordList(), hasItem("ARBITRARY"));
    }
    
    private void setUpWordList(String words) {
        controller.readWordList(new StringReader(words));
    }
}
