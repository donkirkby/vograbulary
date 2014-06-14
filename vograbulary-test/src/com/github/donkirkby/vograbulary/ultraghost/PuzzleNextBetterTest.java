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
public class PuzzleNextBetterTest {
   @Parameter
   public String letters;
   
   @Parameter(value=1)
   public int minimumLength;
   
   @Parameter(value=2)
   public String solution;
   
   @Parameter(value=3)
   public String improvedSolution;
   
   @Parameter(value=4)
   public String expectedNextBetter;
   
   @Parameters(name="{0} at least {1}: {2},{3} => {4}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", 4, "PRIDE", "", "PRICE"},
               {"PIE", 4, "PRICE", "", "PIPE"},
               {"PIE", 4, "price", "", "PIPE"},
               {"PIE", 5, "price", "", "PIECE"},
               {"PIE", 4, "PRICE", null, "PIPE"},
               {"PIE", 4, "", "PRIDE", "PRICE"},
               {"PIE", 4, null, "PRIDE", "PRICE"},
               {"PIE", 4, "PRICE", "PIPE", "PINE"},
               {"PIE", 4, "PRICE", "pipe", "PINE"},
               {"PIE", 4, "PIPE", "PRICE", "PINE"},
               {"PIE", 4, "ASKS", "", "PIERCE"},
               {"PIE", 4, "PAIE", "", "PIERCE"},
               {"PIE", 4, "", "ASKS", "PIERCE"},
               {"PIE", 4, "", "PAIE", "PIERCE"},
               {"PIE", 4, "PINE", "", null}});
   }
   
   private WordList wordList;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.setMinimumWordLength(minimumLength);
       wordList.read(new StringReader(
               "pierce\npride\nprice\npipe\npine\nasks\npiece"));
   }
   
   @Test
   public void findNextBetter() {
       Puzzle puzzle = new Puzzle(letters, new Student("Bob"), wordList);
       puzzle.setSolution(solution);
       puzzle.setResponse(improvedSolution);
       String nextBetter = puzzle.findNextBetter();
       assertThat("next better", nextBetter, is(expectedNextBetter));
   }
}
