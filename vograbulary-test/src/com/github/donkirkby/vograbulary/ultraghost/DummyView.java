package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.utils.Timer.Task;

public class DummyView extends View {
    private String puzzle = "";
    private String solution = "";
    private String challenge = "";
    private String result = "";
    private String scores = "";
    private Task searchTask;
    private String currentFocus = "";

    @Override
    public void schedule(Task task, float delaySeconds, float intervalSeconds) {
        this.searchTask = task;
    }
    
    public Task getSearchTask() {
        return searchTask;
    }

    @Override
    public void setPuzzle(String letters) {
        puzzle = letters;
    }
    
    public String getPuzzle() {
        return puzzle;
    }

    @Override
    public void setActivePlayer(String name) {
    }

    @Override
    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public String getSolution() {
        return solution;
    }
    
    @Override
    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
    
    @Override
    public String getChallenge() {
        return challenge;
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }
    
    @Override
    public String getResult() {
        return result;
    }
    
    @Override
    public void setScores(String scores) {
        this.scores = scores;
    }
    
    public String getScores() {
        return scores;
    }
    
    @Override
    public void focusSolution() {
        currentFocus = "solution";
    }
    
    @Override
    public void focusChallenge() {
        currentFocus = "challenge";
    }
    
    @Override
    public void focusNextButton() {
        currentFocus = "next";
    }
    
    public String getCurrentFocus() {
        return currentFocus;
    }
}
