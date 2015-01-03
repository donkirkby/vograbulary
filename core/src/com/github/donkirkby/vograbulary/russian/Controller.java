package com.github.donkirkby.vograbulary.russian;

import java.util.ArrayList;
import java.util.List;

import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class Controller {
    private RussianDollsScreen screen;
    private ArrayList<String> clues = new ArrayList<String>();
    private int clueNumber = -1;
    private WordList wordList;

    /**
     * Load puzzles in from the source file, one puzzle per line.
     * @param reader contains the puzzles, will be closed before this method
     * returns.
     */
    public void loadPuzzles(List<String> clues) {
        this.clues.addAll(clues);
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
        puzzle.setSolved(wordList.contains(puzzle.getCombination()));
    }

    public String adjustScore(float seconds) {
        return screen.getPuzzle().adjustScore(seconds);
    }

}
