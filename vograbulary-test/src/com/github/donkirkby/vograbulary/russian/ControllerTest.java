package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ControllerTest {
    private Controller controller;
    private RussianDollsScreen screen;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        screen = mock(RussianDollsScreen.class);
        controller = new Controller();
        controller.setScreen(screen);
    }
    
    @Test
    public void loadSinglePuzzle() {
        String expectedPuzzle = "Puzzle number one.";
        Reader reader = new StringReader(expectedPuzzle);
        
        controller.loadPuzzles(reader);

        verify(screen).setPuzzle(expectedPuzzle);
    }
    
    @Test
    public void loadMultiplePuzzles() {
        String expectedPuzzle1 = "Puzzle number one.";
        String expectedPuzzle2 = "Second puzzle.";
        String input = expectedPuzzle1 + "\n" + expectedPuzzle2;
        Reader reader = new StringReader(input);
        
        controller.loadPuzzles(reader);
        verify(screen).setPuzzle(expectedPuzzle1);
        verifyNoMoreInteractions(screen);
        controller.next();
        verify(screen).setPuzzle(expectedPuzzle2);
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
    
}
