package com.github.donkirkby.vograbulary.bacronyms;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class ControllerTest {
    
    private Controller controller;
    private BacronymsScreen screen;

    @Before
    public void setUp() {
        WordList wordList = new WordList();
        wordList.read(Arrays.asList("alpha", "beta", "ateb", "gamma"));
        screen = new DummyScreen();
        controller = new Controller();
        controller.setWordList(wordList);
        controller.setScreen(screen);
        controller.loadPuzzles(Arrays.asList(
                "alpha ateb gamma",
                "one two three"));
    }
    
    @Test
    public void next() {
        controller.next();
        
        Puzzle puzzle = screen.getPuzzle();
        String word = puzzle.getWord(1);
        assertThat("word", word, is("ATEB"));
    }
    
    @Test
    public void secondPuzzle() {
        controller.next();
        
        controller.next();
        
        Puzzle nextPuzzle = screen.getPuzzle();
        String word = nextPuzzle.getWord(1);
        assertThat("word", word, is("TWO"));
    }
    
    @Test
    public void startState() {
        assertThat("state", screen.getState(), is(BacronymsScreen.State.START));
    }
    
    @Test
    public void nextState() {
        controller.next();
        
        assertThat("state", screen.getState(), is(BacronymsScreen.State.NEW));
    }
    
    @Test
    public void solve() {
        controller.next();
        
        Puzzle puzzle = screen.getPuzzle();
        puzzle.setSelectedIndex(1);
        controller.solve();
        
        assertThat("state", screen.getState(), is(BacronymsScreen.State.SOLVED));
    }
    
    @Test
    public void solveBadly() {
        controller.next();
        
        Puzzle puzzle = screen.getPuzzle();
        puzzle.setSelectedIndex(0);
        controller.solve();
        
        assertThat("state", screen.getState(), is(BacronymsScreen.State.WRONG));
    }
}
