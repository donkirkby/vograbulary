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
public class WordListNextBetterTest {
   @Parameter
   public String letters;
   
   @Parameter(value=1)
   public String solution;
   
   @Parameter(value=2)
   public String improvedSolution;
   
   @Parameter(value=3)
   public String expectedNextBetter;
   
   @Parameters(name="{0}: {1},{2} => {3}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", "PRICE", "", "PIECE"},
               {"PIE", "PIECE", "", "PIPE"},
               {"PIE", "piece", "", "PIPE"},
               {"PIE", "PIECE", null, "PIPE"},
               {"PIE", "", "PRICE", "PIECE"},
               {"PIE", null, "PRICE", "PIECE"},
               {"PIE", "PIECE", "PIPE", "PINE"},
               {"PIE", "PIECE", "pipe", "PINE"},
               {"PIE", "PIPE", "PIECE", "PINE"},
               {"PIE", "ASKS", "", "PIERCE"},
               {"PIE", "PAIE", "", "PIERCE"},
               {"PIE", "", "ASKS", "PIERCE"},
               {"PIE", "", "PAIE", "PIERCE"},
               {"PIE", "PINE", "", null}});
   }
   
   private WordList wordList;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.read(new StringReader("pierce\nprice\npiece\npipe\npine\nasks"));
   }
   
   @Test
   public void findNextBetter() {
       String nextBetter = wordList.findNextBetter(letters, solution, improvedSolution);
       assertThat("next better", nextBetter, is(expectedNextBetter));
   }
}
