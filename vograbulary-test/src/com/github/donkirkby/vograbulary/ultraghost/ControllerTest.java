package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
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
import com.github.donkirkby.vograbulary.VograbularyPreferences;
import com.github.donkirkby.vograbulary.core.ultraghost.WordList;
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
    private VograbularyPreferences preferences;
    
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
        
        preferences = mock(VograbularyPreferences.class);
        when(preferences.getComputerStudentVocabularySize()).thenReturn(
                wordList.size());
        student = new Student("Student");
        student2 = new Student("Student 2");
        computerStudent = new ComputerStudent(preferences);
        controller = new Controller();
        controller.setWordList(wordList);
        controller.setRandom(random);
        controller.setView(view);
        startPuzzle = new Puzzle("RPE", student, wordList);
        createMatch(student, student2);
        controller.watchPuzzle(startPuzzle);
    }

    private void createMatch(Student... students) {
        Match match = new Match(MATCH_SCORE, students);
        match.setPuzzle(startPuzzle);
        match.setRandom(random);
        controller.clearStudents();
        view.setMatch(match);
        for (Student student : students) {
            controller.addStudent(student);
            student.setMatch(match);
        }
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
        controller.start();
        
        Task searchTask = view.getSearchTask();
        assertThat("search task", searchTask, notNullValue());
    }
    
    @Test
    public void cancelMatch() {
        controller.start();
        Task searchTask = view.getSearchTask();
        
        boolean isScheduledBefore = searchTask.isScheduled();
        controller.cancelMatch();
        boolean isScheduledAfter = searchTask.isScheduled();
        
        assertThat("is scheduled before cancel", isScheduledBefore, is(true));
        assertThat("is scheduled after cancel", isScheduledAfter, is(false));
    }
    
    @Test
    public void cancelMatchBeforeStart() {
        // This does nothing, but we should check that it doesn't throw an
        // exception.
        controller.cancelMatch();
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
        createMatch(computerStudent, student);
        random.setStartingStudent(0);
        Student expectedStudent = computerStudent;
        
        controller.start();
        
        Puzzle puzzle = view.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Thinking));
        assertThat("letters", computerStudent.getCurrentPuzzle(), is(puzzle));
    }
    
    @Test
    public void computerStudentSolve() {
        random.setPuzzles("RPE");
        computerStudent.setMaxSearchBatchCount(1);
        createMatch(computerStudent, student);
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
        createMatch(computerStudent, student);
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
    public void solutionNotAMatch() {
        startPuzzle.setSolution("pipe");
        int startRefreshCount = view.getRefreshCount();

        controller.solve();
        
        WordResult result = startPuzzle.getResult();
        assertThat("result", result, is(WordResult.NOT_A_MATCH));
        assertThat(
                "refresh count", 
                view.getRefreshCount(), 
                is(startRefreshCount+1));
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solutionNotAWord() {
        startPuzzle.setSolution("pixe");

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solutionTooShort() {
        startPuzzle.setMinimumWordLength(5);
        startPuzzle.setSolution("rope");

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solutionTooSoon() {
        startPuzzle.setPreviousWord("ROPE");
        startPuzzle.setSolution("rope");

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solveWithHumanOwnerAgainstComputer() {
        createMatch(student, computerStudent);
        
        startPuzzle.setSolution("");
        int startRefreshCount = view.getRefreshCount();

        controller.solve();
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
        assertThat("response", startPuzzle.getResponse(), is(""));
        assertThat(
                "refresh count", 
                view.getRefreshCount(), 
                greaterThan(startRefreshCount));
        assertThat("score", student.getScore(), is(1));
    }
    
    @Test
    public void noResponseFromActiveStudent() {
        createMatch(computerStudent, student);
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
        int startRefreshCount = view.getRefreshCount();
        startPuzzle.setResponse("rope");
        
        Focus focus = view.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
        assertThat("result", startPuzzle.getResult(), is(WordResult.WORD_FOUND));
        assertThat("refresh count", view.getRefreshCount(), greaterThan(startRefreshCount));
    }
    
    @Test
    public void summary() {
        startPuzzle.setSolution("");
        startPuzzle.setResponse("rope");
        
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
    
    @Test
    public void hint() {
        startPuzzle.setSolution("");
        startPuzzle.setResponse("");
        
        assertThat("hint", startPuzzle.getHint(), is("hint: ROPE"));
    }
    
    @Test
    public void noHint() {
        startPuzzle.setSolution("rope");
        startPuzzle.setResponse("");
        
        assertThat("hint", startPuzzle.getHint(), nullValue());
    }
}
