package com.github.donkirkby.vograbulary;

import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.russian.LetterDisplay;
import com.github.donkirkby.vograbulary.russian.LetterDisplayFactory;

public class AndroidLetterDisplayFactory extends LetterDisplayFactory {
    private ViewGroup layout;
    
    public AndroidLetterDisplayFactory(ViewGroup layout) {
        this.layout = layout;
    }

    @Override
    public LetterDisplay create(String letter) {
        TextView textView = new TextView(layout.getContext());
        textView.setText(letter);
        textView.setTextAppearance(layout.getContext(), android.R.attr.textAppearanceLarge);
        textView.setTypeface(Typeface.MONOSPACE);
        layout.addView(textView);
        return new AndroidLetterDisplay(textView);
    }

    @Override
    public void destroy(LetterDisplay letter) {
        layout.removeView(((AndroidLetterDisplay)letter).getTextView());
    }

}