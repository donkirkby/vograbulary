package com.github.donkirkby.vograbulary.russian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Control the display of one of the target words. This abstract class must
 * have a concrete subclass for each platform that sets the position and
 * content of the two text fields.
 * @author don
 *
 */
public abstract class TargetDisplay {
    /** get the x position of the drag button for the target */
    public abstract int getDragX();
    
    /** set the x position of the drag button for the target */
    public abstract void setDragX(int x);
    
    /** is the drag button for the target visible? */
    public abstract boolean isDragVisible();
    
    /** set the visibility of the drag button for the target */
    public abstract void setDragVisible(boolean isDragVisible);
    
    private LetterDisplayFactory factory;
    private List<LetterDisplay> letters = new ArrayList<>();
    private int x;
    private int dragStartX;
    private TargetDisplay other;
    private int visibleBoundary;
    private int sign;

    public TargetDisplay(int x, LetterDisplayFactory factory) {
        this.x = x;
        this.factory = factory;
    }

    public void setOther(TargetDisplay other) {
        this.other = other;
        other.other = this;
    }
    
    public void setText(String text) {
        letters.clear();
        int nextX = x;
        for (int i = 0; i < text.length(); i++) {
            LetterDisplay letter = factory.create(text.substring(i, i+1), nextX);
            letters.add(letter);
            nextX += letter.getWidth();
        }
    }
    
    public String getText() {
        StringBuilder builder = new StringBuilder();
        for (LetterDisplay letterDisplay : letters) {
            builder.append(letterDisplay.getLetter());
        }
        return builder.toString();
    }
    
    public TargetDisplay getOther() {
        return other;
    }
    
    public void dragStart(int x) {
        dragStartX = x;
        if (sign == 0) {
            // first drag
            other.visibleBoundary = visibleBoundary = 
                    (getLettersLeft() + other.getLettersLeft())/2;
            sign = (int)Math.signum(other.getLettersLeft() - getLettersLeft());
            other.sign = -sign;
            boolean isOtherMovingLeft = sign > 0;
            other.recordLetterStartPositions(isOtherMovingLeft);
        }
    }
    
    private void recordLetterStartPositions(boolean isMovingLeft) {
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.recordStartPosition();
            letterDisplay.setMovingLeft(isMovingLeft);
        }
    }

    public void drag(int x) {
        int offset = x - dragStartX;
        translate(offset);
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.setLeft(letterDisplay.getLeft() + offset);
        }
        
        other.surround(getLettersLeft(), getLettersRight());
        
        other.setDragVisible(sign*getLettersLeft() <= sign*visibleBoundary);
    }

    public int getLettersRight() {
        return letters.get(letters.size()-1).getRight();
    }

    public int getLettersLeft() {
        return letters.get(0).getLeft();
    }

    private void surround(int left, int right) {
        List<LetterDisplay> before = new ArrayList<>();
        List<LetterDisplay> after = new ArrayList<>();
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.split(left, right, before, after);
        }
        
        if (before.size() > 0 && letters.get(letters.size()-1).getStartRight() > left) {
            int leftEdge = left;
            for (int i = before.size()-1; i >= 0; i--) {
                LetterDisplay letterDisplay = letters.get(i);
                letterDisplay.setRight(leftEdge);
                leftEdge = letterDisplay.getLeft();
            }
        }
        if (after.size() > 0 && letters.get(0).getStartLeft() < right) {
            int rightEdge = right;
            for (LetterDisplay letterDisplay : after) {
                letterDisplay.setLeft(rightEdge);
                rightEdge = letterDisplay.getRight();
            }
        }
    }

    private void translate(int offset) {
        setDragX(getDragX() + offset);
    }
    
    public List<LetterDisplay> getLetters() {
        return Collections.unmodifiableList(letters);
    }

    public void layout() {
        int nextX = x;
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.setLeft(nextX);
            nextX += letterDisplay.getWidth();
        }
    }
}
