package com.github.donkirkby.vograbulary.ultraghost;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.github.donkirkby.vograbulary.core.ultraghost.WordList;

@RunWith(Parameterized.class)
public class PuzzleMatchTest {
   @Parameter
   public String letters;
   
   @Parameter(value=1)
   public String word;
   
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
   
   @Test
   public void isMatch() {
       WordList wordList = new WordList();
       Student owner = new Student("Bob");
       Puzzle puzzle = new Puzzle(letters, owner, wordList);
       
       boolean isMatch = puzzle.isMatch(word);
       
       assertThat("result", isMatch, is(expectedResult));
   }
}
