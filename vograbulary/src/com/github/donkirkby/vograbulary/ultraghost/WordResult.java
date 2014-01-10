package com.github.donkirkby.vograbulary.ultraghost;

public enum WordResult {
    //stopJesting
    NOT_A_WORD, 
    VALID, 
    NOT_A_MATCH, 
    IMPROVED(1), 
    SHORTER(1),
    EARLIER(2),
    LONGER(3), 
    LATER(3), 
    NOT_IMPROVED(3), 
    SKIPPED(1), 
    CHALLENGE_NOT_A_WORD(3, "not a word"), 
    CHALLENGED_SKIP_NOT_A_WORD(1, "not a word");
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
