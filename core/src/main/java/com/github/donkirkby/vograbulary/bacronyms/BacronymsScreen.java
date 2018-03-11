package com.github.donkirkby.vograbulary.bacronyms;

public interface BacronymsScreen {
    public enum State {
        START,
        NEW,
        SOLVED,
        WRONG
    }
    
    public Puzzle getPuzzle();
    public void setPuzzle(Puzzle puzzle);
    
    public State getState();
    public void setState(State state);
}
