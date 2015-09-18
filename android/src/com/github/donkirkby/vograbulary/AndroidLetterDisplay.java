package com.github.donkirkby.vograbulary;

import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class AndroidLetterDisplay extends LetterDisplay {
    private TextView textView;
    private boolean isInitialized;
    private int offsetX; // add to x to get leftMargin
    private int offsetY; // add to y to get topMargin
    
    public AndroidLetterDisplay(TextView textView) {
        this.textView = textView;
        LayoutParams layoutParams = getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.insertImage);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.spacer);
    }

    private LayoutParams getLayoutParams() {
        return (RelativeLayout.LayoutParams)textView.getLayoutParams();
    }

    private void checkOffset() {
        if ( ! isInitialized) {
            int[] location = new int[2];
            textView.getLocationOnScreen(location);
            offsetX = (int)textView.getTranslationX() - location[0];
            isInitialized = true;
        }
    }
    
    @Override
    public int getTop() {
        checkOffset();
        return (int)textView.getTranslationY() - offsetY;
    }
    
    @Override
    public void setTop(int top) {
        checkOffset();
        textView.setTranslationY(top + offsetY);
    }
    
    @Override
    public int getLeft() {
        checkOffset();
        return (int)textView.getTranslationX() - offsetX;
    }

    @Override
    public void setLeft(int left) {
        checkOffset();
        textView.setTranslationX(left + offsetX);
    }

    @Override
    public void animateTo(int left, int top) {
        checkOffset();
        textView.animate().x(left + offsetX).y(top + offsetY);
    }

    @Override
    public int getHeight() {
        return textView.getHeight();
    }
    @Override
    public int getWidth() {
        return textView.getWidth();
    }

    @Override
    public float getTextSize() {
        return textView.getTextSize();
    }
    @Override
    public void setTextSize(float size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    @Override
    public String getLetter() {
        return textView.getText().toString();
    }
    
    public TextView getTextView() {
        return textView;
    }
}
