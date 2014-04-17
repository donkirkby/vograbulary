package com.github.donkirkby.vograbulary.ultraghost;

import java.util.Iterator;

import com.github.donkirkby.vograbulary.VograbularyPreferences;

public class ComputerStudent extends Student {
    private int searchBatchSize = 1;
    private int maxSearchBatchCount = Integer.MAX_VALUE;
    private int searchBatchCount;
    private int searchedWordsCount;
    private boolean isActiveStudent;
    private String currentPuzzle;
    private String bestSolution;
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
        int vocabularySize = preferences.getComputerStudentVocabularySize();
        int wordCount = Math.min(
                searchBatchSize, 
                vocabularySize 
                - searchedWordsCount);
        for (int i = 0; i < wordCount && itr.hasNext(); i++) {
            String word = itr.next();
            checkWord(word);
        }
        searchedWordsCount += wordCount;
        if (searchBatchCount >= maxSearchBatchCount 
                || ! itr.hasNext()
                || searchedWordsCount >= vocabularySize) {
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
