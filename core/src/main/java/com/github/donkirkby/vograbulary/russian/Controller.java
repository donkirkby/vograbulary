package com.github.donkirkby.vograbulary.russian;

import java.util.ArrayList;
import java.util.List;

import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class Controller {
    private RussianDollsScreen screen;
    private ArrayList<String> clues = new ArrayList<String>();
    private int clueIndex = -1;
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
        String clue = clues.get(++clueIndex);
        Puzzle previousPuzzle = screen.getPuzzle();
        screen.setPuzzle(
                previousPuzzle == null
                ? new Puzzle(clue)
                : new Puzzle(clue, previousPuzzle));
    }

    public void back() {
        String clue = clues.get(--clueIndex);
        screen.setPuzzle(new Puzzle(clue));
    }

    public void solve() {
        Puzzle puzzle = screen.getPuzzle();
        puzzle.setSolved(
                puzzle.isTargetSet() &&
                wordList.contains(puzzle.getCombination()));
    }

    /**
     * Adjust the score based on the time.
     * @param seconds the number of seconds since this method was last called.
     * @return the updated score
     */
    public String adjustScore(float seconds) {
        return screen.getPuzzle().adjustScore(seconds);
    }

    /** Get which clue is being displayed.
     */
    public int getClueIndex() {
        return clueIndex;
    }
    /** Set which clue is being displayed.
     */
    public void setClueIndex(int clueIndex) {
        this.clueIndex = clueIndex;
    }
}
