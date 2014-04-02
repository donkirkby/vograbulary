package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class MatchTest {
    private static final int MATCH_SCORE = 1;
    private Match match;
    private Student student;
    private ComputerStudent computer;
    private DummyRandom random;
    private WordList wordList;
    
    @Before
    public void setUp() {
        student = new Student("Student");
        computer = new ComputerStudent();
        wordList = new WordList();
        wordList.read(new StringReader("PIECE\nPIPE"));
        match = new Match(MATCH_SCORE, student, computer);
        random = new DummyRandom();
        match.setRandom(random);
    }
    
    @Test
    public void firstPuzzle() {
        String expectedLetters = "PIE";
        random.setPuzzles(expectedLetters);
        random.setStartingStudent(0);
        
        Puzzle puzzle = match.createPuzzle(wordList);
        
        assertThat("letters", puzzle.getLetters(), is(expectedLetters));
        assertThat("student", puzzle.getOwner(), is(student));
        assertThat("puzzle", match.getPuzzle(), is(puzzle));
    }
    
    @Test
    public void firstPuzzleForOtherStudent() {
        String expectedLetters = "PIE";
        random.setPuzzles(expectedLetters);
        random.setStartingStudent(1);
        Student expectedStudent = computer;
        
        Puzzle puzzle = match.createPuzzle(wordList);
        
        assertThat("puzzle", puzzle.getLetters(), is(expectedLetters));
        assertThat("student", puzzle.getOwner(), is(expectedStudent));
    }
    
    @Test
    public void secondPuzzle() {
        String expectedLetters = "PIE";
        random.setPuzzles("XRZ", expectedLetters);
        random.setStartingStudent(0);
        Student expectedStudent = computer; // for second puzzle
        
        match.createPuzzle(wordList);
        Puzzle puzzle = match.createPuzzle(wordList);
        
        assertThat("puzzle", puzzle.getLetters(), is(expectedLetters));
        assertThat("student", puzzle.getOwner(), is(expectedStudent));
    }
    
    @Test
    public void summary() {
        student.addScore(5);
        computer.addScore(9);
        String expectedSummary = "Student 5\nComputer 9\n";
        
        String summary = match.getSummary();
        
        assertThat("summary", summary, is(expectedSummary));
    }
    
    @Test
    public void summaryOrder() {
        random.setStartingStudent(1);
        String expectedSummary = "Computer 0\nStudent 0\n";
        
        String summary = match.getSummary();
        
        assertThat("summary", summary, is(expectedSummary));
    }
}
