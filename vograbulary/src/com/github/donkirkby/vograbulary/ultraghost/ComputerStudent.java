package com.github.donkirkby.vograbulary.ultraghost;

import java.util.Iterator;

public class ComputerStudent extends Student {
    private int searchBatchSize = 1;
    private int maxSearchBatchCount = Integer.MAX_VALUE;
    private int searchBatchCount;
    private boolean isActiveStudent;
    private String currentPuzzle;
    private String bestSolution;
    private Iterator<String> itr;
    
    
    public ComputerStudent() {
        super("Computer");
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
        searchBatchCount = 0;
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
    }
}
