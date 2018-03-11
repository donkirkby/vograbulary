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
    public void skipped() {
        assertThat(WordResult.SKIPPING.toString(), is("skipping"));
    }

    @Test
    public void improvementNotAWord() {
        assertThat(
                WordResult.IMPROVEMENT_NOT_A_WORD.toString(), 
                is("not a word"));
    }

    @Test
    public void wordFound() {
        assertThat(WordResult.WORD_FOUND.toString(), is("word found"));
    }
}
