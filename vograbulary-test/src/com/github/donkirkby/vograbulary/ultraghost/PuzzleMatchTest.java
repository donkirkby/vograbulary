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
public class PuzzleMatchTest {
   @Parameter
   public String letters;
   
   @Parameter(value=1)
   public String solution;
   
   @Parameter(value=2)
   public boolean expectedResult;
   
   @Parameters(name="{0}: {1} => {2}")
   public static List<Object[]> getParameters() {
       return Arrays.asList(new Object[][] { 
               {"PIE", "PIECE", true},
               {"PIE", "PASTIE", true},
               {"PIE", "pastie", true},
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
       Student owner = new Student("Bob");
       Puzzle puzzle = new Puzzle(letters, owner, wordList);
       puzzle.setSolution(solution);
       boolean isMatch = puzzle.isSolutionAMatch();
       assertThat("result", isMatch, is(expectedResult));
   }
}
