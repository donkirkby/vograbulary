package com.github.donkirkby.vograbulary.bacronyms;

public class DummyScreen implements BacronymsScreen {
    private Puzzle puzzle;
    private State state;

    @Override
    public Puzzle getPuzzle() {
        return puzzle;
    }
    @Override
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }
    
    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }
}
