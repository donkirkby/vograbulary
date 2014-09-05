package com.github.donkirkby.vograbulary.core.russian;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.github.donkirkby.vograbulary.core.russian.RussianDollsScreen;
import com.github.donkirkby.vograbulary.core.ultraghost.WordList;

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
    public void loadPuzzles(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        try {
            String puzzle;
            while (null != (puzzle = bufferedReader.readLine())) {
                clues.add(puzzle);
            }
            next();
            
            reader.close();
        } catch (IOException ex) {
//  TODO:          throw new RuntimeException("Loading puzzles failed.", ex);
        }
        
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
