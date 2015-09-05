package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.donkirkby.vograbulary.SerializableTools;
import com.github.donkirkby.vograbulary.VograbularyPreferences;

public class ComputerStudentTest {
    private WordList wordList;
    private FocusField focus;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private enum FocusField {Solution, Challenge};
    
    @Before
    public void setUp() {
        wordList = new WordList();
        wordList.read(Arrays.asList("PRICE", "PIECE", "PIPE"));
    }

    private ComputerStudent createStudentWithVocabularySize(int vocabularySize) {
        VograbularyPreferences preferences = mock(VograbularyPreferences.class);
        when(preferences.getComputerStudentVocabularySize()).thenReturn(
                vocabularySize);
        ComputerStudent student = new ComputerStudent(preferences);
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
            public void askForResponse() {
                focus = FocusField.Challenge;
            }
        });
        return student;
    }
    
    private ComputerStudent createStudent() {
        ComputerStudent student = createStudentWithVocabularySize(Integer.MAX_VALUE);
        return student;
    }
    
    @Test
    public void noSolutionFound() {
        int batchSize = 100;
        assertThat("word count", wordList.size(), lessThan(batchSize));
        ComputerStudent student = createStudent();
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
        ComputerStudent student = createStudent();
        student.setSearchBatchSize(batchSize);
        Puzzle puzzle = new Puzzle("AXR", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is(Puzzle.NOT_SET));
        assertThat("is finished", isFinished, is(true));
    }
    
    @Test
    public void maxBatchCount() {
        ComputerStudent student = createStudent();
        student.setMaxSearchBatchCount(1);
        
        Puzzle puzzle = new Puzzle("AXR", student, wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(true));
    }
    
    @Test
    public void ignoreWorseWord() {
        ComputerStudent student = createStudent();
        wordList = new WordList();
        wordList.read(Arrays.asList("PIECE", "PRICE"));
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
        wordList.read(Arrays.asList("PIECE", "PIPE"));
        ComputerStudent student = createStudent();
        student.setMaxSearchBatchCount(1);
        
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        puzzle.setMinimumWordLength(5);
        student.startSolving(puzzle);
        
        student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is("PIECE"));
    }
    
    @Test
    public void ignoreWordsBetterThanPrevious() {
        wordList = new WordList();
        wordList.read(Arrays.asList("PRICE", "PIECE", "PIPE"));
        ComputerStudent student = createStudent();
        student.setMaxSearchBatchCount(1);
        
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        puzzle.setPreviousWord("piece");
        student.startSolving(puzzle);
        
        student.runSearchBatch();
        
        assertThat("solution", puzzle.getSolution(), is("PRICE"));
    }
    
    @Test
    public void vocabularySizeNoMatch() {
        ComputerStudent student = createStudentWithVocabularySize(1);
        
        Puzzle puzzle = new Puzzle("AXR", student, wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(true));
    }
    
    @Test
    public void prepareResponse() {
        ComputerStudent student = createStudentWithVocabularySize(1);
        Puzzle puzzle = new Puzzle("PIE", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        student.runSearchBatch();
        puzzle.setSolution("");
        
        student.prepareResponse();
        
        assertThat("response", puzzle.getResponse(), is("PRICE"));
    }
    
    @Test
    public void prepareResponseNotAsGood() {
        ComputerStudent student = createStudentWithVocabularySize(1);
        Puzzle puzzle = new Puzzle("PIE", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        student.runSearchBatch();
        puzzle.setSolution("PIPE");
        
        student.prepareResponse();
        
        assertThat("response", puzzle.getResponse(), is(Puzzle.NO_SOLUTION));
    }
    
    @Test
    public void prepareResponseNotReady() {
        ComputerStudent student = createStudent();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Called prepareResponse() before startSolving().");
        student.prepareResponse();
    }
    
    @Test
    public void vocabularySizeMatch() {
        ComputerStudent student = createStudentWithVocabularySize(1);
        
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
        
        ComputerStudent student = createStudentWithVocabularySize(vocabularySize);
        student.setMaxSearchBatchCount(maxSearchBatchCount);
        
        int batchSize = student.getSearchBatchSize();
        assertThat("batch size", batchSize, is(expectedBatchSize));
    }
    
    @Test
    public void maxBatchCountInactiveStudent() {
        ComputerStudent student = createStudentWithVocabularySize(2);
        student.setMaxSearchBatchCount(1);
        
        Puzzle puzzle = new Puzzle("AXR", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        
        boolean isFinished = student.runSearchBatch();
        
        assertThat("finished", isFinished, is(false));
    }
    
    @Test
    public void createSearchTaskCancelsAfterLastWord() {
        ComputerStudent student = createStudent();
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
        ComputerStudent student = createStudent();
        focus = FocusField.Solution;
        Puzzle puzzle = new Puzzle("PIE", student, wordList);
        student.startSolving(puzzle);
        
        assertThat("focus", focus, nullValue());
    }
    
    @Test
    public void startThinkingWhenInactive() {
        ComputerStudent student = createStudent();
        focus = FocusField.Solution;
        Puzzle puzzle = new Puzzle("PIE", new Student("Bob"), wordList);
        student.startSolving(puzzle);
        
        assertThat("focus", focus, is(FocusField.Solution));
    }
    
    @Test
    public void serialization() throws Exception {
        ComputerStudent student = createStudent();
        student.setSearchBatchSize(50);
        student.startSolving(new Puzzle("ABC", student));
        
        byte[] bytes = SerializableTools.serialize(student);
        ComputerStudent student2 = SerializableTools.deserialize(
                bytes,
                ComputerStudent.class);
        
        assertThat("batch size", student2.getSearchBatchSize(), is(50));
        assertThat("puzzle", student2.getCurrentPuzzle(), nullValue());
    }
}
