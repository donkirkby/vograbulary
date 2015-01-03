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
public class PuzzlePreviousWordTest {
   @Parameter
   public String letters;
   
   @Parameter(value=1)
   public String previousWord;
   
   @Parameter(value=2)
   public String solution;
   
   @Parameter(value=3)
   public String challenge;
   
   @Parameter(value=4)
   public WordResult expectedResult;
   
   @Parameters(name="{0} after {1}: {2}/{3} => {4}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", null, "PINE", null, WordResult.VALID},
               {"PIE", "PINE", "PINE", null, WordResult.TOO_SOON},
               {"PIE", "PINE", "PIPE", null, WordResult.VALID},
               {"PIE", "PINE", "PIECE", null, WordResult.VALID},
               {"PIE", "PINE", "PIECE", "PIPE", WordResult.SHORTER},
               {"PIE", "PINE", "PIECE", "PINE", WordResult.IMPROVEMENT_TOO_SOON},
               {"PIE", "PINE", "", "PINE", WordResult.IMPROVED_SKIP_TOO_SOON}
       });
   }
   
   private WordList wordList;
   private Puzzle puzzle;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.read(Arrays.asList("piece", "pipe", "pine", "rope", "mime", "mine"));
       puzzle = new Puzzle(letters, new Student("Bob"), wordList);
       puzzle.setPreviousWord(previousWord);
       puzzle.setSolution(solution);
       puzzle.setResponse(challenge);
   }
   
   @Test
   public void getResult() {
       WordResult result = puzzle.getResult();
       assertThat("result", result, is(expectedResult));
   }
}
