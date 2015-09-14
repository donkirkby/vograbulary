package com.github.donkirkby.vograbulary.anagrams;

import java.io.Serializable;

public class AnagramsPlayer implements Serializable {
    private static final long serialVersionUID = 2453191708636928442L;
    
    private int mScore;

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }
}
