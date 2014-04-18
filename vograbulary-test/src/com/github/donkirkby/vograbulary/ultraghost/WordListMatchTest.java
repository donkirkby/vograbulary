package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WordListMatchTest {
   @Parameter
   public String puzzle;
   
   @Parameter(value=1)
   public String solution;
   
   @Parameter(value=2)
   public boolean expectedResult;
   
   @Parameters(name="{0}: {1} => {2}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", "PIECE", true},
               {"PIE", "PASTIE", true},
               {"PIE", "RIPE", false},
               {"PIE", "POPE", false} ,
               {"PIE", "PIPS", false} ,
               {"PEE", "PIPE", false} });
   }
   
   private WordList wordList;
   
   @Before
   public void setUp() {
       wordList = new WordList();
   }
   
   @Test
   public void isMatch() {
       boolean isMatch = wordList.isMatch(puzzle, solution);
       assertThat("result", isMatch, is(expectedResult));
   }
}
