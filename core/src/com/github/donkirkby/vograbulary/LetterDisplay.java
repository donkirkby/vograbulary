package com.github.donkirkby.vograbulary;

import java.util.List;

public abstract class LetterDisplay {
    private int startLeft;
    private boolean isMovingLeft;
    
    /** Get the x coordinate of the left side of the letter. */
    public abstract int getLeft();
    /** Set the letter's left side position on the display. */
    public abstract void setLeft(int left);
    
    /** Get the letter's width on the display. */
    public abstract int getWidth();
    
    /** Get the letter that will display. */
    public abstract String getLetter();
    
    /** Get the x coordinate one past the right side of the letter. */
    public int getRight() {
        return getLeft() + getWidth();
    }
    /** Set the x coordinate one past the right side of the letter. */
    public void setRight(int right) {
        setLeft(right - getWidth());
    }
    
    public void recordStartPosition() {
        startLeft = getLeft();
    }
    public int getStartLeft() {
        return startLeft;
    }
    public int getStartRight() {
        return startLeft + getWidth();
    }
    
    /** Set this to true if letters that overlap with the other word should
     * move to the left side of that word.
     */
    public void setMovingLeft(boolean isMovingLeft) {
        this.isMovingLeft = isMovingLeft;
    }
    /** Gets a flag indicating if letters that overlap with the other word
     * should move to the left side of that word.
     */
    public boolean isMovingLeft() {
        return isMovingLeft;
    }
    
    /** Assign this display to one of two lists, depending on whether it should
     * move before or after the other word.
     * @param left the left edge of the other word
     * @param right one pixel past the right edge of the other word
     * @param before the list of letters to display before the other word
     * @param after the list of letters to display after the other word
     */
    public void split(
            int left,
            int right,
            List<LetterDisplay> before,
            List<LetterDisplay> after) {
        if (getStartLeft() < left) {
            before.add(this);
        }
        else if (right < getStartRight()) {
            after.add(this);
        }
        else if (isMovingLeft) {
            before.add(this);
        }
        else {
            after.add(this);
        }
    }
}
