package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UltraghostDummyRandom extends UltraghostRandom {
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
    public int chooseStartingPlayer(int playerCount) {
        return startingPlayer;
    }
    
    /**
     * Set which player will be chosen. Defaults to zero.
     */
    public void setStartingPlayer(int playerIndex) {
        startingPlayer = playerIndex;
    }

    public List<String> getPuzzles() {
        return puzzles;
    }

    public void setPuzzles(String...puzzles) {
        this.puzzles = Arrays.asList(puzzles);
    }
}
