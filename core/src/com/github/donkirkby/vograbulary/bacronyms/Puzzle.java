package com.github.donkirkby.vograbulary.bacronyms;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {
    private static final int NO_SELECTION = Integer.MIN_VALUE;
    
    private List<String> words = new ArrayList<>();
    private int selectedIndex = NO_SELECTION;

    public void addWord(String word) {
        words.add(word);
    }

    public String getWord(int i) {
        String word = words.get(i);
        if (i == selectedIndex) {
            word = new StringBuilder(word).reverse().toString();
        }
        return word;
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public String getSelectedWord() {
        return getWord(selectedIndex);
    }

    public boolean isSelected() {
        return selectedIndex != NO_SELECTION;
    }
}
