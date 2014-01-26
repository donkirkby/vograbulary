package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer.Task;

public class ControllerWithComputerFirstTest {
    private Controller controller;
    private DummyRandom random;
    private View view;
    private Task searchTask;
    private Student student;
    private ComputerStudent computerStudent;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        Gdx.app = mock(Application.class);
        searchTask = null;
        random = new DummyRandom();
        random.setPuzzles("PIE", "PEE", "LOP");
        view = mock(View.class);
        student = new Student("Student");
        computerStudent = new ComputerStudent();
        computerStudent.setMaxSearchBatchCount(2);
        controller = new Controller();
        controller.readWordList(new StringReader("PIPE\nPIECE\nLOOP"));
        controller.setRandom(random);
        controller.setView(view);
        controller.addStudent(computerStudent);
        controller.addStudent(student);
    }
    
    @After
    public void tearDown() {
        if (searchTask != null) {
            searchTask.cancel();
        }
    }
    
    @Test
    public void createSearchTaskForSecondPuzzleWithSolution() {
        DummyView dummyView = new DummyView();
        controller.setView(dummyView);
        
        controller.next(); // display puzzle for computer
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        searchTask.run(); // find and display solution by computer
        controller.next(); // check challenge and display result
        
        controller.next(); // display next puzzle for human
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        dummyView.setSolution("piece");
        controller.next(); // display challenge by computer and result
        
        controller.next(); // display puzzle for computer
        searchTask = dummyView.getSearchTask();
        searchTask.run();
        String clearedSolution = dummyView.getSolution();
        
        assertThat("cleared solution", clearedSolution, is(""));
    }
}
