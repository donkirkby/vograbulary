package com.github.donkirkby.vograbulary.russian;

/**
 * Control the display of one of the target words. This abstract class must
 * have a concrete subclass for each platform that sets the position and
 * content of the two text fields.
 * @author don
 *
 */
public abstract class TargetDisplay {
    /** get the text of the target text field */
    public abstract String getText();
    
    /** set the text of the target text field */
    public abstract void setText(String text);
    
    /** get the x position of the target text field */
    public abstract int getX();
    
    /** set the x position of the target text field */
    public abstract void setX(int x);
    
    /** get the x position of the drag button for the target */
    public abstract int getDragX();
    
    /** set the x position of the drag button for the target */
    public abstract void setDragX(int x);
    
    /** get the width of the target text field */
    public abstract int getWidth();
    
    /** is the drag button for the target visible? */
    public abstract boolean isDragVisible();
    
    /** set the visibility of the drag button for the target */
    public abstract void setDragVisible(boolean isDragVisible);
    
    private int dragStartX;
    private TargetDisplay other;
    private int visibleBoundary;
    private int sign;
    private int letterWidth;
    private String originalText;
    private int edgeStartX; // original position of the closer edge

    public void setOther(TargetDisplay other) {
        this.other = other;
        other.other = this;
    }
    
    public TargetDisplay getOther() {
        return other;
    }
    
    public void dragStart(int x) {
        dragStartX = x;
        if (sign == 0) {
            // first drag
            other.visibleBoundary = visibleBoundary = 
                    (getX() + other.getX())/2;
            sign = (int)Math.signum(other.getX() - getX());
            other.sign = -sign;
            originalText = getText();
            other.originalText = other.getText();
            letterWidth = other.letterWidth = 
                    getWidth()/originalText.length();
            edgeStartX = getCloserEdge();
            other.edgeStartX = other.getCloserEdge();
        }
    }
    
    public void drag(int x) {
        int offset = x - dragStartX;
        translate(offset);
        
        other.setDragVisible(sign*getX() <= sign*visibleBoundary);
        int overlap = getOverlap();
        if (overlap > 0) {
            other.translate(
                    (other.edgeStartX + sign*overlap) - other.getCloserEdge());
            adjustText(overlap);
        }
    }

    public int getOverlap() {
        return sign*getCloserEdge() - sign*other.edgeStartX;
    }

    private void translate(int offset) {
        setX(getX() + offset);
        setDragX(getDragX() + offset);
    }

    private void adjustText(int overlap) {
        String otherText = other.originalText;
        int otherLength = otherText.length();
        int letterOverlap = 
                Math.min(otherLength-1, (int) (overlap/letterWidth));
        if (sign > 0) {
            int lettersToAdd =
                    originalText.length() + letterOverlap - getText().length();
            if (lettersToAdd != 0) {
                setText(otherText.substring(0, letterOverlap) + originalText);
                other.setText(otherText.substring(letterOverlap));
                setX(getX() - lettersToAdd*letterWidth);
            }
        }
        else {
            setText(
                    originalText + 
                    otherText.substring(otherLength-letterOverlap));
            other.setText(otherText.substring(0, otherLength-letterOverlap));
        }
    }
    
    private int getCloserEdge() {
        return getX() + (sign > 0 ? getWidth() : 0);
    }
}
