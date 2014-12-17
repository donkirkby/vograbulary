package com.github.donkirkby.vograbulary.russian;

//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class TargetDisplay { //extends DragListener {
//    private Label target;
//    private TextButton drag;
//    private float dragStartX;
//    private TargetDisplay other;
//    private float visibleBoundary;
//    private float sign;
//    private float letterWidth;
//    private String originalText;
//    private float edgeStartX; // original position of the closer edge
//    
//    public TargetDisplay(Label target, TextButton drag) {
//        this.target = target;
//        this.drag = drag;
//        setTapSquareSize(2);
//    }
//
//    public void setOther(TargetDisplay other) {
//        this.other = other;
//        other.other = this;
//    }
//    
//    @Override
//    public void dragStart(InputEvent event, float x, float y, int pointer) {
//        dragStartX = x;
//        if (sign == 0) {
//            // first drag
//            other.visibleBoundary = visibleBoundary = 
//                    (target.getX() + other.target.getX())/2;
//            sign = Math.signum(other.target.getX() - target.getX());
//            other.sign = -sign;
//            originalText = target.getText().toString();
//            other.originalText = other.target.getText().toString();
//            letterWidth = other.letterWidth = 
//                    target.getWidth()/originalText.length();
//            edgeStartX = getCloserEdge();
//            other.edgeStartX = other.getCloserEdge();
//        }
//    }
//    
//    @Override
//    public void drag(InputEvent event, float x, float y, int pointer) {
//        float offset = x - dragStartX;
//        target.translate(offset, 0);
//        drag.translate(offset, 0);
//        
//        other.drag.setVisible(sign*target.getX() <= sign*visibleBoundary);
//        float overlap = sign*getCloserEdge() - sign*other.edgeStartX;
//        if (overlap > 0) {
//            other.target.translate(
//                    (other.edgeStartX + sign*overlap) - other.getCloserEdge(), 
//                    0);
//            adjustText(overlap);
//        }
//    }
//
//    private void adjustText(float overlap) {
//        String otherText = other.originalText;
//        int otherLength = otherText.length();
//        int letterOverlap = 
//                Math.min(otherLength-1, (int) (overlap/letterWidth));
//        if (sign > 0) {
//            target.setText(otherText.substring(0, letterOverlap) + originalText);
//            other.target.setText(otherText.substring(letterOverlap));
//        }
//        else {
//            target.setText(
//                    originalText + 
//                    otherText.substring(otherLength-letterOverlap));
//            other.target.setText(otherText.substring(0, otherLength-letterOverlap));
//        }
//    }
//    
//    private float getCloserEdge() {
//        return sign > 0 ? target.getRight() : target.getX();
//    }
}
