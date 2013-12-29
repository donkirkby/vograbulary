package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.utils.Timer.Task;

public class UltraghostController {
    public static final String NO_MATCH_MESSAGE = "None";
    
    public enum State {PUZZLE, SOLUTION};
    
    private Random random = new Random();
    private String currentPuzzle;
    private ArrayList<String> wordList = new ArrayList<String>();
    private int searchBatchSize;
    private Task searchTask;
    private String bestSolution;
    
    public State getState() {
        return currentPuzzle != null ? State.PUZZLE : State.SOLUTION;
    }

    public String next() {
        if (currentPuzzle == null)
        {
            currentPuzzle = nextPuzzle();
            return currentPuzzle;
        }
        if (searchTask == null)
        {
            for (String word : wordList) {
                checkWord(word);
            }
        }
        else
        {
            searchTask.cancel();
            searchTask = null;
        }
        currentPuzzle = null;
        return bestSolution == null ? NO_MATCH_MESSAGE : bestSolution;
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

    public Task createSearchTask() {
        return createSearchTask(1);
    }
    
    /**
     * Create a timer task that will search the word list. Each run of the 
     * task examines one word in the word list. Calling next() returns the
     * best solution found so far.
     * @return a task for searching the word list.
     */
    public Task createSearchTask(int searchBatchSize) {
        if (searchTask != null) {
            throw new IllegalStateException(
                    "A search task has already been created for this puzzle.");
        }
        if (currentPuzzle == null) {
            throw new IllegalStateException("No puzzle to search.");
        }
        this.searchBatchSize = searchBatchSize;
        searchTask = new SearchTask();
        return searchTask;
    }
    
    private void checkWord(String word) {
        if (isMatch(word)) {
            if (bestSolution == null) {
                bestSolution = word;
            }
            else if (word.length() < bestSolution.length()) {
                bestSolution = word;
            }
        }
    }

    private class SearchTask extends Task {
        private int index;

        @Override
        public void run() {
            int wordCount = searchBatchSize;
            for (int i = 0; i < wordCount && index < wordList.size(); i++) {
                String word = wordList.get(index);
                if (index % 10000 == 0) {
                    System.out.println("searching " + index + ": " + word);
                }
                checkWord(word);
                index++;
            }
            if (index >= wordList.size()) {
                cancel();
            }
        }
        
    }
}
