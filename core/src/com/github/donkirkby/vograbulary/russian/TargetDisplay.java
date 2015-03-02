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
    
    /** is the insert button visible? */
    public abstract boolean isInsertVisible();
    
    /** set the visibility of the insert button. */
    public abstract void setInsertVisible(boolean isInsertVisible);
    
    private LetterDisplayFactory factory;
    private List<LetterDisplay> letters = new ArrayList<>();
    private Puzzle puzzle;
    private int dragStartX;
    private TargetDisplay other;
    private int visibleBoundary;
    private int sign;
    private int screenWidth;

    public TargetDisplay(LetterDisplayFactory factory) {
        this.factory = factory;
    }

    public void setOther(TargetDisplay other) {
        this.other = other;
        other.other = this;
    }
    
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        other.puzzle = puzzle;
        setText(puzzle.getTarget(0));
        other.setText(puzzle.getTarget(1));
        setDragVisible(false);
        other.setDragVisible(false);
        setInsertVisible(true);
        this.sign = other.sign = 0;
    }
    
    public void setText(String text) {
        for (LetterDisplay letterDisplay : letters) {
            factory.destroy(letterDisplay);
        }
        letters.clear();
        for (int i = 0; i < text.length(); i++) {
            LetterDisplay letter = factory.create(text.substring(i, i+1));
            letters.add(letter);
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
            setInsertVisible(false);
            sign = (int)Math.signum(other.getLettersLeft() - getLettersLeft());
            other.sign = -sign;
            boolean isOtherMovingLeft = sign > 0;
            other.recordLetterStartPositions(isOtherMovingLeft);
        }
        visibleBoundary =
                screenWidth/2
                - (1+sign)/2 * getLettersWidth()
                - sign * other.getLettersWidth();
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
    
    public void dragStop() {
        if (other.isDragVisible()) {
            this.sign = other.sign = 0;
        }
    }
    
    public int getLettersRight() {
        return letters.get(letters.size()-1).getRight();
    }

    public int getLettersLeft() {
        return letters.get(0).getLeft();
    }

    public int getLettersWidth() {
        return getLettersRight() - getLettersLeft();
    }

    private void surround(int left, int right) {
        List<LetterDisplay> before = new ArrayList<>();
        List<LetterDisplay> after = new ArrayList<>();
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.split(left, right, before, after);
        }
        
        if (before.size() == 0 || after.size() == 0) {
            puzzle.clearTargets();
        }
        else {
            puzzle.setTargetWord((1 - sign)/2);
            puzzle.setTargetCharacter(before.size());
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
        int nextX = screenWidth / 2;
        for (LetterDisplay letterDisplay : letters) {
            nextX += letterDisplay.getWidth();
        }
        other.setDragX(nextX);
        for (LetterDisplay letterDisplay : other.letters) {
            letterDisplay.setLeft(nextX);
            nextX += letterDisplay.getWidth();
        }
        nextX = screenWidth - nextX;
        setDragX(nextX);
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.setLeft(nextX);
            nextX += letterDisplay.getWidth();
        }
    }
    
    /** Get the width of the screen that the display should be centred on. */
    public int getScreenWidth() {
        return screenWidth;
    }
    /** Set the width of the screen that the display should be centred on. */
    public void setScreenWidth(int screenWidth) {
        this.screenWidth = other.screenWidth = screenWidth;
    }
}
