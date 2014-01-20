package com.github.donkirkby.vograbulary.ultraghost;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.ultraghost.Student.StudentListener;

public class Controller implements StudentListener {
    public static final String NO_MATCH_MESSAGE = "None";
    private static final int STUDENT_COUNT = 2;
    
    private State state = new StartState();
    private UltraghostRandom random = new UltraghostRandom();
    private View view;
    private String currentPuzzle;
    private WordList wordList = new WordList();
    private Task searchTask;
    private List<Student> students = new ArrayList<Student>();
    private int startingStudent;
    private int activeStudentIndex;

    public void next() {
        state = state.next();
    }

//    public void checkAllWords() {
//        for (String word : wordList) {
//            checkWord(word);
//        }
//    }

    public void setRandom(UltraghostRandom random) {
        this.random = random;
        random.loadWordList(wordList);
    }

    public void setView(View view) {
        this.view = view;
    }
    
    public void addStudent(Student student) {
        students.add(student);
        student.setListener(this);
        student.setWordList(wordList);
    }
    
    public void clearStudents() {
        students.clear();
    }
    
    /**
     * Read a list of words from a reader.
     * @param reader contains the list of words, one per line. The reader will
     * be closed before the method returns.
     */
    public void readWordList(Reader reader) {
        wordList.read(reader);
        random.loadWordList(wordList);
        for (Student student : students) {
            student.setWordList(wordList);
        }
    }

    /**
     * Create a timer task that will search the word list. Each run of the 
     * task examines a batch of words in the word list. Calling next() displays 
     * the best solution found so far.
     * @return a task for searching the word list.
     */
    private Task createSearchTask() {
        searchTask = new SearchTask();
        int studentIndex = 0;
        for (Student student : students) {
            student.startSolving(currentPuzzle, studentIndex == activeStudentIndex);
            studentIndex++;
        }
        return searchTask;
    }
    
    private class SearchTask extends Task {
        private List<Student> searchingStudents = new ArrayList<Student>(students);

        @Override
        public void run() {
            Iterator<Student> itr = searchingStudents.iterator();
            while(itr.hasNext()) {
                if (itr.next().runSearchBatch()) {
                    itr.remove();
                }
            }
            if (searchingStudents.size() == 0) {
                cancel();
            }
        }
    }

    private void addScore(WordResult result) {
        students.get(activeStudentIndex).addScore(result.getScore());
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        for (int i = 0; i < students.size(); i++) {
            int scoreIndex = (startingStudent + i) % students.size();
            Student student = students.get(scoreIndex);
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
            State nextState = new ImprovingState();
            Student inactiveStudent = students.get(1 - activeStudentIndex);
            String solution = view.getSolution();
            WordResult solutionResult = 
                    wordList.checkSolution(currentPuzzle, solution);
            if (solutionResult != WordResult.VALID 
                    && solutionResult != WordResult.SKIPPED) {
                
                view.setResult(solutionResult.toString());
                nextState = nextState.next();
            }
            else if (inactiveStudent.prepareChallenge(solution)) {
                // When computer challenges, we immediately switch to the
                // results state.
                nextState = nextState.next();
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
            view.setSolution("");
            view.setChallenge("");
            view.setResult("");
            float intervalSeconds = 0.01f;
            float delaySeconds = intervalSeconds;
            view.schedule(createSearchTask(), delaySeconds, intervalSeconds);
            return new SolvingState();
        }

        protected Student nextStudent() {
            activeStudentIndex = (activeStudentIndex+1) % STUDENT_COUNT;
            return students.get(activeStudentIndex);
        }
        
    }
    
    /** The challenge has just started. */
    private class StartState extends ResultState {
        @Override
        protected Student nextStudent() {
            activeStudentIndex = startingStudent =
                    random.chooseStartingStudent(STUDENT_COUNT);
            return students.get(activeStudentIndex);
        }
    }

    @Override
    public void askForSolution() {
        view.focusSolution();
    }
    
    @Override
    public void submitSolution(String solution) {
        view.setSolution(solution == null ? NO_MATCH_MESSAGE : solution);
    }

    @Override
    public void showThinking() {
        view.focusNextButton();
    }
    
    @Override
    public void askForChallenge() {
        view.focusChallenge();
    }
    
    @Override
    public void submitChallenge(String challenge, WordResult challengeResult) {
        view.setChallenge(challenge);
        addScore(challengeResult);
        view.focusNextButton();
    }
}
