package com.github.donkirkby.vograbulary.ultraghost;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.ultraghost.Student.StudentListener;

public class Controller implements StudentListener {
//    //stopJesting
//    private static final float INTERVAL_SECONDS = 0.01f;
//    private static final int MATCH_SCORE = 21;
//    //resumeJesting
//
//    private UltraghostRandom random = new UltraghostRandom();
//    private View view;
//    private WordList wordList = new WordList();
//    private Task searchTask;
//    private List<Student> students = new ArrayList<Student>();
//
//    public void setRandom(UltraghostRandom random) {
//        this.random = random;
//        random.loadWordList(wordList);
//    }
//
//    public void setView(View view) {
//        this.view = view;
//    }
//    
//    public void addStudent(Student student) {
//        students.add(student);
//        student.setListener(this);
//        student.setWordList(wordList);
//    }
//    
//    public void clearStudents() {
//        students.clear();
//        view.setMatch(null);
//        view.focusNextButton();
//    }
//    
//    /**
//     * Read a list of words from a reader.
//     * @param reader contains the list of words, one per line. The reader will
//     * be closed before the method returns.
//     */
//    public void setWordList(WordList wordList) {
//        this.wordList = wordList;
//        random.loadWordList(wordList);
//        for (Student student : students) {
//            student.setWordList(wordList);
//        }
//    }
//    
//    private class SearchTask extends Task {
//        private List<Student> searchingStudents = new ArrayList<Student>(students);
//
//        @Override
//        public void run() {
//            Iterator<Student> itr = searchingStudents.iterator();
//            while(itr.hasNext()) {
//                Student student = itr.next();
//                boolean isStudentFinished = student.runSearchBatch();
//                if (isStudentFinished) {
//                    itr.remove();
//                }
//            }
//            if (searchingStudents.size() == 0) {
//                cancel();
//            }
//        }
//    }
//    
    @Override
    public void askForSolution() {
//        view.focusSolution();
    }
    
    @Override
    public void showThinking() {
//        view.showThinking();
    }
    
    @Override
    public void askForResponse() {
//        view.focusResponse();
    }
//    
//    public void start() {
//        checkMatch();
//        Puzzle puzzle = view.getMatch().createPuzzle(wordList);
//        watchPuzzle(puzzle);
//        view.refreshPuzzle();
//        for (Student student : students) {
//            student.startSolving(puzzle);
//        }
//        float delaySeconds = INTERVAL_SECONDS;
//        searchTask = new SearchTask();
//        view.schedule(searchTask, delaySeconds, INTERVAL_SECONDS);
//    }
//
//    /**
//     * Tell the controller to watch for changes in the current puzzle.
//     */
//    public void watchPuzzle(Puzzle puzzle) {
//        puzzle.addListener(new Puzzle.Listener() {
//            @Override
//            public void completed() {
//                Puzzle puzzle = view.getPuzzle();
//                puzzle.getOwner().addScore(puzzle.getResult().getScore());
//                String hint = puzzle.findNextBetter();
//                puzzle.setHint(hint == null ? null : "hint: " + hint);
//                view.focusNextButton();
//            }
//            
//            @Override
//            public void changed() {
//                view.refreshPuzzle();
//            }
//        });
//    }
//
//    private void checkMatch() {
//        Match match = view.getMatch();
//        if (match == null) {
//            match = new Match(
//                    MATCH_SCORE, 
//                    students.toArray(new Student[students.size()]));
//            match.setRandom(random);
//            view.setMatch(match);
//            for (Student student : students) {
//                student.setMatch(match);
//            }
//        }
//    }
//
//    public void solve() {
//        checkMatch();
//        Puzzle puzzle = view.getPuzzle();
//        WordResult result = puzzle.getResult();
//        if (result == WordResult.NOT_A_MATCH || 
//                result == WordResult.NOT_A_WORD ||
//                result == WordResult.TOO_SHORT ||
//                result == WordResult.TOO_SOON) {
//            view.focusSolution();
//        }
//        else {
//            for (Student student : students) {
//                if (student != puzzle.getOwner()) {
//                    student.prepareResponse();
//                }
//            }
//            if (searchTask != null) {
//                searchTask.cancel();
//            }
//        }
//        if (puzzle.getResponse() == null) {
//            view.refreshPuzzle();
//        }
//    }
//
//    public Match getMatch() {
//        checkMatch();
//        return view.getMatch();
//    }
//
//    public void cancelMatch() {
//        if (searchTask != null) {
//            searchTask.cancel();
//        }
//    }
}
