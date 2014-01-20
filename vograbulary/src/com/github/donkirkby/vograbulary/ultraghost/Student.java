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
    private boolean isComputer;
    private WordList wordList;
    
    public Student(String name) {
        this(name, false);
    }
    
    public Student(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
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
    
    public int getScore() {
        return score;
    }
    
    public void addScore(int points) {
        score += points;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isComputer() {
        return isComputer;
    }
    
    @Override
    public String toString() {
        return String.format("%s %d", name, score);
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
     * @param solution TODO
     * @return true if the challenge has already been submitted.
     */
    public boolean prepareChallenge(String solution) {
        listener.askForSolution();
        return false;
    }
    
    /** Run a background search if needed, and return true if the search is
     * completed. 
     */
    public boolean runSearchBatch() {
        return true;
    }
}
