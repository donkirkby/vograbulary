package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.ultraghost.View;

public class UltraghostController {
    public static final String NO_MATCH_MESSAGE = "None";
    
    public enum State {PUZZLE, SOLUTION};
    
    private UltraghostRandom random = new UltraghostRandom();
    private View view;
    private String currentPuzzle;
    private ArrayList<String> wordList = new ArrayList<String>();
    private int searchBatchSize = 1;
    private Task searchTask;
    private String bestSolution;
    private String[] playerNames = new String[] {"Player", "Computer"};
    private int playerIndex = -1;
    
    public State getState() {
        return currentPuzzle != null ? State.PUZZLE : State.SOLUTION;
    }

    public void next() {
        if (currentPuzzle == null)
        {
            displayPuzzle();
            return;
        }
        view.focusNextButton();
        if (searchTask != null)
        {
            searchTask.cancel();
            searchTask = null;
        }
        currentPuzzle = null;
        String solution = bestSolution;
        if (solution == null) {
            solution = NO_MATCH_MESSAGE;
        }
        view.setSolution(solution);
    }

    private void displayPuzzle() {
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
        float intervalSeconds = 0.01f;
        float delaySeconds = intervalSeconds;
        view.schedule(createSearchTask(), delaySeconds, intervalSeconds);
    }

    public void checkAllWords() {
        for (String word : wordList) {
            checkWord(word);
        }
    }

    private boolean isMatch(String word) {
        String puzzle = currentPuzzle;
        if (puzzle == null) {
            return false;
        }
        if (word.charAt(word.length()-1) != puzzle.charAt(2)) {
            return false;
        }
        if (word.charAt(0) != puzzle.charAt(0)) {
            return false;
        }
        int foundAt = word.indexOf(puzzle.charAt(1), 1);
        return 0 < foundAt && foundAt < word.length() - 1;
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
        try {
            BufferedReader lineReader = new BufferedReader(reader);
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
        if (isMatch(word)) {
            String previousSolution = bestSolution;
            if (previousSolution == null 
                    || word.length() < previousSolution.length()) {
                bestSolution = word;
            }
        }
    }

    private class SearchTask extends Task {
        private int index;

        @Override
        public void run() {
            int wordCount = getSearchBatchSize();
            for (int i = 0; i < wordCount && index < wordList.size(); i++) {
                String word = wordList.get(index);
                checkWord(word);
                index++;
            }
            if (index >= wordList.size()) {
                cancel();
            }
        }
        
    }
}
