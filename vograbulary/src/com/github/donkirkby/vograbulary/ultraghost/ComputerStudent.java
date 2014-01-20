package com.github.donkirkby.vograbulary.ultraghost;

import java.util.Iterator;

public class ComputerStudent extends Student {
    private int searchBatchSize = 1;
    private int maxSearchBatchCount;
    private int searchBatchCount;
    private boolean isActiveStudent;
    private String currentPuzzle;
    private String bestSolution;
    private Iterator<String> itr;
    
    
    public ComputerStudent() {
        super("Computer", true);
    }
    
    public void setSearchBatchSize(int searchBatchSize) {
        this.searchBatchSize = searchBatchSize;
    }
    
    public void setMaxSearchBatchCount(int maxSearchBatchCount) {
        this.maxSearchBatchCount = maxSearchBatchCount;
    }
    
    @Override
    public void startSolving(String puzzle, boolean isActiveStudent) {
        currentPuzzle = puzzle;
        bestSolution = null;
        this.isActiveStudent = isActiveStudent;
        if (isActiveStudent) {
            getListener().showThinking();
        }
        itr = getWordList().iterator();
    }
    
    @Override
    public boolean runSearchBatch() {
        searchBatchCount++;
        int wordCount = searchBatchSize;
        for (int i = 0; i < wordCount && itr.hasNext(); i++) {
            String word = itr.next();
            checkWord(word);
        }
        if (searchBatchCount >= maxSearchBatchCount || ! itr.hasNext()) {
            if (isActiveStudent) {
                getListener().submitSolution(bestSolution);
            }
        }
        return ! itr.hasNext();
    }
    
    private void checkWord(String word) {
        if (getWordList().isMatch(currentPuzzle, word)) {
            String previousSolution = bestSolution;
            if (previousSolution == null 
                    || word.length() < previousSolution.length()
                    || (word.length() == previousSolution.length()
                        && word.compareTo(previousSolution) < 0)) {
                bestSolution = word;
            }
        }
    }
    
    @Override
    public boolean prepareChallenge(String humanSolution) {
        String challenge = bestSolution;
        String noChallenge = null;
        WordResult challengeResult = getWordList().checkChallenge(
                currentPuzzle, 
                humanSolution, 
                challenge);
        WordResult noChallengeResult = getWordList().checkChallenge(
                currentPuzzle, 
                humanSolution, 
                noChallenge);
        if (challengeResult.getScore() < noChallengeResult.getScore()) {
            getListener().submitChallenge(challenge, challengeResult);
        }
        else {
            getListener().submitChallenge(Controller.NO_MATCH_MESSAGE, WordResult.NOT_IMPROVED);
        }
        return true;
    }
}
