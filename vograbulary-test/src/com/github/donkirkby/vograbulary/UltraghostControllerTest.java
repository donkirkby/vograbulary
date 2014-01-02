package com.github.donkirkby.vograbulary;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.ultraghost.DummyView;
import com.github.donkirkby.vograbulary.ultraghost.View;

public class UltraghostControllerTest {
    private UltraghostController controller;
    private Random random;
    private View view;
    private Task searchTask;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        searchTask = null;
        random = mock(Random.class);
        view = mock(View.class);
        controller = new UltraghostController();
        controller.setRandom(random);
        controller.setView(view);
    }
    
    @After
    public void tearDown() {
        if (searchTask != null) {
            searchTask.cancel();
        }
    }
    
    @Test
    public void nextPuzzle() {
        String expectedLetters = "AXQ";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        
        controller.next();

        verify(view).setPuzzle(expectedLetters);
    }
    
    @Test
    public void nextSolution() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList(expectedSolution);
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setPuzzle(expectedLetters);
    }
    
    @Test
    public void nextSolutionThenPuzzle() {
        String expectedPuzzle1 = "PIE";
        String expectedPuzzle2 = "APE";
        controller.setGenerator(new UltraghostDummyGenerator(
                expectedPuzzle1,
                expectedPuzzle2));
        
        controller.next(); // get puzzle
        controller.next(); // get solution
        controller.next();
        
        verify(view).setPuzzle(expectedPuzzle1);
        verify(view).setPuzzle(expectedPuzzle2);
    }
    
    @Test
    public void nextSolutionMatchesLastLetter() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("PINT\nPICKLE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void nextSolutionMatchesFirstLetter() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("LIME\nPICKLE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void nextSolutionMatchesInteriorLetter() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("PASTE\nPICKLE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void nextSolutionMatchesInteriorLetterNotLast() {
        String expectedLetters = "PEE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PEACE";
        setUpWordList("PASTE\nPEACE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void nextSolutionShortest() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PIPE";
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void nextSolutionEarliest() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PILE";
        setUpWordList("PILE\nPIPE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void nextSolutionAtLeastFourLetters() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PILE";
        setUpWordList("PIE\nPILE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void nextSolutionNoneFound() {
        String expectedLetters = "AFR";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = UltraghostController.NO_MATCH_MESSAGE;
        setUpWordList("ABDICATE");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void wordListLowerCase() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("pickle");
        
        controller.next(); // get puzzle
        controller.checkAllWords();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void createSearchTaskWithNoCalls() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = UltraghostController.NO_MATCH_MESSAGE;
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        
        // Search task never triggered, so no solution found.
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void createSearchTaskCancelsAfterNext() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PICKLE";
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        captureSearchTask();
        searchTask.run();
        boolean isScheduledBeforeSolution = searchTask.isScheduled();
        controller.next();
        boolean isScheduledAfterSolution = searchTask.isScheduled();
        
        verify(view).setSolution(expectedSolution);
        assertThat(
                "is scheduled before solution", 
                isScheduledBeforeSolution, 
                is(true));
        assertThat(
                "is scheduled after solution",
                isScheduledAfterSolution,
                is(false));
    }

    private void captureSearchTask() {
        float expectedIntervalSeconds = 0.01f;
        float expectedDelaySeconds = expectedIntervalSeconds;
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(view, atLeastOnce()).schedule(
                captor.capture(), 
                eq(expectedDelaySeconds), 
                eq(expectedIntervalSeconds));

        searchTask = captor.getValue();
        
        Gdx.app = mock(Application.class);
        // Schedule it in the far future so we can check if it gets cancelled.
        Timer.schedule(searchTask, Float.MAX_VALUE, 1);
    }
    
    @Test
    public void createSearchTaskCancelsAfterLastWord() {
        String expectedLetters = "PIE";
        controller.setGenerator(new UltraghostDummyGenerator(expectedLetters));
        String expectedSolution = "PIPE";
        setUpWordList("PICKLE\nPIPE");
        
        controller.next(); // get puzzle
        captureSearchTask();
        searchTask.run();
        boolean isScheduledBeforeLastWord = searchTask.isScheduled();
        searchTask.run();
        boolean isScheduledAfterLastWord = searchTask.isScheduled();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
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
        controller.setSearchBatchSize(2);
        
        controller.next(); // get puzzle
        captureSearchTask();
        searchTask.run();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void createSearchTaskForSecondPuzzleWithoutSolution() {
        setUpWordList("PIPE\nPIECE");
        String expectedSolution1 = "PIPE";
        String expectedSolution2 = UltraghostController.NO_MATCH_MESSAGE;
        controller.setGenerator(new UltraghostDummyGenerator(
                "PIE",
                "XKR"));
        
        controller.next(); // display puzzle
        captureSearchTask();
        searchTask.run();
        controller.next(); // display solution
        
        
        controller.next(); // display next puzzle
        controller.next(); // display next solution
        
        verify(view).setSolution(expectedSolution1);
        verify(view).setSolution(expectedSolution2);
    }
    
    @Test
    public void createSearchTaskForSecondPuzzleWithSolution() {
        setUpWordList("PIPE\nPIECE");
        String expectedSolution1 = "PIPE";
        String expectedSolution2 = "PIECE";
        controller.setGenerator(new UltraghostDummyGenerator(
                "PIE",
                "PEE"));
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        searchTask.run();
        controller.next(); // display solution
        String solution1 = dummyView.getSolution();
        
        controller.next(); // display next puzzle
        String clearedSolution = dummyView.getSolution();
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        searchTask.run();
        controller.next(); // display next solution
        String solution2 = dummyView.getSolution();
        
        assertThat("solution 1", solution1, is(expectedSolution1));
        assertThat("cleared solution", clearedSolution, is(""));
        assertThat("solution 2", solution2, is(expectedSolution2));
    }
    
    @Test
    public void displayPlayerForFirstPuzzle() {
        controller.setGenerator(new UltraghostDummyGenerator("ABC", "XYZ"));
        controller.next(); // display puzzle
        
        verify(view).setActivePlayer("Player");
        
        controller.next(); // display solution
        controller.next(); // display second puzzle
        verify(view).setActivePlayer("Computer");
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
