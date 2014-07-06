package com.github.donkirkby.vograbulary.russian;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

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

    @Test
    public void adjustmentsAddUp() {
        Puzzle puzzle1 = new Puzzle("not relevant");
        Puzzle puzzle2 = new Puzzle("not relevant");
        
        puzzle1.adjustScore(10);
        BigDecimal score1 = puzzle1.getScore();
        
        puzzle2.adjustScore(1);
        puzzle2.adjustScore(9);
        BigDecimal score2 = puzzle2.getScore();
        
        assertThat("score after 2 adjustments", score2, is(score1));
    }
}
