package com.github.donkirkby.vograbulary.ultraghost;

public class Student {
    public interface StudentListener {
        void askForSolution();
        
        void submitSolution(String solution);
        
        void askForChallenge();
        
        void submitChallenge(String challenge, WordResult challengeResult);
        
        void showThinking();
    }
    
    private StudentListener listener;
    private String name;
    private int score;
    private int scoreCount;
    private WordList wordList;
    
    public Student(String name) {
        this.name = name;
    }
    
    public void setListener(StudentListener listener) {
        this.listener = listener;
    }
    
    public StudentListener getListener() {
        return listener;
    }
    
    public void setWordList(WordList wordList) {
        this.wordList = wordList;
    }
    
    public WordList getWordList() {
        return wordList;
    }
    
    public void addScore(int points) {
        score += points;
        scoreCount++;
    }
    
    public int getScore() {
        return score;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name + " " + score;
    }

    /**
     * Start searching for solutions to the current puzzle. This is called for
     * all students.
     * @param puzzle the puzzle to find answers for
     * @param isActiveStudent true if this student is the active one
     */
    public void startSolving(String puzzle, boolean isActiveStudent) {
        if (isActiveStudent) {
            listener.askForSolution();
        }
    }
    
    /** Get ready to submit a challenge. This is only called for the inactive
     * student.
     * @param solution the solution to beat.
     */
    public void prepareChallenge(String solution) {
        listener.askForChallenge();
    }
    
    /** Run a background search if needed, and return true if the search is
     * completed. 
     */
    public boolean runSearchBatch() {
        return true;
    }
    
    public int getScoreCount() {
        return scoreCount;
    }
}
