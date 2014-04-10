package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.Configuration;
import com.github.donkirkby.vograbulary.ultraghost.DummyView.Focus;

public class ControllerTest {
    private static final int MATCH_SCORE = 10;
    private Controller controller;
    private DummyRandom random;
    private Puzzle startPuzzle;
    private DummyView view;
    private Student student;
    private Student student2;
    private ComputerStudent computerStudent;
    private Configuration configuration;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        Gdx.app = mock(Application.class);
        random = new DummyRandom();
        random.setPuzzles("AAA", "AAA", "AAA");
        view = new DummyView();
        WordList wordList = new WordList();
        wordList.read(new StringReader("ROPE\nPIECE\nPIPE"));
        configuration = new Configuration();
        configuration.setVocabularySize(wordList.size());
        student = new Student("Student");
        student2 = new Student("Student 2");
        computerStudent = new ComputerStudent(configuration);
        controller = new Controller();
        controller.setWordList(wordList);
        controller.setRandom(random);
        controller.setView(view);
        controller.addStudent(student);
        controller.addStudent(student2);
        startPuzzle = new Puzzle("RPE", student, wordList);
        Match match = new Match(MATCH_SCORE, student, student2);
        match.setPuzzle(startPuzzle);
        view.setMatch(match);
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
    public void computerStudentSolve() {
        random.setPuzzles("RPE");
        computerStudent.setMaxSearchBatchCount(1);
        controller.clearStudents();
        controller.addStudent(computerStudent);
        controller.addStudent(student);
        random.setStartingStudent(0);
        controller.start();
        Task searchTask = view.getSearchTask();
        int previousRefreshCount = view.getRefreshCount();

        searchTask.run();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat("solution", puzzle.getSolution(), is("ROPE"));
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Response));
        assertThat(
                "refresh count", 
                view.getRefreshCount(), 
                is(previousRefreshCount+1));
        assertThat("isScheduled", searchTask.isScheduled(), is(false));
    }
    
    @Test
    public void computerStudentSolveAfter2Batches() {
        random.setPuzzles("PIE");
        computerStudent.setMaxSearchBatchCount(2);
        controller.clearStudents();
        controller.addStudent(computerStudent);
        controller.addStudent(student);
        random.setStartingStudent(0);
        controller.start();
        Task searchTask = view.getSearchTask();
        Puzzle puzzle = view.getPuzzle();

        searchTask.run();
        String solutionAfterSearch1 = puzzle.getSolution();
        boolean isScheduledAfterSearch1 = searchTask.isScheduled();
        searchTask.run();
        String solutionAfterSearch2 = puzzle.getSolution();
        boolean isScheduledAfterSearch2 = searchTask.isScheduled();
        
        assertThat("solution1", solutionAfterSearch1, nullValue());
        assertThat("solution2", solutionAfterSearch2, is("PIECE"));
        assertThat("is scheduled 1", isScheduledAfterSearch1, is(true));
        assertThat("is scheduled 2", isScheduledAfterSearch2, is(false));
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
        controller.addStudent(student);
        controller.addStudent(computerStudent);
        int startRefreshCount = view.getRefreshCount();
        
        startPuzzle.setSolution("");

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
        assertThat("response", startPuzzle.getResponse(), is(""));
        assertThat(
                "refresh count", 
                view.getRefreshCount(), 
                is(startRefreshCount+1));
        assertThat("score", student.getScore(), is(1));
    }
    
    @Test
    public void noResponseFromActiveStudent() {
        controller.clearStudents();
        controller.addStudent(computerStudent);
        controller.addStudent(student);
        random.setStartingStudent(1);
        startPuzzle.setSolution("");

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
    }
    
    @Test
    public void cancelSearchTask() {
        controller.clearStudents();
        controller.addStudent(student);
        controller.addStudent(computerStudent);
        random.setPuzzles("RPE");
        controller.start();
        Puzzle puzzle = view.getPuzzle();
        
        puzzle.setSolution("");

        controller.solve();
        
        assertThat("isScheduled", view.getSearchTask().isScheduled(), is(false));
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
    
    @Test
    public void summary() {
        startPuzzle.setSolution("");
        startPuzzle.setResponse("rope");
        
        controller.respond();
        
        assertThat("score", student.getScore(), is(-1));
    }
    
    @Test
    public void newMatch() {
        controller.start();
        Match match1 = controller.getMatch();
        
        controller.clearStudents();
        controller.addStudent(student);
        controller.addStudent(student2);
        controller.start();
        
        Match match2 = controller.getMatch();
        
        assertThat("match1", match1, notNullValue());
        assertThat("match2", match2, allOf(notNullValue(), not(match1)));
    }
    
    @Test
    public void changeWordList() {
        WordList newWordList = new WordList();
        newWordList.read(new StringReader("NEW\nWORD\nLIST"));
        
        controller.setWordList(newWordList);
        
        assertThat("student's word list", student.getWordList(), is(newWordList));
    }
}
