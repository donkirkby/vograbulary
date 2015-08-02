package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.donkirkby.vograbulary.LetterDisplay.LetterDisplayListener;

public class WordDisplay {
    public interface WordDisplayListener {
        void onClick(WordDisplay wordDisplay);
    }

    private LetterDisplayFactory factory;
    private List<LetterDisplay> letters = new ArrayList<>();
    private List<WordDisplayListener> listeners = new ArrayList<>();
    private Integer width;
    private int centreX;
    private int centreY;
    private double rotation;
    
    private LetterDisplayListener letterDisplayListener = new LetterDisplayListener() {
        @Override
        public void onClick() {
            for (WordDisplayListener listener : listeners) {
                listener.onClick(WordDisplay.this);
            }
        }
    };

    public WordDisplay(LetterDisplayFactory factory) {
        this.factory = factory;
    }
    
    public void addListener(WordDisplayListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public String toString() {
        return "WordDisplay(\"" + getWord() + "\")";
    }

    public void setWord(String word) {
        for (LetterDisplay letter : letters) {
            factory.destroy(letter);
        }
        letters.clear();
        rotation = 0;
        for (int i = 0; i < word.length(); i++) {
            LetterDisplay letter = factory.create(word.substring(i, i+1));
            letter.addClickListener(letterDisplayListener);
            letters.add(letter);
        }
        width = null;
    }

    public String getWord() {
        StringBuilder builder = new StringBuilder();
        for (LetterDisplay letterDisplay : letters) {
            builder.append(letterDisplay.getLetter());
        }
        return builder.toString();
    }
    
    public List<LetterDisplay> getLetters() {
        return Collections.unmodifiableList(letters);
    }

    public int getWidth() {
        if (width == null) {
            width = 0;
            for (LetterDisplay letter : letters) {
                width += letter.getWidth();
            }
        }
        return width;
    }

    public void setLeft(int left) {
        int nextLeft = left;
        for (LetterDisplay letter : letters) {
            letter.setLeft(nextLeft);
            nextLeft += letter.getWidth();
        }
        centreX = left + getWidth()/2;
    }

    public void setTop(int top) {
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.setTop(top);
        }
        centreY = letters.get(0).getCentreY();
    }

    public int getTop() {
        return letters.get(0).getTop();
    }
    
    public float getTextSize() {
        return letters.get(0).getTextSize();
    }
    
    public void setTextSize(float textSize) {
        for (LetterDisplay letterDisplay : letters) {
            letterDisplay.setTextSize(textSize);
        }
        width = null; // force recalculate
    }

    /** Set the rotation of the word in radians. */
    public void setRotation(double rotation) {
        this.rotation = rotation;
        int width = getWidth();
        double dx = Math.cos(rotation);
        double dy = Math.sin(rotation);
        double x = centreX - width * dx / 2;
        double y = centreY - width * dy / 2;
        for (LetterDisplay letter : letters) {
            int letterWidth = letter.getWidth();
            double dxLetter = dx * letterWidth;
            double dyLetter = dy * letterWidth;
            letter.setCentreX((int) Math.round(x + dxLetter/2));
            letter.setCentreY((int) Math.round(y + dyLetter/2));
            x += dxLetter;
            y += dyLetter;
        }
    }
    
    public double getRotation() {
        return rotation;
    }
}
