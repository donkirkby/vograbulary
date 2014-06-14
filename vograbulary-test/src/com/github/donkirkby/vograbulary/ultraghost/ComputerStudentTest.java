package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import com.github.donkirkby.vograbulary.VograbularyPreferences;

public class ComputerStudentTest {
    private ComputerStudent student;
    private WordList wordList;
    private FocusField focus;
    private VograbularyPreferences preferences;
    
    private enum FocusField {Solution, Challenge};
    
    @Before
    public void setUp() {
        wordList = new WordList();
        wordList.read(new StringReader("PRICE\nPIECE\nPIPE"));
        preferences = mock(VograbularyPreferences.class);
        when(preferences.getComputerStudentVocabularySize()).thenReturn(
                Integer.MAX_VALUE);
        student = new ComputerStudent(preferences);
        student.setWordList(wordList);
        student.setListener(new Student.StudentListener() {
            @Override
            public void showThinking() {
                focus = null;
            }
            
            @Override
            public void askForSolution() {
                focus = FocusField.Solution;
            }
            
            @Override
            public void askForChallenge() {
                focus = FocusField.Challenge;
            }
            
            @Override
            public void refreshPuzzle() {
            }
        });
    }
    
    @Test
    public void noSolutionFound() {
        int batchSize = 100;
        assertThat("word count", wordList.size(), lessThan(batchSize));
        student.setSearchBatchSize(batchSize);
        Puzzle puzzle = new Puzzle("AXR", student, wordList);
        student.startSolving(puzzle);
        
        student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is(""));
    }
    
    @Test
    public void noSolutionFoundWhenNotActiveStudent() {
        int batchSize = 100;
        assertThat("word count", wordList.size(), lessThan(batchSize));
        student.setSearchBatchSize(batchSize);
        Puzzle puzzle = new Puzzle("AXR", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is(Puzzle.NOT_SET));
        assertThat("is finished", isFinished, is(true));
    }
    
    @Test
    public void maxBatchCount() {
        student.setMaxSearchBatchCount(1);
        
        Puzzle puzzle = new Puzzle("AXR", student, wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(true));
    }
    
    @Test
    public void ignoreWorseWord() {
        wordList = new WordList();
        wordList.read(new StringReader("PIECE\nPRICE"));
        student.setWordList(wordList);
        student.setMaxSearchBatchCount(1);
        
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        student.startSolving(puzzle);
        
        student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is("PIECE"));
    }
    
    @Test
    public void ignoreShortWord() {
        wordList = new WordList();
        wordList.read(new StringReader("PIECE\nPIPE"));
        student.setWordList(wordList);
        student.setMaxSearchBatchCount(1);
        wordList.setMinimumWordLength(5);
        
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        student.startSolving(puzzle);
        
        student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is("PIECE"));
    }
    
    @Test
    public void vocabularySizeNoMatch() {
        when(preferences.getComputerStudentVocabularySize()).thenReturn(1);
        
        Puzzle puzzle = new Puzzle("AXR", student, wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(true));
    }
    
    @Test
    public void prepareResponse() {
        when(preferences.getComputerStudentVocabularySize()).thenReturn(1);
        Puzzle puzzle = new Puzzle("PIE", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        student.runSearchBatch();
        puzzle.setSolution("");
        
        student.prepareChallenge();
        
        assertThat("response", puzzle.getResponse(), is("PRICE"));
    }
    
    @Test
    public void vocabularySizeMatch() {
        when(preferences.getComputerStudentVocabularySize()).thenReturn(1);
        
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        student.startSolving(puzzle);
        
        student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is("PRICE"));
    }
    
    @Test
    public void batchSize() {
        int vocabularySize = 100;
        int maxSearchBatchCount = 20;
        int expectedBatchSize = vocabularySize / maxSearchBatchCount;
        
        when(preferences.getComputerStudentVocabularySize()).thenReturn(
                vocabularySize);
        student.setMaxSearchBatchCount(maxSearchBatchCount);
        
        int batchSize = student.getSearchBatchSize();
        assertThat("batch size", batchSize, is(expectedBatchSize));
    }
    
    @Test
    public void maxBatchCountInactiveStudent() {
        when(preferences.getComputerStudentVocabularySize()).thenReturn(2);
        student.setMaxSearchBatchCount(1);
        
        Puzzle puzzle = new Puzzle("AXR", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(false));
    }
    
    @Test
    public void createSearchTaskCancelsAfterLastWord() {
        assertThat("word count", wordList.size(), is(3));
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        student.startSolving(puzzle);
        String expectedSolution = "PIPE";

        student.runSearchBatch();
        boolean isCompleteAfterSecondLast = student.runSearchBatch();
        boolean isCompleteAfterLast = student.runSearchBatch();
        
        assertThat(
                "is complete before last word", 
                isCompleteAfterSecondLast, 
                is(false));
        assertThat(
                "is complete after last word",
                isCompleteAfterLast,
                is(true));
        assertThat("solution", puzzle.getSolution(), is(expectedSolution));
    }
    
    @Test
    public void startThinkingWhenActive() {
        focus = FocusField.Solution;
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        student.startSolving(puzzle);
        
        assertThat("focus", focus, nullValue());
    }
    
    @Test
    public void startThinkingWhenInactive() {
        focus = FocusField.Solution;
        Puzzle puzzle = new Puzzle("PIE", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        
        assertThat("focus", focus, is(FocusField.Solution));
    }
}
