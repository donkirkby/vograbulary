package com.github.donkirkby.vograbulary.ultraghost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class WordList implements Iterable<String> {
    private ArrayList<String> wordList = new ArrayList<String>();
    
    public WordList() {
    }
    
    public WordList(String... words) {
        read(Arrays.asList(words));
    }
    
    /** Add all the words in a list.
     */
    public void read(List<String> words) {
        for (String word : words) {
            if (word.length() > 3) {
                wordList.add(word.toUpperCase());
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
