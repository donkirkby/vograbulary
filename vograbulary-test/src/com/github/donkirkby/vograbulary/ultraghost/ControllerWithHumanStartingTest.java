package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ControllerWithHumanStartingTest {
    private Controller controller;
    private DummyRandom random;
    private DummyView dummyView;
    private Student student;
    private ComputerStudent computerStudent;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private String firstPuzzle;
    
    @Before
    public void setUp() {
        random = new DummyRandom();
        dummyView = new DummyView();
        student = new Student("Student");
        computerStudent = new ComputerStudent();
        controller = new Controller();
        controller.setRandom(random);
        controller.setView(dummyView);
        controller.addStudent(student);
        controller.addStudent(computerStudent);
        WordList wordList = new WordList();
        wordList.read(new StringReader("PIPE\nPIECE\nLOOP"));
        controller.setWordList(wordList);
        firstPuzzle = "PIE";
        random.setPuzzles(firstPuzzle);
    }
    
    @Test
    public void nextPuzzle() {
        controller.next();

        assertThat("puzzle", dummyView.getPuzzle(), is(firstPuzzle));
    }
}
