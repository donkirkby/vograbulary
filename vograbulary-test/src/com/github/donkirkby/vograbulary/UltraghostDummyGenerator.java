package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UltraghostDummyGenerator implements UltraghostGenerator {
    private List<String> puzzles;
    private ArrayList<String> wordList = new ArrayList<String>();
    private int index;
    
    public UltraghostDummyGenerator(String...puzzles) {
        this.puzzles = Arrays.asList(puzzles);
    }
    
    @Override
    public String generate() {
        return puzzles.get(index++);
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
}
