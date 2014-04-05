package com.github.donkirkby.vograbulary.ultraghost;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.ultraghost.Student.StudentListener;

public class Controller implements StudentListener {
    private State state = new StartState();
    private UltraghostRandom random = new UltraghostRandom();
    private View view;
    private String currentPuzzle;
    private WordList wordList = new WordList();
    private Task searchTask;
    private List<Student> students = new ArrayList<Student>();
    private int startingStudent = -1;
    private int activeStudentIndex;
    private Match match;

    public void next() {
        state.next();
    }

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
        match = null;
        view.setActiveStudent(" ");
        view.focusNextButton();
        state = new StartState();
    }
    
    /**
     * Read a list of words from a reader.
     * @param reader contains the list of words, one per line. The reader will
     * be closed before the method returns.
     */
    public void setWordList(WordList wordList) {
        this.wordList = wordList;
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
                Student student = itr.next();
                boolean isStudentFinished = student.runSearchBatch();
                if (isStudentFinished) {
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
        StringBuilder writer = new StringBuilder();
        for (int i = 0; i < students.size(); i++) {
            int scoreIndex = (startingStudent + i) % students.size();
            Student student = students.get(scoreIndex);
            if (i > 0) {
                writer.append("\n");
            }
            writer.append(student.toString());
        }
        view.setScores(writer.toString());
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
            state = new ImprovingState();
            Student inactiveStudent = students.get(1 - activeStudentIndex);
            String solution = view.getSolution();
            WordResult solutionResult = 
                    wordList.checkSolution(currentPuzzle, solution);
            if (solutionResult != WordResult.VALID 
                    && solutionResult != WordResult.SKIPPED) {
                
                view.setResult(solutionResult.toString());
                state.next();
            }
            else {
                inactiveStudent.prepareChallenge(solution);
            }
            currentPuzzle = null;
            return null;
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
                WordResult result2 = wordList.checkResponse(
                        currentPuzzle, 
                        view.getSolution(), 
                        view.getChallenge());
                view.setResult(result2.toString());
                addScore(result2);
            }
            state = new ResultState();
            return null;
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
            state = new SolvingState();
            return null;
        }

        protected Student nextStudent() {
            activeStudentIndex = (activeStudentIndex+1) % students.size();
            return students.get(activeStudentIndex);
        }
        
    }
    
    /** The challenge has just started. */
    private class StartState extends ResultState {
        @Override
        protected Student nextStudent() {
            activeStudentIndex = startingStudent =
                    random.chooseStartingStudent(students.size());
            return students.get(activeStudentIndex);
        }
    }

    @Override
    public void askForSolution() {
        view.focusSolution();
    }
    
    @Override
    public void submitSolution(String solution) {
        view.getPuzzle().setSolution(solution);
        view.refreshPuzzle();
        view.focusResponse();
    }

    @Override
    public void showThinking() {
        view.showThinking();
    }
    
    @Override
    public void askForChallenge() {
        view.focusResponse();
    }
    
    @Override
    public void submitChallenge(String challenge, WordResult challengeResult) {
        view.getPuzzle().setResponse(challenge);
        respond();
    }

    public void start() {
        if (match == null) {
            match = new Match(
                    21, 
                    students.toArray(new Student[students.size()]));
            match.setRandom(random);
        }
        Puzzle puzzle = match.createPuzzle(wordList);
        view.setMatch(match);
        for (Student student : students) {
            student.startSolving(
                    puzzle.getLetters(), 
                    student == puzzle.getOwner());
        }
        float intervalSeconds = 0.01f;
        float delaySeconds = intervalSeconds;
        searchTask = new SearchTask();
        view.schedule(searchTask, delaySeconds, intervalSeconds);
    }

    public void solve() {
        Puzzle puzzle = view.getPuzzle();
        for (Student student : students) {
            if (student != puzzle.getOwner()) {
                student.prepareChallenge(puzzle.getSolution());
            }
        }
        if (searchTask != null) {
            searchTask.cancel();
        }
    }

    public void respond() {
        Puzzle puzzle = view.getPuzzle();
        puzzle.getOwner().addScore(puzzle.getResult().getScore());
        view.focusNextButton();
        view.refreshPuzzle();
    }
    
    public Match getMatch() {
        return match;
    }
}
