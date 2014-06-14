package com.github.donkirkby.vograbulary.ultraghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class WordList implements Iterable<String> {
    private ArrayList<String> wordList = new ArrayList<String>();
    
    /** Read all the words from a reader and add them to the list.
     * 
     * @param reader an open reader. This method will close it.
     */
    public void read(Reader reader) {
        read(new BufferedReader(reader));
    }

    /** Read all the words from a reader and add them to the list.
     * 
     * @param reader an open reader. This method will close it.
     */
    public void read(BufferedReader lineReader) {
        try {
            try {
                String line;
                while ((line = lineReader.readLine()) != null) {
                    if (line.length() > 3) {
                        wordList.add(line.toUpperCase());
                    }
                }
            } 
            finally {
                lineReader.close();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Reading word list failed.", e);
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
