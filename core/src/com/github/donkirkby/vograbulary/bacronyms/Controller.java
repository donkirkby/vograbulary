package com.github.donkirkby.vograbulary.bacronyms;

import java.util.ArrayList;
import java.util.List;

import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class Controller {
    private WordList wordList;
    private BacronymsScreen screen;
    private List<String> puzzleLines;
    private int solvedCount;
    
    public WordList getWordList() {
        return wordList;
    }
    public void setWordList(WordList wordList) {
        this.wordList = wordList;
    }
    
    public BacronymsScreen getScreen() {
        return screen;
    }
    public void setScreen(BacronymsScreen screen) {
        this.screen = screen;
        screen.setState(BacronymsScreen.State.START);
    }
    
    public void loadPuzzles(List<String> puzzleLines) {
        this.puzzleLines = new ArrayList<>(puzzleLines);
    }
    
    public void next() {
        Puzzle puzzle = new Puzzle();
        String line = puzzleLines.get(solvedCount).toUpperCase();
        for (String word : line.split("\\s+")) {
            puzzle.addWord(word);
        }
        screen.setPuzzle(puzzle);
        screen.setState(BacronymsScreen.State.NEW);
        solvedCount++;
    }
    
    public void solve() {
        String selectedWord = screen.getPuzzle().getSelectedWord();
        String reversed = new StringBuilder(selectedWord).reverse().toString();
        if (wordList.contains(reversed)) {
            screen.setState(BacronymsScreen.State.SOLVED);
        }
        else {
            screen.setState(BacronymsScreen.State.WRONG);
        }
    }
}
