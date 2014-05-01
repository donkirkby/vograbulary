package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WordListSolutionTest {
   @Parameter
   public String puzzle;
   
   @Parameter(value=1)
   public int minimumLength;
   
   @Parameter(value=2)
   public String solution;
   
   @Parameter(value=3)
   public WordResult expectedResult;
   
   @Parameters(name="{0} at least {1}: {2} => {3}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", 4, "PIZE", WordResult.NOT_A_WORD},
               {"PIE", 4, "PIZS", WordResult.NOT_A_WORD}, // user probably wants to know
               {"PIE", 4, "PIECE", WordResult.VALID},
               {"PIE", 4, "piece", WordResult.VALID},
               {"PIE", 4, "ripe", WordResult.NOT_A_MATCH},
               {"PIE", 4, "pipe", WordResult.VALID},
               {"PIE", 5, "pipe", WordResult.TOO_SHORT},
               {"PIE", 5, "piece", WordResult.VALID},
               {"PIE", 4, null, WordResult.SKIPPED},
               {"PIE", 4, "", WordResult.SKIPPED} });
   }
   
   private WordList wordList;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.setMinimumWordLength(minimumLength);
       wordList.read(new StringReader("piece\nripe\npipe"));
   }
   
   @Test
   public void checkSolution() {
       WordResult result = wordList.checkSolution(puzzle, solution);
       assertThat("result", result, is(expectedResult));
   }
}
