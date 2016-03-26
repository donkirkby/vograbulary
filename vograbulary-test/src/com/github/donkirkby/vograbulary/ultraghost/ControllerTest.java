package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.donkirkby.vograbulary.Scheduler;
import com.github.donkirkby.vograbulary.VograbularyPreferences;
import com.github.donkirkby.vograbulary.ultraghost.DummyScreen.Focus;

public class ControllerTest {
    private static final int MATCH_SCORE = 10;
    private Controller controller;
    private DummyRandom random;
    private Puzzle startPuzzle;
    private DummyScreen screen;
    private Student student;
    private Student student2;
    private ComputerStudent computerStudent;
    private VograbularyPreferences preferences;
    private Runnable scoreTask;
    private Runnable searchTask;
    private Scheduler scheduler = new Scheduler() {
        @Override
        public void scheduleRepeating(Runnable task, int periodMilliseconds) {
            if (scoreTask == null) {
                scoreTask = task;
            }
            else {
                searchTask = task;
            }
        }
        
        @Override
        public void cancel(Runnable task) {
            if (scoreTask == task) {
                scoreTask = null;
            }
            else {
                searchTask = null;
            }
        }
    };
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        random = new DummyRandom();
        random.setPuzzles("AAA", "AAA", "AAA");
        screen = new DummyScreen();
        WordList wordList = new WordList();
        wordList.read(Arrays.asList("ROPE", "PIECE", "PIPE"));
        
        preferences = mock(VograbularyPreferences.class);
        when(preferences.getComputerStudentVocabularySize()).thenReturn(
                wordList.size());
        student = new Student("Student");
        student2 = new Student("Student 2");
        computerStudent = new ComputerStudent(preferences);
        controller = new Controller();
        scoreTask = null;
        searchTask = null;
        controller.setPreferences(preferences);
        controller.setScheduler(scheduler);
        controller.setWordList(wordList);
        controller.setRandom(random);
        controller.setScreen(screen);
        startPuzzle = new Puzzle("RPE", student, wordList);
        createMatch(student, student2);
        controller.watchPuzzle(startPuzzle);
    }

    private void createMatch(Student... students) {
        Match match = new Match(MATCH_SCORE, students);
        match.setPuzzle(startPuzzle);
        match.setRandom(random);
        controller.clearStudents();
        for (Student student : students) {
            controller.addStudent(student);
            student.setMatch(match);
        }
        screen.setMatch(match);
    }
    
    @Test
    public void startPuzzle() {
        String expectedLetters = "PIE";
        random.setPuzzles(expectedLetters);
        
        controller.start();
        
        Puzzle puzzle = screen.getPuzzle();
        assertThat(
                "puzzle", 
                puzzle, 
                not(anyOf(sameInstance(startPuzzle), nullValue())));
        assertThat("letters", puzzle.getLetters(), is(expectedLetters));
    }
    
    @Test
    public void searchTask() {
        controller.start();
        
        assertThat("search task", searchTask, notNullValue());
    }
    
    @Test
    public void cancelMatch() {
        controller.start();

        boolean isScheduledBefore = searchTask != null;
        controller.cancelMatch();
        boolean isScheduledAfter = searchTask != null;
       
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
        
        Puzzle puzzle = screen.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void startOtherStudent() {
        random.setStartingStudent(1);
        Student expectedStudent = student2;
        
        controller.start();
        
        Puzzle puzzle = screen.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void startComputerStudent() {
        createMatch(computerStudent, student);
        random.setStartingStudent(0);
        Student expectedStudent = computerStudent;
        
        controller.start();
        
        Puzzle puzzle = screen.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
        Focus focus = screen.getCurrentFocus();
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
        int previousRefreshCount = screen.getRefreshCount();

        searchTask.run();
        
        Puzzle puzzle = screen.getPuzzle();
        assertThat("solution", puzzle.getSolution(), is("ROPE"));
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Response));
        assertThat(
                "refresh count", 
                screen.getRefreshCount(), 
                is(previousRefreshCount+1));
        assertThat("isScheduled", searchTask, nullValue());
    }
    
    @Test
    public void computerStudentSolveAfter2Batches() {
        random.setPuzzles("PIE");
        computerStudent.setMaxSearchBatchCount(2);
        createMatch(computerStudent, student);
        random.setStartingStudent(0);
        controller.start();
        Puzzle puzzle = screen.getPuzzle();

        searchTask.run();
        String solutionAfterSearch1 = puzzle.getSolution();
        boolean isScheduledAfterSearch1 = searchTask != null;
        searchTask.run();
        String solutionAfterSearch2 = puzzle.getSolution();
        boolean isScheduledAfterSearch2 = searchTask != null;
        
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
        
        Puzzle puzzle = screen.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
    }
    
    @Test
    public void startSecondPuzzleWrapAround() {
        random.setStartingStudent(1);
        Student expectedStudent = student;
        
        controller.start();
        controller.start();
        
        Puzzle puzzle = screen.getPuzzle();
        assertThat("owner", puzzle.getOwner(), is(expectedStudent));
    }
    
    @Test
    public void solveWithHumanOwnerAgainstHuman() {
        startPuzzle.setSolution("");

        controller.solve();
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Response));
    }
    
    @Test
    public void solutionNotAMatch() {
        startPuzzle.setSolution("pipe");
        int startRefreshCount = screen.getRefreshCount();

        controller.solve();
        
        WordResult result = startPuzzle.getResult();
        assertThat("result", result, is(WordResult.NOT_A_MATCH));
        assertThat(
                "refresh count", 
                screen.getRefreshCount(), 
                is(startRefreshCount+1));
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solutionNotAWord() {
        startPuzzle.setSolution("pixe");

        controller.solve();
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solutionTooShort() {
        startPuzzle.setMinimumWordLength(5);
        startPuzzle.setSolution("rope");

        controller.solve();
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solutionTooSoon() {
        startPuzzle.setPreviousWord("ROPE");
        startPuzzle.setSolution("rope");

        controller.solve();
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Solution));
    }
    
    @Test
    public void solutionTimesOut() {
        startPuzzle.adjustScore(45);
        startPuzzle.setSolution("pixe");

        controller.solve();
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
    }
    
    @Test
    public void solveWithHumanOwnerAgainstComputer() {
        createMatch(student, computerStudent);
        
        startPuzzle.setSolution("");
        int startRefreshCount = screen.getRefreshCount();

        controller.solve();
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
        assertThat("response", startPuzzle.getResponse(), is(""));
        assertThat(
                "refresh count", 
                screen.getRefreshCount(), 
                greaterThan(startRefreshCount));
        assertThat("score", student.getScore(), is(33));
    }
    
    @Test
    public void noResponseFromActiveStudent() {
        createMatch(computerStudent, student);
        random.setStartingStudent(1);
        startPuzzle.setSolution("");

        controller.solve();
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
    }
    
    @Test
    public void cancelSearchTask() {
        controller.clearStudents();
        controller.addStudent(student);
        controller.addStudent(computerStudent);
        random.setPuzzles("RPE");
        controller.start();
        Puzzle puzzle = screen.getPuzzle();
        
        puzzle.setSolution("");

        controller.solve();
        
        assertThat("search task", searchTask, nullValue());
        assertThat("score task", scoreTask, nullValue());
    }
    
    @Test
    public void cancelScoreTask() {
        random.setPuzzles("RPE");
        controller.start();
        Puzzle puzzle = screen.getPuzzle();
        
        puzzle.setSolution("");
        controller.solve();
        puzzle.setResponse("");
        
        assertThat("search task", searchTask, nullValue());
        assertThat("score task", scoreTask, nullValue());
    }
    
    @Test
    public void keepScoreTaskAfterInvalidResponse() {
        random.setPuzzles("RPE");
        controller.start();
        Puzzle puzzle = screen.getPuzzle();
        
        puzzle.setSolution("");
        controller.solve();
        puzzle.setResponse("RICE");
        
        assertThat("score task", scoreTask, notNullValue());
    }
    
    @Test
    public void focusAfterInvalidResponse() {
        random.setPuzzles("RPE");
        controller.start();
        Puzzle puzzle = screen.getPuzzle();
        
        puzzle.setSolution("");
        controller.solve();
        screen.focusSolution(); // just to check that it gets updated.
        puzzle.setResponse("RICE");
        
        assertThat(
                "focus",
                screen.getCurrentFocus(),
                is(DummyScreen.Focus.Response));
    }
    
    @Test
    public void respond() {
        startPuzzle.setSolution("");
        int startRefreshCount = screen.getRefreshCount();
        startPuzzle.setResponse("rope");
        
        Focus focus = screen.getCurrentFocus();
        assertThat("focus", focus, is(Focus.Result));
        assertThat("result", startPuzzle.getResult(), is(WordResult.WORD_FOUND));
        assertThat("refresh count", screen.getRefreshCount(), greaterThan(startRefreshCount));
    }
    
    @Test
    public void summary() {
        startPuzzle.setSolution("");
        startPuzzle.setResponse("rope");
        
        assertThat("score", student.getScore(), is(-33));
    }
    
    @Test
    public void adjustScore() {
        controller.start();
        Puzzle puzzle = screen.getPuzzle();
        
        int seconds = 10;
        int loopCount = seconds * 1000 / Controller.SCORE_MILLISECONDS;
        for (int i = 0; i < loopCount; i++) {
            scoreTask.run();
        }
        
        assertThat("score", puzzle.getScore(WordResult.NOT_IMPROVED), is(98));
        assertThat("refresh count", screen.getScoreRefreshCount(), is(loopCount));
    }
    
    @Test
    public void adjustScoreOnResponseUntilTimeout() {
        random.setPuzzles("RPE");
        controller.start();
        Puzzle puzzle = screen.getPuzzle();
        
        puzzle.setSolution("ROPE");
        controller.solve();
        int seconds = (int) Puzzle.MAX_DELAY/2;
        int loopCount = seconds * 1000 / Controller.SCORE_MILLISECONDS;
        for (int i = 0; i < loopCount; i++) {
            scoreTask.run();
        }
        
        assertThat("score", puzzle.getScore(WordResult.NOT_IMPROVED), is(100));
    }
    
    @Test
    public void newMatch() {
        controller.start();
        Match match1 = controller.getMatch();
        int expectedWordLength = 7;
        when(preferences.getUltraghostMinimumWordLength()).thenReturn(
                expectedWordLength);
        
        controller.clearStudents();
        controller.addStudent(student);
        controller.addStudent(student2);

        controller.start();
        
        Match match2 = controller.getMatch();
        
        assertThat("match1", match1, notNullValue());
        assertThat("match2", match2, allOf(notNullValue(), not(match1)));
        assertThat(
                "word length",
                match2.getPuzzle().getMinimumWordLength(),
                is(expectedWordLength));
    }
    
    @Test
    public void changeWordList() {
        WordList newWordList = new WordList();
        newWordList.read(Arrays.asList("NEW", "WORD", "LIST"));
        
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
        
        assertThat("hint", startPuzzle.getHint(), is("Perfect!"));
    }
    
    @Test
    public void hintAfterInvalid() {
        startPuzzle.setSolution("r");
        float solutionSeconds = 1000;
        startPuzzle.adjustScore(solutionSeconds);
        
        assertThat("hint", startPuzzle.getHint(), is("hint: ROPE"));
    }
    
    @Test
    public void addStudent() {
        controller = new Controller();
        controller.setPreferences(preferences);
        controller.setScreen(new DummyScreen());
        
        controller.addStudent(student);
        controller.addStudent(student2);
        
        assertThat(
                "students",
                controller.getMatch().getStudents(),
                containsInAnyOrder(student, student2));
    }
    
    @Test
    public void addStudentAfterGetMatch() {
        controller = new Controller();
        controller.setPreferences(preferences);
        controller.setScreen(new DummyScreen());
        
        controller.getMatch();
        controller.addStudent(student);
        controller.addStudent(student2);
        
        assertThat(
                "students",
                controller.getMatch().getStudents(),
                containsInAnyOrder(student, student2));
    }
}
