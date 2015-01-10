package com.github.donkirkby.vograbulary.ultraghost;


public class DummyView implements UltraghostScreen {
    private Focus currentFocus = Focus.Unknown;
    private int refreshCount;
    private Match match;
    
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
    
    public enum Focus {Unknown, Thinking, Solution, Response, Result}

    @Override
    public void clear() {
    }

    public Puzzle getPuzzle() {
        return match.getPuzzle();
    }

    @Override
    public void setMatch(Match match) {
        this.match = match;
    }
    
    @Override
    public Match getMatch() {
        return match;
    }
}
