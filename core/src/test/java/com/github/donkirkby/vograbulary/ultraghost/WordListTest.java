package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
        wordList.read(Arrays.asList("ones", "two", "three"));
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
    public void readAgain() {
        wordList.read(Arrays.asList("four"));
        
        assertThat("words", wordList, hasItem("THREE"));
    }
}
