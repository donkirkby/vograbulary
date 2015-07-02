package com.github.donkirkby.vograbulary.ultraghost;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.donkirkby.vograbulary.Scheduler;
import com.github.donkirkby.vograbulary.VograbularyPreferences;
import com.github.donkirkby.vograbulary.ultraghost.Student.StudentListener;

public class Controller implements StudentListener {
    //stopJesting
    public static final int SCORE_MILLISECONDS = 100;
    private static final int SEARCH_MILLISECONDS = 10;
    private static final int MATCH_SCORE = 300;
    //resumeJesting

    private UltraghostRandom random = new UltraghostRandom();
    private VograbularyPreferences preferences;
    private UltraghostScreen screen;
    private WordList wordList = new WordList();
    private Runnable scoreTask;
    private Runnable searchTask;
    private Scheduler scheduler;
    private List<Student> students = new ArrayList<Student>();

    public void setRandom(UltraghostRandom random) {
        this.random = random;
        random.loadWordList(wordList);
    }
    
    public void setPreferences(VograbularyPreferences preferences) {
        this.preferences = preferences;
    }
    public VograbularyPreferences getPreferences() {
        return preferences;
    }

    public void setScreen(UltraghostScreen screen) {
        this.screen = screen;
    }
    
    public void addStudent(Student student) {
        students.add(student);
        student.setListener(this);
        student.setWordList(wordList);
        screen.setMatch(null);
    }
    
    public void clearStudents() {
        students.clear();
        screen.setMatch(null);
        screen.focusNextButton();
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
    
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    
    private class ScoreTask implements Runnable {
        @Override
        public void run() {
            screen.getMatch().getPuzzle().adjustScore(SCORE_MILLISECONDS / 1000.0f);
            screen.refreshScore();
        }
    }
    
    private class SearchTask implements Runnable {
        private List<Student> searchingStudents = new ArrayList<Student>(students);
        private Scheduler scheduler;
        
        public SearchTask(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

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
                scheduler.cancel(this);
            }
        }
    }
    
    @Override
    public void askForSolution() {
        screen.focusSolution();
    }
    
    @Override
    public void showThinking() {
        screen.showThinking();
    }
    
    @Override
    public void askForResponse() {
        screen.focusResponse();
    }
    
    public void start() {
        Puzzle puzzle = getMatch().createPuzzle(wordList);
        watchPuzzle(puzzle);
        screen.refreshPuzzle();
        for (Student student : students) {
            student.startSolving(puzzle);
        }
        scoreTask = new ScoreTask();
        scheduler.scheduleRepeating(scoreTask, SCORE_MILLISECONDS);
        searchTask = new SearchTask(scheduler);
        scheduler.scheduleRepeating(searchTask, SEARCH_MILLISECONDS);
    }

    /**
     * Tell the controller to watch for changes in the current puzzle.
     */
    public void watchPuzzle(Puzzle puzzle) {
        puzzle.addListener(new Puzzle.Listener() {
            @Override
            public void completed() {
                Puzzle puzzle = getMatch().getPuzzle();
                scheduler.cancel(scoreTask);
                puzzle.getOwner().addScore(puzzle.getScore());
                String hint = puzzle.findNextBetter();
                puzzle.setHint(hint == null ? null : "hint: " + hint);
                screen.focusNextButton();
            }
            
            @Override
            public void changed() {
                screen.refreshPuzzle();
            }
        });
    }

    public Match getMatch() {
        Match match = screen.getMatch();
        if (match == null) {
            match = new Match(
                    MATCH_SCORE, 
                    students.toArray(new Student[students.size()]));
            match.setRandom(random);
            match.setMinimumWordLength(
                    preferences.getUltraghostMinimumWordLength());
            screen.setMatch(match);
            for (Student student : students) {
                student.setMatch(match);
            }
        }
        return match;
    }

    public void solve() {
        Puzzle puzzle = getMatch().getPuzzle();
        WordResult result = puzzle.getResult();
        if (result == WordResult.NOT_A_MATCH || 
                result == WordResult.NOT_A_WORD ||
                result == WordResult.TOO_SHORT ||
                result == WordResult.TOO_SOON) {
            screen.focusSolution();
        }
        else {
            for (Student student : students) {
                if (student != puzzle.getOwner()) {
                    student.prepareResponse();
                }
            }
            if (searchTask != null) {
                scheduler.cancel(searchTask);
            }
        }
        if (puzzle.getResponse() == null) {
            screen.refreshPuzzle();
        }
    }

    public void cancelMatch() {
        if (searchTask != null) {
            scheduler.cancel(searchTask);
        }
    }
}
