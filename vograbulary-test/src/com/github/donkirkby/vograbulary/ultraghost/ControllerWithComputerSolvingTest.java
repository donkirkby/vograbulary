package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class ControllerWithComputerSolvingTest {
    private Controller controller;
    private DummyRandom random;
    private DummyView dummyView;
    private Student student;
    private ComputerStudent computerStudent;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        random = new DummyRandom();
        dummyView = new DummyView();
        student = new Student("Student");
        computerStudent = new ComputerStudent();
        controller = new Controller();
        controller.readWordList(new StringReader(""));
        controller.setRandom(random);
        controller.setView(dummyView);
        controller.addStudent(computerStudent);
        controller.addStudent(student);
        controller.readWordList(new StringReader("PRIDE\nPIECE\nPILE"));
        random.setPuzzles("PIE");
        
        controller.next(); // display puzzle for computer
    }

    @Test
    public void focus() {
        assertThat("focus", dummyView.getCurrentFocus(), is("next"));
    }
}
