package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
        assertThat("size", wordList.size(), is(2));
    }
    
    @Test
    public void iterateWithMinimumWordLength() {
        wordList.setMinimumWordLength(5);
        ArrayList<String> words = new ArrayList<String>();
        for (String word : wordList) {
            words.add(word);
        };
        
        // skips the three-letter word, "two".
        assertThat("words", words, is(Arrays.asList("THREE")));
        assertThat("size", wordList.size(), is(2));
    }
    
    @Test
    public void hasNextWithMinimumWordLength() {
        wordList = new WordList();
        wordList.read(new StringReader("three\nones"));
        wordList.setMinimumWordLength(5);

        Iterator<String> iterator = wordList.iterator();
        iterator.next();
        boolean hasNext = iterator.hasNext();

        assertThat("hasNext", hasNext, is(false));
    }
    
    @Test
    public void nextThrows() {
        wordList.setMinimumWordLength(10);

        Iterator<String> iterator = wordList.iterator();
        thrown.expect(NoSuchElementException.class);
        iterator.next();
    }
    
    @Test
    public void nextAfterHasNext() {
        wordList.setMinimumWordLength(10);

        Iterator<String> iterator = wordList.iterator();
        iterator.hasNext();
        
        thrown.expect(NoSuchElementException.class);
        iterator.next();
    }
    
    @Test
    public void hasNextTwice() {
        wordList.setMinimumWordLength(5);

        Iterator<String> iterator = wordList.iterator();
        boolean hasNext1 = iterator.hasNext();
        boolean hasNext2 = iterator.hasNext();
        
        assertThat("has next 1", hasNext1, is(true));
        assertThat("has next 2", hasNext2, is(true));
    }
    
    @Test
    public void iteratorRemove() {
        Iterator<String> iterator = wordList.iterator();
        iterator.next();
        
        thrown.expect(UnsupportedOperationException.class);
        iterator.remove();
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
