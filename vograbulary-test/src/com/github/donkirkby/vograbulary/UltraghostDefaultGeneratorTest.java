package com.github.donkirkby.vograbulary;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class UltraghostDefaultGeneratorTest {
    @Test
    public void evenDistribution() {
        Random random = mock(Random.class);
        when(random.nextDouble()).thenReturn(0.1, 0.3, 0.9);
        
        UltraghostGenerator generator = new UltraghostDefaultGenerator(random);
        generator.loadWordList(
                Arrays.asList("AAAA", "BBBB", "CCCC", "DDDD", "EEEE"));
        
        String puzzle = generator.generate();
        
        assertThat("puzzle", puzzle, is("ABE"));
    }

    @Test
    public void unevenDistribution() {
        Random random = mock(Random.class);
        when(random.nextDouble()).thenReturn(0.1, 0.3, 0.9);
        
        UltraghostGenerator generator = new UltraghostDefaultGenerator(random);
        generator.loadWordList(
                Arrays.asList("AABA", "BCCB", "CCCC", "DDDD", "EEEE"));
        
        String puzzle = generator.generate();
        
        assertThat("puzzle", puzzle, is("ACE"));
    }
}
