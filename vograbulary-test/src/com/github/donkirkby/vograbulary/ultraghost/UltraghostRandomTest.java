package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import com.github.donkirkby.vograbulary.ultraghost.UltraghostRandom;

public class UltraghostRandomTest {
    @Test
    public void evenDistribution() {
        Random random = mock(Random.class);
        when(random.nextDouble()).thenReturn(0.1, 0.3, 0.9);
        
        UltraghostRandom generator = new UltraghostRandom(random);
        generator.loadWordList(
                Arrays.asList("AAAA", "BBBB", "CCCC", "DDDD", "EEEE"));
        
        String puzzle = generator.generatePuzzle();
        
        assertThat("puzzle", puzzle, is("ABE"));
    }

    @Test
    public void unevenDistribution() {
        Random random = mock(Random.class);
        when(random.nextDouble()).thenReturn(0.1, 0.3, 0.9);
        
        UltraghostRandom generator = new UltraghostRandom(random);
        generator.loadWordList(
                Arrays.asList("AABA", "BCCB", "CCCC", "DDDD", "EEEE"));
        
        String puzzle = generator.generatePuzzle();
        
        assertThat("puzzle", puzzle, is("ACE"));
    }
}
