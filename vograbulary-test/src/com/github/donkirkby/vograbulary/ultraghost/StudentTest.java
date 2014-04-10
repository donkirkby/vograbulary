package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StudentTest {
    @Test
    public void scoreCount() {
        int expectedCount = 2;
        Student student = new Student("Bob");
        
        student.addScore(3);
        student.addScore(2);
        int scoreCount = student.getScoreCount();
        
        assertThat("score count", scoreCount, is(expectedCount));
    }

    @Test
    public void scoreCountAtStart() {
        int expectedCount = 0;
        Student student = new Student("Bob");
        
        int scoreCount = student.getScoreCount();
        
        assertThat("score count", scoreCount, is(expectedCount));
    }
}
