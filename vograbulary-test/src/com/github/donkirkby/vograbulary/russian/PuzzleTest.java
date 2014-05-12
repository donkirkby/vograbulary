package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PuzzleTest {
    @Test
    public void twoWordClue() {
        String expectedClue = "";
        Puzzle puzzle = new Puzzle("two words");
        
        String clue = puzzle.getClue();
        assertThat("clue", clue, is(expectedClue));
    }

    @Test
    public void twoWordTargets() {
        Puzzle puzzle = new Puzzle("two words");
        
        String target1 = puzzle.getTarget(0);
        String target2 = puzzle.getTarget(1);
        assertThat("target 1", target1, is("TWO"));
        assertThat("target 2", target2, is("WORDS"));
    }
    
    @Test
    public void threeWordClue() {
        String expectedClue = "three *big* *words*";
        Puzzle puzzle = new Puzzle(expectedClue);
        
        String clue = puzzle.getClue();
        assertThat("clue", clue, is(expectedClue));
    }

    @Test
    public void threeWordTargets() {
        Puzzle puzzle = new Puzzle("three *big* *words*");
        
        String target1 = puzzle.getTarget(0);
        String target2 = puzzle.getTarget(1);
        assertThat("target 1", target1, is("BIG"));
        assertThat("target 2", target2, is("WORDS"));
    }
    
    @Test
    public void solve() {
        Puzzle puzzle = new Puzzle("unable comfort");
        
        puzzle.setTargetWord(0);
        puzzle.setTargetCharacter(2);
        String combination = puzzle.getCombination();
        
        assertThat("combination", combination, is("UNCOMFORTABLE"));
    }
    
    @Test
    public void solveReverse() {
        Puzzle puzzle = new Puzzle("comfort unable");
        
        puzzle.setTargetWord(1);
        puzzle.setTargetCharacter(2);
        String combination = puzzle.getCombination();
        
        assertThat("combination", combination, is("UNCOMFORTABLE"));
    }

    @Test
    public void targetsWithPunctuation() {
        Puzzle puzzle = new Puzzle("*targets* sometimes have *punctuation*!");
        
        String target1 = puzzle.getTarget(0);
        String target2 = puzzle.getTarget(1);
        assertThat("target 1", target1, is("TARGETS"));
        assertThat("target 2", target2, is("PUNCTUATION"));
    }
}
