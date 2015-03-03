package com.github.donkirkby.vograbulary;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.github.donkirkby.vograbulary.russian.TargetDisplay;

public class AndroidTargetDisplay
extends TargetDisplay implements View.OnTouchListener{
    private ImageView dragButton;
    private ImageView insertButton;
    private boolean isInitialized;
    private int offsetX; // add to x to get leftMargin
    
    public AndroidTargetDisplay(
            ImageView dragButton,
            ImageView insertButton,
            ViewGroup layout) {
        super(new AndroidLetterDisplayFactory(layout));
        this.dragButton = dragButton;
        this.insertButton = insertButton;
        dragButton.setOnTouchListener(this);
    }
    
    private LayoutParams getDragLayoutParams() {
        return (RelativeLayout.LayoutParams)dragButton.getLayoutParams();
    }

    private void checkOffset() {
        if ( ! isInitialized) {
            int[] location = new int[2];
            dragButton.getLocationOnScreen(location);
            offsetX = getDragLayoutParams().leftMargin - location[0];
            isInitialized = true;
        }
    }
    
    @Override
    public int getDragX() {
        checkOffset();
        return getDragLayoutParams().leftMargin - offsetX;
    }
    
    @Override
    public void setDragX(int x) {
        checkOffset();
        LayoutParams dragLayoutParams = getDragLayoutParams();
        dragLayoutParams.leftMargin = x + offsetX;
        dragButton.setLayoutParams(dragLayoutParams);
    }
    
    @Override
    public boolean isDragVisible() {
        return dragButton.getVisibility() == View.VISIBLE;
    }
    
    @Override
    public void setDragVisible(boolean isDragVisible) {
        dragButton.setVisibility(isDragVisible ? View.VISIBLE : View.INVISIBLE);
    }
    
    @Override
    public boolean isInsertVisible() {
        return insertButton.getVisibility() == View.VISIBLE;
    }
    
    @Override
    public void setInsertVisible(boolean isInsertVisible) {
        insertButton.setVisibility(isInsertVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int eventX = (int) event.getX();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            dragStart(eventX);
            break;
        case MotionEvent.ACTION_MOVE:
            drag(eventX);
            break;
        case MotionEvent.ACTION_UP:
            dragStop();
            view.performClick();
            break;
        }
        return true;
    }
}
