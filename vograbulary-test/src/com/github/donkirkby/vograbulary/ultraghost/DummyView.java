package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.utils.Timer.Task;

public class DummyView extends View {
    private String solution;
    private String challenge;
    private Task searchTask;
    private String currentFocus;

    @Override
    public void schedule(Task task, float delaySeconds, float intervalSeconds) {
        this.searchTask = task;
    }
    
    public Task getSearchTask() {
        return searchTask;
    }

    @Override
    public void setPuzzle(String letters) {
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
