package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class ControllerTest {
    private Controller controller;
    private RussianDollsScreen screen;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        screen = mock(RussianDollsScreen.class);
        WordList wordList = new WordList();
        wordList.read(new StringReader("uncomfortable"));
        controller = new Controller();
        controller.setScreen(screen);
        controller.setWordList(wordList);
    }
    
    @Test
    public void loadSinglePuzzle() {
        String expectedClue = "Puzzle number one.";
        Reader reader = new StringReader(expectedClue);
        
        controller.loadPuzzles(reader);

        Puzzle puzzle = capturePuzzle();
        assertThat("puzzle", puzzle.getClue(), is(expectedClue));
    }
    
    @Test
    public void loadMultiplePuzzles() {
        String expectedClue1 = "Puzzle number one.";
        String expectedClue2 = "Another nice puzzle.";
        String input = expectedClue1 + "\n" + expectedClue2;
        Reader reader = new StringReader(input);
        
        controller.loadPuzzles(reader);
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
        String input = expectedClue1 + "\n" + expectedClue2;
        Reader reader = new StringReader(input);
        controller.loadPuzzles(reader);
        controller.next();
        
        controller.back();
        List<Puzzle> allPuzzles = captureAllPuzzles();
        
        assertThat("puzzle count", allPuzzles.size(), is(3));
        assertThat("puzzle 2", allPuzzles.get(2).getClue(), is(expectedClue1));
    }
    
    @Test
    public void readerIsClosed() throws Exception {
        String expectedPuzzle = "Puzzle number one.";
        Reader reader = new StringReader(expectedPuzzle);
        
        controller.loadPuzzles(reader);

        thrown.expect(IOException.class);
        thrown.expectMessage("Stream closed");
        reader.read();
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
        String expectedClue1 = "unable comfort";
        String expectedClue2 = "something else";
        String input = expectedClue1 + "\n" + expectedClue2;
        Reader reader = new StringReader(input);
        
        controller.loadPuzzles(reader);
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
