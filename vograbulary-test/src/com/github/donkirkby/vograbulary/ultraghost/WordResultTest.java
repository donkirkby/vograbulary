package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class WordResultTest {
    @Test
    public void notAMatch() {
        assertThat(WordResult.NOT_A_MATCH.toString(), is("not a match"));
        assertThat("score", WordResult.NOT_A_MATCH.getScore(), is(0));
    }

    @Test
    public void earlier() {
        assertThat(WordResult.EARLIER.toString(), is("earlier (+2)"));
    }

    @Test
    public void improvementNotAWord() {
        assertThat(
                WordResult.IMPROVEMENT_NOT_A_WORD.toString(), 
                is("not a word (+3)"));
    }

    @Test
    public void wordFound() {
        assertThat(WordResult.WORD_FOUND.toString(), is("word found (-1)"));
    }
}
