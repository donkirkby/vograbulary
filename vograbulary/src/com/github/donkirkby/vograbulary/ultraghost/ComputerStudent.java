package com.github.donkirkby.vograbulary.ultraghost;

import java.util.Iterator;

import com.github.donkirkby.vograbulary.Configuration;

public class ComputerStudent extends Student {
    private int searchBatchSize = 1;
    private int maxSearchBatchCount = Integer.MAX_VALUE;
    private int searchBatchCount;
    private int searchedWordsCount;
    private boolean isActiveStudent;
    private String currentPuzzle;
    private String bestSolution;
    private Iterator<String> itr;
    private Configuration configuration;
    
    public ComputerStudent() {
        this(new Configuration());
    }
    
    public ComputerStudent(Configuration configuration) {
        super("Computer");
        this.configuration = configuration;
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
                configuration.getVocabularySize() / maxSearchBatchCount;
    }
    
    @Override
    public void startSolving(String puzzle, boolean isActiveStudent) {
        currentPuzzle = puzzle;
        bestSolution = null;
        searchBatchCount = 0;
        searchedWordsCount = 0;
        this.isActiveStudent = isActiveStudent;
        if (isActiveStudent) {
            getListener().showThinking();
        }
        itr = getWordList().iterator();
    }
    
    @Override
    public boolean runSearchBatch() {
        searchBatchCount++;
        int wordCount = Math.min(
                searchBatchSize, 
                configuration.getVocabularySize() - searchedWordsCount);
        for (int i = 0; i < wordCount && itr.hasNext(); i++) {
            String word = itr.next();
            checkWord(word);
        }
        searchedWordsCount += wordCount;
        if (searchBatchCount >= maxSearchBatchCount 
                || ! itr.hasNext()
                || searchedWordsCount >= configuration.getVocabularySize()) {
            if (isActiveStudent) {
                getListener().submitSolution(
                        bestSolution == null
                        ? ""
                        : bestSolution);
                return true;
            }
        }
        return ! itr.hasNext();
    }
    
    private void checkWord(String word) {
        if (getWordList().isMatch(currentPuzzle, word)) {
            String previousSolution = bestSolution;
            WordResult comparison = previousSolution == null
                    ? WordResult.SHORTER
                    : getWordList().challengeWord(previousSolution, word);
            if (comparison == WordResult.SHORTER 
                    || comparison == WordResult.EARLIER) {
                bestSolution = word;
            }
        }
    }
    
    @Override
    public void prepareChallenge(String humanSolution) {
        String challenge = bestSolution;
        String noChallenge = null;
        WordResult challengeResult = getWordList().checkResponse(
                currentPuzzle, 
                humanSolution, 
                challenge);
        WordResult noChallengeResult = getWordList().checkResponse(
                currentPuzzle, 
                humanSolution, 
                noChallenge);
        if (challengeResult.getScore() < noChallengeResult.getScore()) {
            getListener().submitChallenge(challenge, challengeResult);
        }
        else {
            getListener().submitChallenge("", WordResult.NOT_IMPROVED);
        }
    }
    
    public String getCurrentPuzzle() {
        return currentPuzzle;
    }
}
