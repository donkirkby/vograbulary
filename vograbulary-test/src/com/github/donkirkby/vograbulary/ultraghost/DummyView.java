package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.utils.Timer.Task;

public class DummyView extends View {
    private String puzzle = "";
    private String activeStudent = "";
    private String solution = "";
    private String challenge = "";
    private String result = "";
    private String scores = "";
    private Task searchTask;
    private Focus currentFocus = Focus.Unknown;
    private int refreshCount;

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
    
    public String getPuzzleLetters() {
        return puzzle;
    }

    @Override
    public void setActiveStudent(String name) {
        activeStudent = name;
    }
    
    public String getActiveStudent() {
        return activeStudent;
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
        currentFocus = Focus.Solution;
    }
    
    @Override
    public void focusResponse() {
        currentFocus = Focus.Response;
    }
    
    @Override
    public void focusNextButton() {
        currentFocus = Focus.Result;
    }
    
    @Override
    public void showThinking() {
        currentFocus = Focus.Thinking;
    }
    
    @Override
    public void refreshPuzzle() {
        refreshCount++;
    }
    
    public int getRefreshCount() {
        return refreshCount;
    }
    
    public Focus getCurrentFocus() {
        return currentFocus;
    }
    
    public enum Focus {Unknown, Thinking, Solution, Response, Result};
}
