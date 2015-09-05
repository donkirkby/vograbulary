package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.donkirkby.vograbulary.SerializableTools;

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
    
    @Test
    public void serialize() throws Exception {
        Student.StudentListener listener = new Student.StudentListener() {
            @Override
            public void showThinking() {
            }
            
            @Override
            public void askForSolution() {
            }
            
            @Override
            public void askForResponse() {
            }
        };
        Student student = new Student("Bob");
        student.setWordList(new WordList());
        student.addScore(2);
        student.setListener(listener);
        
        byte[] bytes = SerializableTools.serialize(student);
        Student student2 = SerializableTools.deserialize(bytes, Student.class);
        
        assertThat(student2.getName(), is("Bob"));
        assertThat(student2.getScore(), is(2));
        assertThat(student2.getListener(), nullValue());
    }
}
