package com.github.donkirkby.vograbulary.core.ultraghost;

import java.util.Iterator;

import com.github.donkirkby.vograbulary.core.VograbularyPreferences;

public class ComputerStudent extends Student {
    private int searchBatchSize = 1;
    private int maxSearchBatchCount = Integer.MAX_VALUE;
    private int searchBatchCount;
    private int searchedWordsCount;
    private Puzzle currentPuzzle;
    private Puzzle searchPuzzle; // used to search for the best solution.
    private Iterator<String> itr;
    private VograbularyPreferences preferences;
    
    public ComputerStudent(VograbularyPreferences preferences) {
        super("Computer");
        this.preferences = preferences;
    }
    
    public void setSearchBatchSize(int searchBatchSize) {
        this.searchBatchSize = searchBatchSize;
    }
    
    public int getSearchBatchSize() {
        return searchBatchSize;
    }
    
    public void setMaxSearchBatchCount(int maxSearchBatchCount) {
        this.maxSearchBatchCount = maxSearchBatchCount;
        searchBatchSize = 
                preferences.getComputerStudentVocabularySize() 
                / maxSearchBatchCount;
    }
    
    @Override
    public void startSolving(Puzzle puzzle) {
        currentPuzzle = puzzle;
        searchPuzzle = new Puzzle(puzzle.getLetters(), this);
        searchPuzzle.setMinimumWordLength(puzzle.getMinimumWordLength());
        searchPuzzle.setPreviousWord(puzzle.getPreviousWord());
        searchPuzzle.setSolution(Puzzle.NO_SOLUTION);
        searchBatchCount = 0;
        searchedWordsCount = 0;
        if (currentPuzzle.getOwner() == this) {
            getListener().showThinking();
        }
        itr = getWordList().iterator();
    }
    
    @Override
    public boolean runSearchBatch() {
        checkCurrentPuzzle();
        searchBatchCount++;
        int vocabularySize = preferences.getComputerStudentVocabularySize();
        int wordCount = Math.min(
                searchBatchSize, 
                vocabularySize 
                - searchedWordsCount);
        for (int i = 0; i < wordCount && itr.hasNext(); i++) {
            searchPuzzle.setResponse(itr.next());
            if (searchPuzzle.isImproved()) {
                searchPuzzle.setSolution(searchPuzzle.getResponse());
            }
        }
        searchedWordsCount += wordCount;
        if (searchBatchCount >= maxSearchBatchCount 
                || ! itr.hasNext()
                || searchedWordsCount >= vocabularySize) {
            if (currentPuzzle.getOwner() == this) {
                currentPuzzle.setSolution(searchPuzzle.getSolution());
                getListener().askForResponse();
                return true;
            }
        }
        return ! itr.hasNext();
    }

    private void checkCurrentPuzzle() {
        Match match = getMatch();
        if (match != null && match.getPuzzle() != currentPuzzle) {
            startSolving(match.getPuzzle());
        }
    }
    
    @Override
    public void prepareResponse() {
        checkCurrentPuzzle();
        if (currentPuzzle == null) {
            throw new IllegalStateException(
                    "Called prepareResponse() before startSolving().");
        }
        String challenge = searchPuzzle.getSolution();
        currentPuzzle.setResponse(challenge);
        if ( ! currentPuzzle.isImproved()) {
            currentPuzzle.setResponse(Puzzle.NO_SOLUTION);
        }
    }
    
    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }
}
