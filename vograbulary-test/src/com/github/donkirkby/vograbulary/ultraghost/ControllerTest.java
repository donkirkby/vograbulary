package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;

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

public class ControllerTest {
    private Controller controller;
    private DummyRandom random;
    private View view;
    private Task searchTask;
    private Student student;
    private ComputerStudent computerStudent;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        Gdx.app = mock(Application.class);
        searchTask = null;
        random = new DummyRandom();
        view = mock(View.class);
        student = new Student("Student");
        computerStudent = new ComputerStudent();
        controller = new Controller();
        setUpWordList("");
        controller.setRandom(random);
        controller.setView(view);
        controller.addStudent(computerStudent);
        controller.addStudent(student);
    }
    
    @After
    public void tearDown() {
        if (searchTask != null) {
            searchTask.cancel();
        }
    }
    
    private void setUpStudents(Student...students) {
        controller.clearStudents();
        for (Student student : students) {
            controller.addStudent(student);
        }
    }
    
    @Test
    public void nextPuzzle() {
        String expectedLetters = "AXQ";
        random.setPuzzles(expectedLetters);
        
        controller.next();

        verify(view).setPuzzle(expectedLetters);
    }
    
//    @Test
//    public void nextSolution() {
//        random.setPuzzles("PIE");
//        String expectedSolution = "PICKLE";
//        setUpWordList(expectedSolution);
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next(); // display solution
//        
//        verify(view).setSolution(expectedSolution);
//        verify(view).focusChallenge();
//    }
    
    @Test
    public void nextSolutionThenChallengeAndPuzzle() {
        String expectedPuzzle1 = "PIE";
        String expectedPuzzle2 = "APE";
        random.setPuzzles(
                expectedPuzzle1,
                expectedPuzzle2);
        
        controller.next(); // get puzzle
        controller.next(); // get solution
        controller.next(); // get challenge
        controller.next();
        
        verify(view).setPuzzle(expectedPuzzle1);
        verify(view).setPuzzle(expectedPuzzle2);
    }
    
//    @Test
//    public void nextSolutionMatchesLastLetter() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PICKLE";
//        setUpWordList("PINT\nPICKLE");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
//    
//    @Test
//    public void nextSolutionMatchesFirstLetter() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PICKLE";
//        setUpWordList("LIME\nPICKLE");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
//    
//    @Test
//    public void nextSolutionMatchesInteriorLetter() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PICKLE";
//        setUpWordList("PASTE\nPICKLE");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
//    
//    @Test
//    public void nextSolutionMatchesInteriorLetterNotLast() {
//        String expectedLetters = "PEE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PEACE";
//        setUpWordList("PASTE\nPEACE");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
//    
//    @Test
//    public void nextSolutionShortest() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PIPE";
//        setUpWordList("PICKLE\nPIPE");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
//    
//    @Test
//    public void nextSolutionEarliest() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PILE";
//        setUpWordList("PILE\nPIPE");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
//    
//    @Test
//    public void nextSolutionAtLeastFourLetters() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PILE";
//        setUpWordList("PIE\nPILE");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
    
    @Test
    public void nextSolutionNoneFound() {
        random.setPuzzles("AFR");
        String expectedSolution = Controller.NO_MATCH_MESSAGE;
        setUpWordList("ABDICATE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        searchTask = dummyView.getSearchTask();
        searchTask.run(); // display solution
        String solution = dummyView.getSolution();
        
        assertThat("solution", solution, is(expectedSolution));
    }
//    
//    @Test
//    public void wordListLowerCase() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = "PICKLE";
//        setUpWordList("pickle");
//        
//        controller.next(); // get puzzle
//        controller.checkAllWords();
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
//    
//    @Test
//    public void createSearchTaskWithNoCalls() {
//        String expectedLetters = "PIE";
//        random.setPuzzles(expectedLetters);
//        String expectedSolution = Controller.NO_MATCH_MESSAGE;
//        setUpWordList("PICKLE\nPIPE");
//        
//        controller.next(); // get puzzle
//        
//        // Search task never triggered, so no solution found.
//        controller.next();
//        
//        verify(view).setSolution(expectedSolution);
//    }
    
    @Test
    public void createSearchTaskCancelsAfterNext() {
        String expectedLetters = "PIE";
        random.setPuzzles(expectedLetters);
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
        random.setPuzzles(expectedLetters);
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
        random.setPuzzles(expectedLetters);
        String expectedSolution = "PICKLE";
        setUpWordList("AIRBAG\nPICKLE\nPIPE");
        computerStudent.setSearchBatchSize(2);
        
        controller.next(); // get puzzle
        captureSearchTask();
        searchTask.run();
        controller.next();
        
        verify(view).setSolution(expectedSolution);
    }
    
    @Test
    public void createSearchTaskForTwoWordsEachRunAndCheckCancel() {
        Gdx.app = mock(Application.class);
        String expectedLetters = "PIE";
        random.setPuzzles(expectedLetters);
        setUpWordList("AIRBAG\nPICKLE\nPIPE");
        computerStudent.setSearchBatchSize(2);
        
        controller.next(); // get puzzle
        captureSearchTask();
        searchTask.run();
        boolean isScheduledBeforeLastWord = searchTask.isScheduled();
        searchTask.run();
        boolean isScheduledAfterLastWord = searchTask.isScheduled();
        
        assertThat(
                "scheduled before last word", 
                isScheduledBeforeLastWord, 
                is(true));
        assertThat(
                "scheduled after last word", 
                isScheduledAfterLastWord, 
                is(false));
    }
    
    @Test
    public void createSearchTaskForSecondPuzzleWithSolution() {
        setUpWordList("PIPE\nPIECE\nLOOP");
        String expectedSolution1 = "PIPE";
        String expectedSolution2 = "PIECE";
        random.setPuzzles("PIE", "PEE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle for computer
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        searchTask.run();
        controller.next(); // display solution by computer
        String solution1 = dummyView.getSolution();
        dummyView.setChallenge("xxxx");
        controller.next(); // check challenge and display result
        
        controller.next(); // display next puzzle for human
        String clearedSolution = dummyView.getSolution();
        String clearedChallenge = dummyView.getChallenge();
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        searchTask.run();
        controller.next(); // display challenge by computer
        String solution2 = dummyView.getChallenge();
        
        assertThat("solution 1", solution1, is(expectedSolution1));
        assertThat("cleared solution", clearedSolution, is(""));
        assertThat("cleared challenge", clearedChallenge, is(""));
        assertThat("solution 2", solution2, is(expectedSolution2));
    }
    
    @Test
    public void computerChallengeImmediatelyShowsResult() {
        setUpWordList("PIPE\nPIECE");
        String expectedPuzzle2 = "PEE";
        random.setPuzzles("PIE", expectedPuzzle2);
        setUpStudents(student, computerStudent);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer challenge
        dummyView.setSolution("PIECE"); // enter human solution
        controller.next(); // display computer challenge and result
        String result = dummyView.getResult();
        controller.next(); // display puzzle 2
        String puzzle2 = dummyView.getPuzzle();
        
        assertThat("result", result, is("shorter (+1)"));
        assertThat("puzzle 2", puzzle2, is(expectedPuzzle2));
    }
    
    @Test
    public void humanThenComputer() {
        setUpWordList("PIPE\nPIECE");
        random.setPuzzles("PIE", "PEE");
        setUpStudents(student, computerStudent);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle for human
        controller.next(); // display computer challenge and result
        controller.next(); // display puzzle 2 for computer
        String activeStudent = dummyView.getActiveStudent();
        
        assertThat("active student", activeStudent, is("Computer"));
    }
    
    @Test
    public void humanSkip() {
        setUpWordList("PIPE\nPIECE");
        random.setPuzzles("DNX");
        setUpStudents(student, computerStudent);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle for human
        // skip
        controller.next(); // display computer challenge (none)
        String challenge = dummyView.getChallenge();
        
        assertThat("challenge", challenge, is(Controller.NO_MATCH_MESSAGE));
    }
    
    /** If it can't improve the solution, the computer will not display a 
     * challenge. 
     */
    @Test
    public void computerDoesNotChallenge() {
        setUpWordList("PIPE\nPIECE");
        random.setPuzzles("PIE");
        setUpStudents(student, computerStudent);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer challenge
        dummyView.setSolution("PIPE"); // enter human solution
        controller.next(); // display computer challenge and result
        String challenge = dummyView.getChallenge();
        
        assertThat("challenge", challenge, is(Controller.NO_MATCH_MESSAGE));
    }
    
    @Test
    public void humanChallengeNotAWord() {
        setUpWordList("PIECE\nPIPE");
        random.setPuzzles("PIE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer solution
        controller.next(); // display computer solution
        dummyView.setChallenge("PIXE"); // enter human challenge
        controller.next(); // display result
        String focus = dummyView.getCurrentFocus();
        String result = dummyView.getResult();
        String scores = dummyView.getScores();
        
        assertThat("focus", focus, is("next"));
        assertThat("result", result, is("not a word (+3)"));
        assertThat("scores", scores, is("Computer 3\nStudent 0"));
    }
    
    @Test
    public void humanChallengeLonger() {
        setUpWordList("PIPE\nPIECE");
        random.setPuzzles("PIE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer solution
        controller.next(); // display computer solution
        dummyView.setChallenge("piece"); // enter human challenge
        controller.next(); // display result
        String result = dummyView.getResult();
        
        assertThat("result", result, is("longer (+3)"));
    }
    
    @Test
    public void humanChallengeLater() {
        setUpWordList("PINE\nPIPE");
        random.setPuzzles("PIE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer solution
        controller.next(); // display computer solution
        dummyView.setChallenge("pipe"); // enter human challenge
        controller.next(); // display result
        String result = dummyView.getResult();
        
        assertThat("result", result, is("later (+3)"));
    }
    
    @Test
    public void timerBugDoesNotSkipHumanChallenge() {
        setUpWordList("PINE\nPIPE");
        random.setPuzzles("PIE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        computerStudent.setMaxSearchBatchCount(1);
        
        controller.next(); // display puzzle
        searchTask = dummyView.getSearchTask();
        searchTask.run(); // find computer solution and display
        searchTask.run(); // run an extra time to simulate timer bug
        String result = dummyView.getResult();
        
        assertThat("result", result, is(""));
    }
    
    @Test
    public void computerSolutionNotChallenged() {
        setUpWordList("PIECE\nPIPE");
        random.setPuzzles("PIE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer solution
        controller.next(); // display computer solution
        controller.next(); // display result (No human challenge)
        String focus = dummyView.getCurrentFocus();
        String result = dummyView.getResult();
        
        assertThat("focus", focus, is("next"));
        assertThat("result", result, is("not improved (+3)"));
    }
    
    @Test
    public void humanSolutionNotAWord() {
        setUpWordList("PIECE\nPIPE");
        random.setPuzzles("PIE", "RPE");
        setUpStudents(student, computerStudent);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer solution
        dummyView.setSolution("PIXE"); // enter human challenge
        controller.next(); // display result
        String focus = dummyView.getCurrentFocus();
        String result = dummyView.getResult();
        
        controller.next(); // next puzzle
        String newResult = dummyView.getResult();
        
        assertThat("focus", focus, is("next"));
        assertThat("result", result, is("not a word"));
        assertThat("new result", newResult, is(""));
    }
    
    @Test
    public void humanSolutionNoMatch() {
        setUpWordList("ROPE\nPIECE\nPIPE");
        random.setPuzzles("RPE");
        setUpStudents(student, computerStudent);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        dummyView.getSearchTask().run(); // find computer solution
        dummyView.setSolution("PIPE"); // enter human challenge
        controller.next(); // display result
        String result = dummyView.getResult();
        String challenge = dummyView.getChallenge();
        
        assertThat("result", result, is("not a match"));
        assertThat("challenge", challenge, is(""));
    }
    
    @Test
    public void displaySolutionForComputerPlayer() {
        setUpWordList("PIPE\nPIECE");
        String expectedSolution = "PIPE";
        random.setPuzzles("PIE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        controller.next(); // display solution
        String solution = dummyView.getSolution();
        String challenge = dummyView.getChallenge();
        
        assertThat("solution", solution, is(expectedSolution));
        assertThat("challenge", challenge, is(""));
    }
    
    @Test
    public void displayChallengeForHumanPlayer() {
        setUpWordList("PIPE\nPIECE");
        String expectedChallenge = "PIPE";
        random.setPuzzles("PIE");
        setUpStudents(student, computerStudent);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        controller.next(); // display challenge
        String challenge = dummyView.getChallenge();
        
        assertThat("challenge", challenge, is(expectedChallenge));
    }
    
    @Test
    public void displayPlayerForFirstPuzzle() {
        random.setPuzzles("ABC");
        setUpStudents(student, computerStudent);
        controller.next(); // display puzzle
        
        verify(view).setActiveStudent("Student");
        verify(view).focusSolution();
    }
    
    @Test
    public void displayComputerForFirstPuzzle() {
        random.setPuzzles("ABC");
        controller.next(); // display puzzle
        
        verify(view).setActiveStudent("Computer");
        verify(view).focusNextButton();
    }
    
    @Test
    public void displayPlayerForSecondPuzzle() {
        random.setPuzzles("ABC", "XYZ");
        controller.next(); // display puzzle
        controller.next(); // display solution
        controller.next(); // display challenge

        controller.next(); // display second puzzle
        
        verify(view).setActiveStudent("Student");
    }
    
    @Test
    public void passWordsToGenerator() {
        setUpWordList("ARBITRARY\nWORDS");
        DummyRandom generator = new DummyRandom();
        controller.setRandom(generator);
        
        assertThat("word list", generator.getWordList(), hasItem("ARBITRARY"));
    }
    
    @Test
    public void passWordsToGeneratorWhenWordsSetLater() {
        DummyRandom generator = new DummyRandom();
        controller.setRandom(generator);
        setUpWordList("ARBITRARY\nWORDS");
        
        assertThat("word list", generator.getWordList(), hasItem("ARBITRARY"));
    }
    
    @Test
    public void computerTimerCount() {
        setUpWordList("PRIDE\nPIECE\nPIPE");
        random.setPuzzles("PIE");
        computerStudent.setMaxSearchBatchCount(2);
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        searchTask.run(); // display solution
        String solution = dummyView.getSolution();
        String focus = dummyView.getCurrentFocus();
        
        assertThat("solution", solution, is("PIECE"));
        assertThat("focus", focus, is("challenge"));
    }
    
    @Test
    public void computerTimerEndOfWordList() {
        setUpWordList("PRIDE\nPIECE");
        random.setPuzzles("PIE");
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        searchTask.run(); // display solution
        String solution = dummyView.getSolution();
        
        assertThat("solution", solution, is("PIECE"));
    }
    
    private void setUpWordList(String words) {
        controller.readWordList(new StringReader(words));
    }
}
