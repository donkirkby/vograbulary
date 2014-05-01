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
   public int minimumLength;
   
   @Parameter(value=2)
   public String solution;
   
   @Parameter(value=3)
   public String challenge;
   
   @Parameter(value=4)
   public WordResult expectedResult;
   
   @Parameters(name="{0} at least {1}: {2}/{3} => {4}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", 4, "PIECE", "", WordResult.NOT_IMPROVED},
               {"PIE", 4, "piece", "", WordResult.NOT_IMPROVED},
               {"PIE", 4, "piece", null, WordResult.NOT_IMPROVED},
               {"PIE", 4, "piece", "piece", WordResult.NOT_IMPROVED},
               {"PIE", 4, "piece", "pipe", WordResult.SHORTER},
               {"PIE", 5, "piece", "pipe", WordResult.IMPROVEMENT_TOO_SHORT},
               {"PIE", 4, "price", "piece", WordResult.EARLIER},
               {"PIE", 4, "piece", "pize", WordResult.IMPROVEMENT_NOT_A_WORD},
               {"PIE", 4, "piece", "rope", WordResult.IMPROVEMENT_NOT_A_MATCH},
               {"PIE", 4, "pine", "piece", WordResult.LONGER},
               {"MIE", 4, "mime", "mine", WordResult.LATER},
               {"PIE", 4, null, "pipe", WordResult.WORD_FOUND},
               {"PIE", 4, "", "pipe", WordResult.WORD_FOUND},
               {"PIE", 5, null, "pipe", WordResult.IMPROVED_SKIP_TOO_SHORT},
               {"PIE", 5, "", "pipe", WordResult.IMPROVED_SKIP_TOO_SHORT},
               {"PIE", 4, null, "pize", WordResult.IMPROVED_SKIP_NOT_A_WORD},
               {"PIE", 4, "", "pize", WordResult.IMPROVED_SKIP_NOT_A_WORD},
               {"PIE", 4, null, "rope", WordResult.IMPROVED_SKIP_NOT_A_MATCH},
               {"PIE", 4, "", "rope", WordResult.IMPROVED_SKIP_NOT_A_MATCH},
               {"PIE", 4, "", "", WordResult.SKIPPED},
               {"PIE", 4, null, null, WordResult.SKIPPED} });
   }
   
   private WordList wordList;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.setMinimumWordLength(minimumLength);
       wordList.read(new StringReader("piece\npipe\npine\nrope\nmime\nmine"));
   }
   
   @Test
   public void checkChallenge() {
       WordResult result = wordList.checkResponse(puzzle, solution, challenge);
       assertThat("result", result, is(expectedResult));
   }
}
