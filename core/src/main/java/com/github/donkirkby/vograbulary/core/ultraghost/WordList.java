package com.github.donkirkby.vograbulary.core.ultraghost;

import java.util.ArrayList;
import java.util.Iterator;

public class WordList implements Iterable<String> {
    private ArrayList<String> wordList = new ArrayList<String>();
    
    /** Read all the words from a string and add them to the list.
     * 
     */
    public void read(String words) {
        String[] lines = words.split("\\s");
        for (String line : lines) {
            if (line.length() > 3) {
                wordList.add(line.toUpperCase());
            }
        }
    }
    
    public boolean contains(String word) {
        return wordList.contains(word);
    }

    /**
     * Get an iterator that only iterates over the words that meet the minimum
     * word length.
     */
    @Override
    public Iterator<String> iterator() {
        return wordList.iterator();
    }

    /**
     * Get the size of the word list, including words that do not meet the
     * minimum word length.
     */
    public int size() {
        return wordList.size();
    }
}
