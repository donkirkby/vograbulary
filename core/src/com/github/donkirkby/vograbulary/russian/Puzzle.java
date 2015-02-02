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
        this(clue, previous.getTotalScore());
    }
    
    private Puzzle(String clue, BigDecimal totalScore) {
        this.clue = clue;
        this.totalScore = totalScore;
        clearTargets();
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
     * Clear the target word and character so no solution is selected.
     */
    public void clearTargets() {
        targetWord = targetCharacter = -1;
    }

    /**
     * The index of the word that will have the other word inserted into it.
     * For example, if the words are "unable"(0) and "comfortable"(1), then the
     * target word is 0, because "comfortable" is inserted into to "unable" to
     * make "uncomfortable".
     * @param targetWord
     */
    public void setTargetWord(int targetWord) {
        if (targetWord < 0 || 1 < targetWord) {
            throw new ArrayIndexOutOfBoundsException(
                    "Target word index " + targetWord + " is invalid.");
        }
        this.targetWord = targetWord;
    }
    public int getTargetWord() {
        return targetWord;
    }
    
    /**
     * The index of the character in the target word that will have the other
     * word inserted before it. For example, if the words are "unable" and 
     * "comfortable", then the target character is 2, because "comfortable" is
     * inserted before the "a" to make "uncomfortable".
     * @param targetCharacter
     */
    public void setTargetCharacter(int targetCharacter) {
        if (targetWord < 0) {
            throw new IllegalStateException(
                    "Target character set before target word.");
        }
        if (! isValidTargetCharacter(targetCharacter)) {
            throw new ArrayIndexOutOfBoundsException(
                    "Target character index " + targetCharacter + " is invalid.");
        }
        this.targetCharacter = targetCharacter;
    }

    public boolean isValidTargetCharacter(int targetCharacter) {
        return 1 <= targetCharacter &&
                targetCharacter <= getTarget(targetWord).length() - 1;
    }
    
    public int getTargetCharacter() {
        return targetCharacter;
    }
    public String getClue() {
        return clue;
    }
    public String getTarget(int wordIndex) {
        return targets[wordIndex];
    }
    
    public boolean isTargetSet() {
        return targetCharacter >= 0;
    }
    
    public boolean isSolved() {
        return isSolved;
    }
    public void setSolved(boolean isSolved) {
        this.isSolved = isSolved;
        if (isSolved) {
            this.totalScore = totalScore.add(getScore());
        }
    }

    public String getCombination() {
        if ( ! isTargetSet()) {
            throw new IllegalStateException(
                    "Target word and character are not set.");
        }
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
        if ( ! isSolved) {
            delay += seconds;
        }
        return getScoreDisplay();
    }

    public String getScoreDisplay() {
        return getScoreDisplay(getScore());
    }

    public String getTotalScoreDisplay() {
        return getScoreDisplay(getTotalScore());
    }

    private String getScoreDisplay(BigDecimal score) {
        // TODO: Put the formatting back in a way that works under GWT.
        // int formatPrecision = Math.max(0, score.scale());
        return score.toString();//String.format("%." + formatPrecision + "f", score);
    }
    
    public BigDecimal getTotalScore() {
        return totalScore;
    }
}
