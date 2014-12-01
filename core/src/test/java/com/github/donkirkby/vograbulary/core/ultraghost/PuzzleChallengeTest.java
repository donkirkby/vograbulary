package com.github.donkirkby.vograbulary.core.ultraghost;

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
public class PuzzleChallengeTest {
   @Parameter
   public String letters;
   
   @Parameter(value=1)
   public int minimumLength;
   
   @Parameter(value=2)
   public String solution;
   
   @Parameter(value=3)
   public String challenge;
   
   @Parameter(value=4)
   public WordResult expectedResult;
   
   @Parameter(value=5)
   public boolean isExpectedToImprove;
   
   @Parameters(name="{0} at least {1}: {2}/{3} => {4}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", 4, "PIECE", "", WordResult.NOT_IMPROVED, false},
               {"PIE", 4, "piece", "", WordResult.NOT_IMPROVED, false},
               {"PIE", 4, "piece", "piece", WordResult.NOT_IMPROVED, false},
               {"PIE", 4, "piece", "pipe", WordResult.SHORTER, true},
               {"PIE", 5, "piece", "pipe", WordResult.IMPROVEMENT_TOO_SHORT, false},
               {"PIE", 4, "price", "piece", WordResult.EARLIER, true},
               {"PIE", 4, "piece", "pize", WordResult.IMPROVEMENT_NOT_A_WORD, false},
               {"PIE", 4, "piece", "rope", WordResult.IMPROVEMENT_NOT_A_MATCH, false},
               {"PIE", 4, "pine", "piece", WordResult.LONGER, false},
               {"MIE", 4, "mime", "mine", WordResult.LATER, false},
               {"PIE", 4, "", "pipe", WordResult.WORD_FOUND, true},
               {"PIE", 5, "", "pipe", WordResult.IMPROVED_SKIP_TOO_SHORT, false},
               {"PIE", 4, "", "pize", WordResult.IMPROVED_SKIP_NOT_A_WORD, false},
               {"PIE", 4, "", "rope", WordResult.IMPROVED_SKIP_NOT_A_MATCH, false},
               {"PIE", 4, "", "", WordResult.SKIPPED, false},
               {"PIE", 4, null, "pipe", WordResult.UNKNOWN, false},
               {"PIE", 4, "piece", null, WordResult.VALID, false} });
   }
   
   private WordList wordList;
   private Puzzle puzzle;
   
   @Before
   public void setUp() {
       wordList = new WordList();
       wordList.read("piece\npipe\npine\nrope\nmime\nmine");
       puzzle = new Puzzle(letters, new Student("Bob"), wordList);
       puzzle.setMinimumWordLength(minimumLength);
       puzzle.setSolution(solution);
       puzzle.setResponse(challenge);
   }
   
   @Test
   public void getResult() {
       WordResult result = puzzle.getResult();
       assertThat("result", result, is(expectedResult));
   }
   
   @Test
   public void isImproved() {
       boolean isImproved = puzzle.isImproved();
       assertThat("isImproved", isImproved, is(isExpectedToImprove));
   }
}
