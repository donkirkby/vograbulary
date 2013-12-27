package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

public class UltraghostController {
    public static final String NO_MATCH_MESSAGE = "None";
    
    private Random random = new Random();
    private String currentPuzzle;
    private ArrayList<String> wordList = new ArrayList<String>();

    public String next() {
        if (currentPuzzle == null)
        {
            currentPuzzle = nextPuzzle();
            return currentPuzzle;
        }
        String best = null;
        for (String word : wordList) {
            if (isMatch(word)) {
                if (best == null) {
                    best = word;
                }
                else if (word.length() < best.length()) {
                    best = word;
                }
            }
        }
        currentPuzzle = null;
        return best == null ? NO_MATCH_MESSAGE : best;
    }

    private boolean isMatch(String word) {
        if (word.charAt(word.length()-1) != currentPuzzle.charAt(2)) {
            return false;
        }
        if (word.charAt(0) != currentPuzzle.charAt(0)) {
            return false;
        }
        int foundAt = word.indexOf(currentPuzzle.charAt(1), 1);
        return 0 < foundAt && foundAt < word.length() - 1;
    }

    private String nextPuzzle() {
        int numLetters = 3;
        int alphabetSize = 26;
        StringBuilder builder = new StringBuilder(numLetters);
        for (int i = 0; i < numLetters; i++) {
            char j = (char) ('A' + getRandom().nextInt(alphabetSize));
            builder.append(j);
        }
        return builder.toString();
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Read a list of words from a reader.
     * @param reader contains the list of words, one per line. The reader will
     * be closed before the method returns.
     */
    public void readWordList(Reader reader) {
        try (BufferedReader lineReader = new BufferedReader(reader)) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (line.length() > 3) {
                    wordList.add(line.toUpperCase());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Reading word list failed.", e);
        }
    }
}
