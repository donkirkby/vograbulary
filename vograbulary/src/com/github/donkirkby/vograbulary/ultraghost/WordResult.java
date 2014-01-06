package com.github.donkirkby.vograbulary.ultraghost;

public enum WordResult {
    NOT_A_WORD, 
    VALID, 
    NOT_A_MATCH, 
    NOT_IMPROVED, 
    IMPROVED, 
    LONGER, 
    LATER, 
    SKIPPED, 
    CHALLENGE_NOT_A_WORD("not a word"), 
    CHALLENGED_SKIP_NOT_A_WORD("not a word");
    
    private String name;
    
    private WordResult() {
        this.name = name().toLowerCase().replace('_', ' ');
    }
    
    private WordResult(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
