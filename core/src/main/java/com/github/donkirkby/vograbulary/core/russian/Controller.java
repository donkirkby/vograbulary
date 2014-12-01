package com.github.donkirkby.vograbulary.core.russian;

import java.util.ArrayList;

import com.github.donkirkby.vograbulary.core.ultraghost.WordList;

public class Controller {
    private RussianDollsScreen screen;
    private ArrayList<String> clues = new ArrayList<String>();
    private int clueNumber = -1;
    private WordList wordList;

    /**
     * Load puzzles in from the source file, one puzzle per line.
     */
    public void loadPuzzles(String puzzles) {
        String[] lines = puzzles.split("\n");
        for (String line : lines) {
            clues.add(line);
        }
        next();
    }
    
    public void setWordList(WordList wordList) {
        this.wordList = wordList;
    }
    
    public void setScreen(RussianDollsScreen screen) {
        this.screen = screen;
    }

    public void next() {
        String clue = clues.get(++clueNumber);
        Puzzle previousPuzzle = screen.getPuzzle();
        screen.setPuzzle(
                previousPuzzle == null
                ? new Puzzle(clue)
                : new Puzzle(clue, previousPuzzle));
    }

    public void back() {
        String clue = clues.get(--clueNumber);
        screen.setPuzzle(new Puzzle(clue));
    }

    public void solve() {
        Puzzle puzzle = screen.getPuzzle();
        puzzle.setSolved(
                puzzle.isTargetValid() &&
                wordList.contains(puzzle.getCombination()));
    }

    public String adjustScore(float seconds) {
        Puzzle puzzle = screen.getPuzzle();
        return puzzle == null ? "" : puzzle.adjustScore(seconds);
    }

}
