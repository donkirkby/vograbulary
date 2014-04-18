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
    private boolean isSolutionSet = false;
    private String solution;
    private String challenge;
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
        solution = challenge = null;
        student.setListener(new Student.StudentListener() {
            
            @Override
            public void submitSolution(String solution) {
                isSolutionSet = true;
                ComputerStudentTest.this.solution = solution;
            }
            
            @Override
            public void submitChallenge(String challenge, WordResult challengeResult) {
                ComputerStudentTest.this.challenge = challenge;
            }
            
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
        });
    }
    
    @Test
    public void noSolutionFound() {
        int batchSize = 100;
        assertThat("word count", wordList.size(), lessThan(batchSize));
        student.setSearchBatchSize(batchSize);
        boolean isActiveStudent = true;
        student.startSolving("AXR", isActiveStudent);
        
        student.runSearchBatch();
        
        assertThat("is solution submitted", isSolutionSet, is(true));
        assertThat("solution", solution, is(""));
    }
    
    @Test
    public void noSolutionFoundWhenNotActiveStudent() {
        int batchSize = 100;
        assertThat("word count", wordList.size(), lessThan(batchSize));
        student.setSearchBatchSize(batchSize);
        boolean isActiveStudent = false;
        student.startSolving("AXR", isActiveStudent);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("is solution submitted", isSolutionSet, is(false));
        assertThat("is finished", isFinished, is(true));
    }
    
    @Test
    public void maxBatchCount() {
        student.setMaxSearchBatchCount(1);
        
        boolean isActiveStudent = true;
        student.startSolving("AXR", isActiveStudent);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(true));
    }
    
    @Test
    public void ignoreWorseWord() {
        wordList = new WordList();
        wordList.read(new StringReader("PIECE\nPRICE"));
        student.setWordList(wordList);
        student.setMaxSearchBatchCount(1);
        
        boolean isActiveStudent = true;
        student.startSolving("PIE", isActiveStudent);
        
        student.runSearchBatch();
        
        assertThat("solution", solution, is("PIECE"));
    }
    
    @Test
    public void vocabularySizeNoMatch() {
        when(preferences.getComputerStudentVocabularySize()).thenReturn(1);
        
        boolean isActiveStudent = true;
        student.startSolving("AXR", isActiveStudent);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(true));
    }
    
    @Test
    public void prepareChallenge() {
        when(preferences.getComputerStudentVocabularySize()).thenReturn(1);
        boolean isActiveStudent = false;
        student.startSolving("PIE", isActiveStudent);
        student.runSearchBatch();
        String humanSolution = "";
        
        student.prepareChallenge(humanSolution);
        
        assertThat("challenge", challenge, is("PRICE"));
    }
    
    @Test
    public void vocabularySizeMatch() {
        when(preferences.getComputerStudentVocabularySize()).thenReturn(1);
        
        boolean isActiveStudent = true;
        student.startSolving("PIE", isActiveStudent);
        
        student.runSearchBatch();
        
        assertThat("solution", solution, is("PRICE"));
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
        
        boolean isActiveStudent = false;
        student.startSolving("AXR", isActiveStudent);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(false));
    }
    
    @Test
    public void createSearchTaskCancelsAfterLastWord() {
        assertThat("word count", wordList.size(), is(3));
        boolean isActiveStudent = true;
        student.startSolving("PIE", isActiveStudent);
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
        assertThat("solution", solution, is(expectedSolution));
    }
    
    @Test
    public void startThinkingWhenActive() {
        focus = FocusField.Solution;
        boolean isActiveStudent = true;
        student.startSolving("PIE", isActiveStudent);
        
        assertThat("focus", focus, nullValue());
    }
    
    @Test
    public void startThinkingWhenInactive() {
        focus = FocusField.Solution;
        boolean isActiveStudent = false;
        student.startSolving("PIE", isActiveStudent);
        
        assertThat("focus", focus, is(FocusField.Solution));
    }
}
