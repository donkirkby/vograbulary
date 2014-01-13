package com.github.donkirkby.vograbulary.ultraghost;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Iterator;

import com.badlogic.gdx.utils.Timer.Task;

public class Controller {
    public static final String NO_MATCH_MESSAGE = "None";
    public static final int HUMAN_STUDENT_INDEX = 0;
    public static final int COMPUTER_STUDENT_INDEX = 1;
    private static final int STUDENT_COUNT = 2;
    
    private State state = new StartState();
    private UltraghostRandom random = new UltraghostRandom();
    private View view;
    private String currentPuzzle;
    private WordList wordList = new WordList();
    private int searchBatchSize = 1;
    private int maxSearchBatchForComputer = Integer.MAX_VALUE;
    private int searchBatchCount;
    private Task searchTask;
    private String bestSolution;
    private Student[] students = new Student[] {
            new Student("Student"),
            new Student("Computer", true)
    };
    private int startingStudent;
    private int studentIndex;

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
    
    public int getMaxSearchBatchForComputer() {
        return maxSearchBatchForComputer;
    }
    
    /**
     * Set the number of search batches that will run before a computer student
     * displays its best solution.
     */
    public void setMaxSearchBatchForComputer(int maxSearchBatchForComputer) {
        this.maxSearchBatchForComputer = maxSearchBatchForComputer;
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
        searchBatchCount = 0;
        searchTask = new SearchTask();
        return searchTask;
    }
    
    private void checkWord(String word) {
        if (wordList.isMatch(currentPuzzle, word)) {
            String previousSolution = bestSolution;
            if (previousSolution == null 
                    || word.length() < previousSolution.length()
                    || (word.length() == previousSolution.length()
                        && word.compareTo(previousSolution) < 0)) {
                bestSolution = word;
            }
        }
    }

    private class SearchTask extends Task {
        private Iterator<String> itr = wordList.iterator();

        @Override
        public void run() {
            searchBatchCount++;
            int wordCount = getSearchBatchSize();
            for (int i = 0; i < wordCount && itr.hasNext(); i++) {
                String word = itr.next();
                checkWord(word);
            }
            if ( ! itr.hasNext()) {
                cancel();
            }
            if (searchBatchCount >= maxSearchBatchForComputer || ! itr.hasNext()) {
                if (students[studentIndex].isComputer()) {
                    next();
                }
            }
        }
    }

    private void addScore(WordResult result) {
        students[studentIndex].addScore(result.getScore());
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        for (int i = 0; i < students.length; i++) {
            int scoreIndex = (startingStudent + i) % students.length;
            Student student = students[scoreIndex];
            if (i > 0) {
                printer.println();
            }
            printer.print(student);
        }
        view.setScores(writer.toString());
        printer.close();
    }
    
    /** An abstract base class for all controller states to implement. */
    private abstract class State {
        public abstract State next();
    }
    
    /** The puzzle is displayed, and the student is thinking of a solution. */
    private class SolvingState extends State {

        @Override
        public State next() {
            searchTask.cancel();
            searchTask = null;
            String computerSolution = bestSolution;
            if (computerSolution == null) {
                computerSolution = NO_MATCH_MESSAGE;
            }
            State nextState = new ImprovingState();
            if (studentIndex == HUMAN_STUDENT_INDEX) {
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
                    if (challengeResult.getScore() < WordResult.LONGER.getScore()) {
                        view.setChallenge(challenge);
                    }
                    else {
                        view.setChallenge(NO_MATCH_MESSAGE);
                        challengeResult = WordResult.NOT_IMPROVED;
                    }
                    view.setResult(challengeResult.toString());
                    addScore(challengeResult);
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
    
    /** A solution, skip, or challenge is displayed, and the student is thinking
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
                addScore(result2);
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
            Student student = nextStudent();
            view.setActiveStudent(student.getName());
            if (student.isComputer()) {
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

        protected Student nextStudent() {
            studentIndex = (studentIndex+1) % STUDENT_COUNT;
            return students[studentIndex];
        }
        
    }
    
    /** The challenge has just started. */
    private class StartState extends ResultState {
        @Override
        protected Student nextStudent() {
            studentIndex = startingStudent =
                    random.chooseStartingStudent(STUDENT_COUNT);
            return students[studentIndex];
        }
    }
}
