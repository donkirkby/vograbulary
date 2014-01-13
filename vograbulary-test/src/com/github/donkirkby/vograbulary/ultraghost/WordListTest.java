package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WordListTest {
    private WordList wordList;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        wordList = new WordList();
        wordList.read(new StringReader("ones\ntwo\nthree"));
    }
    
    @Test
    public void iterate() {
        ArrayList<String> words = new ArrayList<String>();
        for (String word : wordList) {
            words.add(word);
        };
        
        // skips the three-letter word, "two".
        assertThat("words", words, is(Arrays.asList("ONES", "THREE")));
    }
    
    @Test
    public void readAgain() {
        wordList.read(new StringReader("four"));
        
        assertThat("words", wordList, hasItem("THREE"));
    }
    
    @Test
    public void failedRead() throws Exception {
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine())
            .thenReturn("success")
            .thenThrow(new IOException("Timeout"));
        
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Reading word list failed.");
        wordList.read(reader);
    }
}
