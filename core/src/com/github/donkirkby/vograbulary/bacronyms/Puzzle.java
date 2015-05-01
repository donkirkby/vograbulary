package com.github.donkirkby.vograbulary.bacronyms;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {
    private List<String> words = new ArrayList<>();
    private int selectedIndex = -1;

    public void addWord(String word) {
        words.add(word);
    }

    public String getWord(int i) {
        return words.get(i);
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public String getSelectedWord() {
        return words.get(selectedIndex);
    }

    public boolean isSelected() {
        return selectedIndex >= 0;
    }
}
