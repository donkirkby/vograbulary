package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.utils.Timer.Task;

public class DummyView implements View {
    private String solution;
    private String currentFocus;
    private Task searchTask;

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

    public String getSolution() {
        return solution;
    }

    @Override
    public void focusSolution() {
        currentFocus = "solution";
    }
    
    @Override
    public void focusNextButton() {
        currentFocus = "next button";
    }
    
    public String getCurrentFocus() {
        return currentFocus;
    }
}
