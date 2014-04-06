package com.github.donkirkby.vograbulary.ultraghost;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.ultraghost.Student.StudentListener;

public class Controller implements StudentListener {
    private UltraghostRandom random = new UltraghostRandom();
    private View view;
    private WordList wordList = new WordList();
    private Task searchTask;
    private List<Student> students = new ArrayList<Student>();
    private Match match;

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
        //stopJesting
        float intervalSeconds = 0.01f;
        //resumeJesting
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
