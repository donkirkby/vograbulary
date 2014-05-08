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
        verifyNoMoreInteractions(screen);
        controller.next();
        List<Puzzle> allPuzzles = captureAllPuzzles();
        
        assertThat("puzzle 1", puzzle1.getClue(), is(expectedClue1));
        assertThat("puzzle count", allPuzzles.size(), is(2));
        assertThat("puzzle 2", allPuzzles.get(1).getClue(), is(expectedClue2));
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
