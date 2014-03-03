package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class ComputerStudentTest {
    private boolean isSolutionSet = false;
    private String solution;
    private ComputerStudent student;
    private WordList wordList;
    private FocusField focus;
    
    private enum FocusField {Solution, Challenge};
    
    @Before
    public void setUp() {
        wordList = new WordList();
        wordList.read(new StringReader("PRICE\nPIECE\nPIPE"));
        student = new ComputerStudent();
        student.setWordList(wordList);
        student.setListener(new Student.StudentListener() {
            
            @Override
            public void submitSolution(String solution) {
                isSolutionSet = true;
                ComputerStudentTest.this.solution = solution;
            }
            
            @Override
            public void submitChallenge(String challenge, WordResult challengeResult) {
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
        assertThat("solution", solution, nullValue());
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
    public void maxBatchCountInactiveStudent() {
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
