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
public class WordListChallengeTest {
   @Parameter
   public String puzzle;
   
   @Parameter(value=1)
   public String solution;
   
   @Parameter(value=2)
   public String challenge;
   
   @Parameter(value=3)
   public WordResult expectedResult;
   
   @Parameters(name="{0}: {1}/{2} => {3}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", "PIECE", "", WordResult.NOT_IMPROVED},
               {"PIE", "piece", "", WordResult.NOT_IMPROVED},
               {"PIE", "piece", null, WordResult.NOT_IMPROVED},
               {"PIE", "piece", "piece", WordResult.NOT_IMPROVED},
               {"PIE", "piece", "pipe", WordResult.SHORTER},
               {"PIE", "price", "piece", WordResult.EARLIER},
               {"PIE", "piece", "pize", WordResult.IMPROVEMENT_NOT_A_WORD},
               {"PIE", "pine", "piece", WordResult.LONGER},
               {"MIE", "mime", "mine", WordResult.LATER},
               {"PIE", null, "pipe", WordResult.WORD_FOUND},
               {"PIE", "", "pipe", WordResult.WORD_FOUND},
               {"PIE", null, "pize", WordResult.IMPROVED_SKIP_NOT_A_WORD},
               {"PIE", "", "pize", WordResult.IMPROVED_SKIP_NOT_A_WORD},
               {"PIE", "", "", WordResult.SKIPPED} });
   }
   
   private WordList wordList;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.read(new StringReader("piece\npipe\npine\nrope\nmime\nmine"));
   }
   
   @Test
   public void checkChallenge() {
       WordResult result = wordList.checkResponse(puzzle, solution, challenge);
       assertThat("result", result, is(expectedResult));
   }
}
