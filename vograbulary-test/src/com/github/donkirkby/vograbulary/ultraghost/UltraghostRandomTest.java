package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.github.donkirkby.vograbulary.ultraghost.UltraghostRandom;

@RunWith(Parameterized.class)
public class UltraghostRandomTest {
    @Parameter
    public Double firstCharacterChoice;
    
    @Parameter(value=1)
    public Double middleCharacterChoice;
    
    @Parameter(value=2)
    public Double lastCharacterChoice;
    
    @Parameter(value=3)
    public String words;
    
    @Parameter(value=4)
    public String expectedPuzzle;
    
    @Parameters(name="{0},{1},{2} of {3} -> {4}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {0.1, 0.0, 0.0, "AXXX BXXX CXXX DXXX EXXX", "AXX"},
                {0.3, 0.0, 0.0, "AXXX BXXX CXXX DXXX EXXX", "BXX"},
                {0.0, 0.0, 0.3, "XXXE XXXD XXXC XXXB XXXA", "XXB"},
                {0.0, 0.15, 0.0, "XAXX XBXX XCXX XDXX XEXX", "XBX"}
        });
    }
    
    @Test
    public void letterDistribution() {
        Random random = mock(Random.class);
        when(random.nextDouble()).thenReturn(
                firstCharacterChoice, 
                middleCharacterChoice, 
                lastCharacterChoice);
        
        UltraghostRandom generator = new UltraghostRandom(random);
        generator.loadWordList(Arrays.asList(words.split(" ")));
        
        String puzzle = generator.generatePuzzle();
        
        assertThat("puzzle", puzzle, is(expectedPuzzle));
    }
}
