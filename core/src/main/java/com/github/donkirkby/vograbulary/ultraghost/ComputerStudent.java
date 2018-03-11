package com.github.donkirkby.vograbulary.ultraghost;

import java.util.Iterator;

import com.github.donkirkby.vograbulary.VograbularyPreferences;

public class ComputerStudent extends Student {
    private static final long serialVersionUID = 2114858077675128651L;
    private int searchBatchSize = 1;
    private int maxSearchBatchCount = Integer.MAX_VALUE;
    private int vocabularySize;
    private transient int searchBatchCount;
    private transient int searchedWordsCount;
    private transient Puzzle currentPuzzle;
    private transient Puzzle searchPuzzle; // used to search for the best solution.
    private transient Iterator<String> itr;
    
    public ComputerStudent(VograbularyPreferences preferences) {
        super("Computer");
        vocabularySize = preferences.getComputerStudentVocabularySize();
    }
    
    public void setSearchBatchSize(int searchBatchSize) {
        this.searchBatchSize = searchBatchSize;
    }
    
    public int getSearchBatchSize() {
        return searchBatchSize;
    }
    
    public void setMaxSearchBatchCount(int maxSearchBatchCount) {
        this.maxSearchBatchCount = maxSearchBatchCount;
        searchBatchSize = vocabularySize / maxSearchBatchCount;
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
        int wordCount = Math.min(
                searchBatchSize, 
                vocabularySize 
                - searchedWordsCount);
        for (int i = 0; i < wordCount && itr.hasNext(); i++) {
            searchPuzzle.setResponse(itr.next());
            if (searchPuzzle.getResult().isImproved()) {
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
        if ( ! currentPuzzle.getResult().isImproved()) {
            currentPuzzle.setResponse(Puzzle.NO_SOLUTION);
        }
    }
    
    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }
}
