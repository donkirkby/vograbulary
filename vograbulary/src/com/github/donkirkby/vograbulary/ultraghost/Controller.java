package com.github.donkirkby.vograbulary.ultraghost;

import java.io.Reader;
import java.util.Iterator;

import com.badlogic.gdx.utils.Timer.Task;

public class Controller {
    public static final String NO_MATCH_MESSAGE = "None";
    public static final int HUMAN_PLAYER_INDEX = 0;
    public static final int COMPUTER_PLAYER_INDEX = 1;
    
    private State state = new ResultState();
    private UltraghostRandom random = new UltraghostRandom();
    private View view;
    private String currentPuzzle;
    private WordList wordList = new WordList();
    private int searchBatchSize = 1;
    private Task searchTask;
    private String bestSolution;
    private String[] playerNames = new String[] {"Player", "Computer"};
    private int playerIndex = -1;


    public void next() {
        state = state.next();
    }

    public void checkAllWords() {
        for (String word : wordList) {
            checkWord(word);
        }
    }

    public UltraghostRandom getRandom() {
        return random;
    }

    public void setRandom(UltraghostRandom random) {
        this.random = random;
        random.loadWordList(wordList);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * Read a list of words from a reader.
     * @param reader contains the list of words, one per line. The reader will
     * be closed before the method returns.
     */
    public void readWordList(Reader reader) {
        wordList.read(reader);
        random.loadWordList(wordList);
    }

    public int getSearchBatchSize() {
        return searchBatchSize;
    }

    /**
     * Set the number of words to check each time the search task is triggered.
     * @param searchBatchSize
     */
    public void setSearchBatchSize(int searchBatchSize) {
        this.searchBatchSize = searchBatchSize;
    }

    /**
     * Create a timer task that will search the word list. Each run of the 
     * task examines a batch of words in the word list. Calling next() displays 
     * the best solution found so far.
     * @return a task for searching the word list.
     */
    private Task createSearchTask() {
        if (searchTask != null) {
            throw new IllegalStateException(
                    "A search task has already been created for this puzzle.");
        }
        if (currentPuzzle == null) {
            throw new IllegalStateException("No puzzle to search.");
        }
        searchTask = new SearchTask();
        return searchTask;
    }
    
    private void checkWord(String word) {
        if (wordList.isMatch(currentPuzzle, word)) {
            String previousSolution = bestSolution;
            if (previousSolution == null 
                    || word.length() < previousSolution.length()) {
                bestSolution = word;
            }
        }
    }

    private class SearchTask extends Task {
        private Iterator<String> itr = wordList.iterator();

        @Override
        public void run() {
            int wordCount = getSearchBatchSize();
            for (int i = 0; i < wordCount && itr.hasNext(); i++) {
                String word = itr.next();
                checkWord(word);
            }
            if ( ! itr.hasNext()) {
                cancel();
            }
        }
        
    }

    /** An abstract base class for all controller states to implement. */
    private abstract class State {
        public abstract State next();
    }
    
    /** The puzzle is displayed, and the player is thinking of a solution. */
    private class SolvingState extends State {

        @Override
        public State next() {
            if (searchTask != null)
            {
                searchTask.cancel();
                searchTask = null;
            }
            String computerSolution = bestSolution;
            if (computerSolution == null) {
                computerSolution = NO_MATCH_MESSAGE;
            }
            State nextState = new ImprovingState();
            if (playerIndex == HUMAN_PLAYER_INDEX) {
                String humanSolution = view.getSolution();
                WordResult solutionResult = 
                        wordList.checkSolution(currentPuzzle, humanSolution);
                if (solutionResult != WordResult.VALID 
                        && solutionResult != WordResult.SKIPPED) {
                    
                    view.setResult(solutionResult.toString());
                }
                else {
                    String challenge = bestSolution;
                    WordResult challengeResult = wordList.checkChallenge(
                            currentPuzzle, 
                            humanSolution, 
                            challenge);
                    if (challengeResult == WordResult.IMPROVED) {
                        view.setChallenge(challenge);
                    }
                    else {
                        view.setChallenge(NO_MATCH_MESSAGE);
                        challengeResult = WordResult.NOT_IMPROVED;
                    }
                    view.setResult(challengeResult.toString());
                }
                view.focusNextButton();
                // When computer challenges, we immediately switch to the
                // results state.
                nextState = nextState.next();
            }
            else {
                view.setSolution(computerSolution);
                view.setChallenge("");
                view.focusChallenge();
            }
            currentPuzzle = null;
            return nextState;
        }
    }
    
    /** A solution, skip, or challenge is displayed, and the player is thinking
     * of a better solution.
     */
    private class ImprovingState extends State {

        @Override
        public State next() {
            view.focusNextButton();
            String result = view.getResult();
            if (result != null && result.length() == 0) {
                WordResult result2 = wordList.checkChallenge(
                        currentPuzzle, 
                        view.getSolution(), 
                        view.getChallenge());
                view.setResult(result2.toString());
            }
            return new ResultState();
        }
        
    }
    
    /** A better solution has been entered, skipped, or timed out. The results
     * are displayed.
     */
    private class ResultState extends State {

        @Override
        public State next() {
            currentPuzzle = random.generatePuzzle();
            view.setPuzzle(currentPuzzle);
            int playerCount = 2;
            if (playerIndex < 0) {
                playerIndex = random.chooseStartingPlayer(playerCount);
            }
            else {
                playerIndex = (playerIndex+1) % playerCount;
            }
            view.setActivePlayer(playerNames[playerIndex]);
            if (playerNames[playerIndex].equals("Computer")) {
                view.focusNextButton();
            }
            else {
                view.focusSolution();
            }
            bestSolution = null;
            view.setSolution("");
            view.setChallenge("");
            view.setResult("");
            float intervalSeconds = 0.01f;
            float delaySeconds = intervalSeconds;
            view.schedule(createSearchTask(), delaySeconds, intervalSeconds);
            return new SolvingState();
        }
        
    }
}
