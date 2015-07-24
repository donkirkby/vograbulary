package com.github.donkirkby.vograbulary;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
        textView.setTypeface(Typeface.MONOSPACE);
        layout.addView(textView);
        final AndroidLetterDisplay display = new AndroidLetterDisplay(textView);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.click();
            }
        });
        return display;
    }

    @Override
    public void destroy(LetterDisplay letter) {
        layout.removeView(((AndroidLetterDisplay)letter).getTextView());
    }

}
