package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.ultraghost.DummyView.Focus;

public class ControllerTest {
    private Controller controller;
    private DummyRandom random;
    private Puzzle startPuzzle;
    private DummyView view;
    private Student student;
    private Student student2;
    private ComputerStudent computerStudent;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        random = new DummyRandom();
        random.setPuzzles("AAA", "AAA", "AAA");
        view = new DummyView();
        student = new Student("Student");
        student2 = new Student("Student 2");
        computerStudent = new ComputerStudent();
        controller = new Controller();
        WordList wordList = new WordList();
        wordList.read(new StringReader("ROPE\nPIECE\nPIPE"));
        controller.setWordList(wordList);
        controller.setRandom(random);
        controller.setView(view);
        controller.addStudent(student);
        controller.addStudent(student2);
        startPuzzle = new Puzzle("RPE", student, wordList);
        view.setPuzzle(startPuzzle);
    }
    
    @Test
    public void startPuzzle() {
        String expectedLetters = "PIE";
        random.setPuzzles(expectedLetters);
        
        controller.start();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat(
                "puzzle", 
                puzzle, 
                not(anyOf(sameInstance(startPuzzle), nullValue())));
        assertThat("letters", puzzle.getLetters(), is(expectedLetters));
    }
    
    @Test
    public void searchTask() {
        String expectedPuzzle = "PIE";
        random.setPuzzles(expectedPuzzle);
        
        controller.start();
        
        Task searchTask = view.getSearchTask();
        assertThat("search task", searchTask, notNullValue());
    }
    
    @Test
    public void startFirstStudent() {
        random.setStartingStudent(0);
        Student expectedStudent = student;
        
        controller.start();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void startOtherStudent() {
        random.setStartingStudent(1);
        Student expectedStudent = student2;
        
        controller.start();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void startComputerStudent() {
        controller.clearStudents();
        controller.addStudent(computerStudent);
        controller.addStudent(student);
        random.setStartingStudent(0);
        Student expectedStudent = computerStudent;
        
        controller.start();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Thinking));
        String letters = computerStudent.getCurrentPuzzle();
        assertThat("letters", letters, is(puzzle.getLetters()));
    }
    
    @Test
    public void startSecondPuzzle() {
        random.setStartingStudent(0);
        Student expectedStudent = student2;
        
        controller.start();
        controller.start();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
    }
    
    @Test
    public void startSecondPuzzleWrapAround() {
        random.setStartingStudent(1);
        Student expectedStudent = student;
        
        controller.start();
        controller.start();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
    }
    
    @Test
    public void solveWithHumanOwnerAgainstHuman() {
        startPuzzle.setSolution("");

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Response));
    }
    
    @Test
    public void solveWithHumanOwnerAgainstComputer() {
        controller.clearStudents();
        controller.addStudent(computerStudent);
        controller.addStudent(student);
        
        startPuzzle.setSolution("");

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
        assertThat("response", startPuzzle.getResponse(), is(""));
        assertThat("refresh count", view.getRefreshCount(), is(2));
    }
    
    @Test
    public void respond() {
        startPuzzle.setSolution("");
        startPuzzle.setResponse("rope");
        
        controller.respond();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
        assertThat("result", startPuzzle.getResult(), is(WordResult.WORD_FOUND));
        assertThat("refresh count", view.getRefreshCount(), is(2));
    }
}
