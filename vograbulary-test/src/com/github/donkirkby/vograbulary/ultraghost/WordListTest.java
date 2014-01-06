package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class WordListTest {
    private WordList wordList;
    
    @Before
    public void setUp() {
        wordList = new WordList();
        wordList.read(new StringReader("ones\ntwo\nthree"));
    }
    
    @Test
    public void visit() {
        ArrayList<String> words = new ArrayList<String>();
        for (String word : wordList) {
            words.add(word);
        };
        
        // skips the three-letter word, "two".
        assertThat("words", words, is(Arrays.asList("ONES", "THREE")));
    }
}
