package com.github.donkirkby.vograbulary.ultraghost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.donkirkby.vograbulary.ultraghost.UltraghostRandom;

public class DummyRandom extends UltraghostRandom {
    private List<String> puzzles;
    private ArrayList<String> wordList = new ArrayList<String>();
    private int puzzleIndex;
    private int startingPlayer;
    
    @Override
    public String generatePuzzle() {
        return getPuzzles().get(puzzleIndex++);
    }

    @Override
    public void loadWordList(Iterable<String> wordList) {
        for (String word : wordList) {
            this.wordList.add(word);
        }
    }

    public Iterable<String> getWordList() {
        return wordList;
    }
    
    @Override
    public int chooseStartingStudent(int playerCount) {
        return startingPlayer;
    }
    
    /**
     * Set which player will be chosen. Defaults to zero.
     */
    public void setStartingStudent(int playerIndex) {
        startingPlayer = playerIndex;
    }

    public List<String> getPuzzles() {
        return puzzles;
    }

    public void setPuzzles(String...puzzles) {
        this.puzzles = Arrays.asList(puzzles);
    }
}
