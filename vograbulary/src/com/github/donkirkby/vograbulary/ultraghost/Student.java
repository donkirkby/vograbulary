package com.github.donkirkby.vograbulary.ultraghost;

public class Student {
    public interface StudentListener {
        void submitSolution(String solution);
    }
    
    private String name;
    private int score;
    private boolean isComputer;
    
    public Student(String name) {
        this(name, false);
    }
    
    public Student(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
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
}
