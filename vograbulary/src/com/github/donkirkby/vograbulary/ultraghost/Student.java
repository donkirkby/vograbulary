package com.github.donkirkby.vograbulary.ultraghost;

public class Student {
    private String name;
    private int score;
    
    public Student(String name) {
        this.name = name;
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
    
    @Override
    public String toString() {
        return String.format("%s %d", name, score);
    }
}
