package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.github.donkirkby.vograbulary.SerializableTools;
import com.github.donkirkby.vograbulary.VograbularyPreferences;

public class MatchTest {
    private static final int MATCH_SCORE = 3;
    private Match match;
    private Student student;
    private ComputerStudent computer;
    private DummyRandom random;
    private WordList wordList;
    
    @Before
    public void setUp() {
        student = new Student("Student");
        computer = new ComputerStudent(mock(VograbularyPreferences.class));
        wordList = new WordList();
        wordList.read(Arrays.asList("PIECE", "PIPE"));
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
    public void secondPuzzleNotPaused() {
        random.setPuzzles("ABC", "XYZ");
        match.createPuzzle(wordList);
        Puzzle puzzle = match.createPuzzle(wordList);
        
        assertThat("isPaused", puzzle.isPaused(), is(false));
    }
    
    @Test
    public void secondPuzzlePaused() {
        random.setPuzzles("ABC", "XYZ");
        Puzzle puzzle1 = match.createPuzzle(wordList);
        puzzle1.setPaused(true);
        Puzzle puzzle2 = match.createPuzzle(wordList);
        
        assertThat("isPaused", puzzle2.isPaused(), is(true));
    }
    
    @Test
    public void wordLength() {
        random.setPuzzles("PIE");
        int expected = 5;
        match.setMinimumWordLength(expected);
        
        Puzzle puzzle = match.createPuzzle(wordList);
        
        assertThat("word length", puzzle.getMinimumWordLength(), is(expected));
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
    
    @Test
    public void winnerNotSetForLowScores() {
        student.addScore(MATCH_SCORE - 1);
        computer.addScore(MATCH_SCORE - 2);
        
        Student winner = match.getWinner();
        
        assertThat("winner", winner, nullValue());
    }
    
    @Test
    public void winnerSet() {
        student.addScore(MATCH_SCORE);
        computer.addScore(MATCH_SCORE - 1);
        
        Student winner = match.getWinner();
        
        assertThat("winner", winner, is(student));
    }
    
    @Test
    public void winnerSecondStudent() {
        student.addScore(MATCH_SCORE - 1);
        computer.addScore(MATCH_SCORE);
        
        Student winner = match.getWinner();
        
        assertThat("winner", winner, is((Student)computer));
    }
    
    @Test
    public void winnerNotSetOnUnevenTurnCount() {
        student.addScore(MATCH_SCORE);
        
        Student winner = match.getWinner();
        
        assertThat("winner", winner, nullValue());
    }
    
    @Test
    public void winnerNotSetOnTie() {
        student.addScore(MATCH_SCORE);
        computer.addScore(MATCH_SCORE);
        
        Student winner = match.getWinner();
        
        assertThat("winner", winner, nullValue());
    }
    
    @Test
    public void serialize() throws Exception {
        byte[] bytes = SerializableTools.serialize(match);
        Match match2 = SerializableTools.deserialize(bytes, Match.class);
        
        assertThat(match2.getMatchScore(), is(MATCH_SCORE));
        assertThat(match2.getStudents().get(0).getName(), is(student.getName()));
    }
}
