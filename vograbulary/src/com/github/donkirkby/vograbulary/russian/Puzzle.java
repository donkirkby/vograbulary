package com.github.donkirkby.vograbulary.russian;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Puzzle {
    private String clue;
    private String[] targets;
    private int targetWord;
    private int targetCharacter;
    private boolean isSolved;
    private float delay = 0;
    private BigDecimal totalScore;
    
    public Puzzle(String clue) {
        this(clue, BigDecimal.ZERO);
    }
    
    public Puzzle(String clue, Puzzle previous) {
        this(clue, previous.getScore().add(previous.getTotalScore()));
    }
    
    private Puzzle(String clue, BigDecimal totalScore) {
        this.clue = clue;
        this.totalScore = totalScore;
        int targetPosition = 0;
        String[] words = clue.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.charAt(0) == '*') {
                // remove * and any other punctuation
                words[targetPosition++] = word.replaceAll("\\W*", "");
            }
        }
        targets = new String[] {
                words[0].toUpperCase(),
                words[1].toUpperCase()
        };
        if (words.length == 2) {
            this.clue = "";
        }
    }

    /**
     * The index of the word that will have the other word inserted into it.
     * For example, if the words are "unable"(0) and "comfortable"(1), then the
     * target word is 0, because "comfortable" is inserted into to "unable" to
     * make "uncomfortable".
     * @param targetWord
     */
    public void setTargetWord(int targetWord) {
        this.targetWord = targetWord;
    }
    
    /**
     * The index of the character in the target word that will have the other
     * word inserted before it. For example, if the words are "unable" and 
     * "comfortable", then the target character is 2, because "comfortable" is
     * inserted before the "a" to make "uncomfortable".
     * @param targetCharacter
     */
    public void setTargetCharacter(int targetCharacter) {
        this.targetCharacter = targetCharacter;
    }
    public String getClue() {
        return clue;
    }
    public String getTarget(int wordIndex) {
        return targets[wordIndex];
    }
    public boolean isSolved() {
        return isSolved;
    }
    public void setSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }

    public String getCombination() {
        return targets[targetWord].substring(0, targetCharacter) +
                targets[(targetWord+1)%2] +
                targets[targetWord].substring(targetCharacter);
    }
    
    public BigDecimal getScore() {
        float rawScore = 
                Math.max(0.000101f, 100 * (float)Math.exp(-delay*Math.log(2)/10));
        int precision = rawScore >= 100 ? 3 : 2;
        return new BigDecimal(
                rawScore, 
                new MathContext(precision, RoundingMode.FLOOR));
    }

    public String adjustScore(float seconds) {
        delay += seconds;
        return getScoreDisplay();
    }

    public String getScoreDisplay() {
        return getScoreDisplay(getScore());
    }

    public String getTotalScoreDisplay() {
        return getScoreDisplay(getTotalScore());
    }

    private String getScoreDisplay(BigDecimal score) {
        int formatPrecision = Math.max(0, score.scale());
        return String.format("%." + formatPrecision + "f", score);
    }
    
    public BigDecimal getTotalScore() {
        return totalScore;
    }
}
