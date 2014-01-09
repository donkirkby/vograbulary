package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class WordResultTest {
    @Test
    public void string() {
        assertThat(WordResult.NOT_A_MATCH.toString(), is("not a match"));
        assertThat("score", WordResult.NOT_A_MATCH.getScore(), is(0));
    }
}
