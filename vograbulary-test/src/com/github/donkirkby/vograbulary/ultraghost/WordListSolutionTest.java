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
   public String solution;
   
   @Parameter(value=2)
   public WordResult expectedResult;
   
   @Parameters(name="{0}: {1} => {2}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", "PIZE", WordResult.NOT_A_WORD},
               {"PIE", "PIZS", WordResult.NOT_A_WORD}, // user probably wants to know
               {"PIE", "PIECE", WordResult.VALID},
               {"PIE", "piece", WordResult.VALID},
               {"PIE", "ripe", WordResult.NOT_A_MATCH},
               {"PIE", "", WordResult.SKIPPED} });
   }
   
   private WordList wordList;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.read(new StringReader("piece\nripe"));
   }
   
   @Test
   public void checkSolution() {
       WordResult result = wordList.checkSolution(puzzle, solution);
       assertThat("result", result, is(expectedResult));
   }
}
