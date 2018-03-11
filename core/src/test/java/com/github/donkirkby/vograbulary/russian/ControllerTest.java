package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class ControllerTest {
    private Controller controller;
    private RussianDollsScreen screen;
    
    @Before
    public void setUp() {
        screen = mock(RussianDollsScreen.class);
        WordList wordList = new WordList();
        wordList.read(Arrays.asList("uncomfortable"));
        controller = new Controller();
        controller.setScreen(screen);
        controller.setWordList(wordList);
    }
    
    @Test
    public void loadSinglePuzzle() {
        String expectedClue = "Puzzle number one.";
        
        controller.loadPuzzles(Arrays.asList(expectedClue));

        Puzzle puzzle = capturePuzzle();
        assertThat("puzzle", puzzle.getClue(), is(expectedClue));
    }
    
    @Test
    public void loadMultiplePuzzles() {
        String expectedClue1 = "Puzzle number one.";
        String expectedClue2 = "Another nice puzzle.";
        
        controller.loadPuzzles(Arrays.asList(expectedClue1, expectedClue2));
        Puzzle puzzle1 = capturePuzzle();
        controller.next();
        List<Puzzle> allPuzzles = captureAllPuzzles();
        
        assertThat("puzzle 1", puzzle1.getClue(), is(expectedClue1));
        assertThat("puzzle count", allPuzzles.size(), is(2));
        assertThat("puzzle 2", allPuzzles.get(1).getClue(), is(expectedClue2));
    }
    
    @Test
    public void goBack() {
        String expectedClue1 = "Puzzle number one.";
        String expectedClue2 = "Another nice puzzle.";
        
        controller.loadPuzzles(Arrays.asList(expectedClue1, expectedClue2));
        controller.next();
        
        controller.back();
        List<Puzzle> allPuzzles = captureAllPuzzles();
        
        assertThat("puzzle count", allPuzzles.size(), is(3));
        assertThat("puzzle 2", allPuzzles.get(2).getClue(), is(expectedClue1));
    }
    
    @Test
    public void solve() {
        Puzzle puzzle = new Puzzle("unable comfort");
        when(screen.getPuzzle()).thenReturn(puzzle);
        puzzle.setTargetWord(0);
        puzzle.setTargetCharacter(2);
        assertThat("is solved before", puzzle.isSolved(), is(false));
        
        controller.solve();
        
        assertThat("is solved", puzzle.isSolved(), is(true));
    }
    
    @Test
    public void solveIncorrectly() {
        Puzzle puzzle = new Puzzle("unable comfort");
        when(screen.getPuzzle()).thenReturn(puzzle);
        puzzle.setTargetWord(1);
        puzzle.setTargetCharacter(2);
        
        controller.solve();
        
        assertThat("is solved", puzzle.isSolved(), is(false));
    }
    
    @Test
    public void solveWithoutTarget() {
        Puzzle puzzle = new Puzzle("unable comfort");
        when(screen.getPuzzle()).thenReturn(puzzle);
        
        controller.solve();
        
        assertThat("is solved", puzzle.isSolved(), is(false));
    }
    
    @Test
    public void adjustScore() {
        Puzzle puzzle = new Puzzle("unable comfort");
        when(screen.getPuzzle()).thenReturn(puzzle);
        int seconds = 10;
        
        String startScore = puzzle.getScoreDisplay();
        String adjustedScore = controller.adjustScore(seconds);
        
        assertThat("start score", startScore, is("100"));
        assertThat("adjusted score", adjustedScore, is("50"));
    }
    
    @Test
    public void nextSetsPrevious() {
        controller.loadPuzzles(Arrays.asList(
                "unable comfort",
                "something else"));
        Puzzle puzzle = capturePuzzle();
        when(screen.getPuzzle()).thenReturn(puzzle);
        int seconds = 10;
        
        String adjustedScore = controller.adjustScore(seconds);
        puzzle.setTargetWord(0);
        puzzle.setTargetCharacter(2);
        controller.solve();
        
        controller.next();
        Puzzle puzzle2 = captureAllPuzzles().get(1);
        String totalScore = puzzle2.getTotalScoreDisplay();
        
        assertThat("total score", totalScore, is(adjustedScore));
    }

    private Puzzle capturePuzzle() {
        ArgumentCaptor<Puzzle> captor = ArgumentCaptor.forClass(Puzzle.class);
        verify(screen).setPuzzle(captor.capture());
        return captor.getValue();
    }

    private List<Puzzle> captureAllPuzzles() {
        ArgumentCaptor<Puzzle> captor = ArgumentCaptor.forClass(Puzzle.class);
        verify(screen, atLeastOnce()).setPuzzle(captor.capture());
        return captor.getAllValues();
    }
}
