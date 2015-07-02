package com.github.donkirkby.vograbulary.ultraghost;

public enum WordResult {
    //stopJesting
    UNKNOWN,
    NOT_A_WORD, 
    TOO_SHORT,
    TOO_SOON,
    VALID, 
    NOT_A_MATCH, 
    SHORTER(1),
    EARLIER(2),
    LONGER(3), 
    LATER(3), 
    NOT_IMPROVED(3), 
    SKIPPED(1),
    WORD_FOUND(-1), // Failed skip
    IMPROVEMENT_NOT_A_WORD(3, "not a word"), 
    IMPROVEMENT_NOT_A_MATCH(3, "not a match"),
    IMPROVEMENT_TOO_SHORT(3, "too short"),
    IMPROVEMENT_TOO_SOON(3, "too soon"),
    IMPROVED_SKIP_NOT_A_WORD(1, "not a word"),
    IMPROVED_SKIP_NOT_A_MATCH(1, "not a match"),
    IMPROVED_SKIP_TOO_SHORT(1, "too short"), 
    IMPROVED_SKIP_TOO_SOON(1, "too soon");
    //resumeJesting
    
    private int score;
    private String name;
    
    private WordResult() {
        this(0);
    }
    
    private WordResult(int score) {
        this(score, null);
        this.name = name().toLowerCase().replace('_', ' ');
    }
    
    private WordResult(int score, String name) {
        this.score = score;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
}
