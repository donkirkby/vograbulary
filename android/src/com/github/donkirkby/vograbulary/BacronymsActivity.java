package com.github.donkirkby.vograbulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.WordDisplay.WordDisplayListener;
import com.github.donkirkby.vograbulary.bacronyms.BacronymsScreen;
import com.github.donkirkby.vograbulary.bacronyms.Controller;
import com.github.donkirkby.vograbulary.bacronyms.Puzzle;
import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class BacronymsActivity
extends VograbularyActivity implements BacronymsScreen {
    private Controller controller = new Controller();
    private Puzzle puzzle;
    private State state;
    private TextView stateText;
    private List<WordDisplay> wordDisplays;
    private List<ValueAnimator> animations;
    private AndroidLetterDisplayFactory displayFactory;
    private Button nextButton;
    private boolean isLaidOut;
    private WordDisplayListener wordListener = new WordDisplayListener() {
        @Override
        public void onClick(WordDisplay wordDisplay) {
            for (int i = 0; i < animations.size(); i++) {
                ValueAnimator wordAnimation = animations.get(i);
                wordAnimation.cancel();
                if (wordDisplays.get(i) == wordDisplay &&
                        Math.abs(wordDisplay.getRotation()) < 0.0001) {
                    wordAnimation.setFloatValues(0, (float)Math.PI);
                    puzzle.setSelectedIndex(i);
                }
                else {
                    wordAnimation.setFloatValues(
                            (float)wordDisplays.get(i).getRotation(),
                            0);
                }
                wordAnimation.start();
            }
        }
    };
    private ViewGroup bacronymsLayout;

    @Override
    public Puzzle getPuzzle() {
        return puzzle;
    }
    @Override
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        displayWords();
    }
    
    private void displayWords() {
        for (int i = 0; i < 3; i++) {
            wordDisplays.get(i).setWord(puzzle.getWord(i));
        }
        isLaidOut = false;
        bacronymsLayout.invalidate();
    }
    
    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
        stateText.setText(state.name());
        nextButton.setVisibility(
                state == State.SOLVED
                ? View.VISIBLE
                : View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacronyms);
        stateText = (TextView)findViewById(R.id.stateText);
        nextButton = (Button)findViewById(R.id.nextButton);

        List<String> puzzleLines;
        List<String> wordSource;
        try {
            puzzleLines = loadTextAsset("bacronyms.txt");
            wordSource = loadTextAsset("wordlist.txt");
        } catch (IOException e) {
            puzzleLines = Arrays.asList(
                    "Failed to open file. " + e.getMessage());
            wordSource = new ArrayList<String>();
        }
        WordList wordList = new WordList();
        wordList.read(wordSource);
        controller.setScreen(this);
        controller.setWordList(wordList);
        controller.loadPuzzles(puzzleLines);
        
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.next();
            }
        });
        bacronymsLayout = (ViewGroup)findViewById(R.id.bacronymsLayout);
        displayFactory = new AndroidLetterDisplayFactory(bacronymsLayout);
        wordDisplays = new ArrayList<>();
        animations = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final WordDisplay wordDisplay = new WordDisplay(displayFactory);
            wordDisplay.addListener(wordListener);
            wordDisplays.add(wordDisplay);
            ValueAnimator animator = new ValueAnimator().setDuration(1000);
            animations.add(animator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    wordDisplay.setRotation((float) animation.getAnimatedValue());
                }
            });
            
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }
                
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
                
                @Override
                public void onAnimationEnd(Animator animation) {
                    controller.solve();
                }
                
                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
        }
        bacronymsLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ( ! isLaidOut) {
                    int left = 0;
                    for (WordDisplay wordDisplay : wordDisplays) {
                        wordDisplay.setLeft(left);
                        wordDisplay.setTop(wordDisplay.getTop());
                        left += wordDisplay.getWidth() + wordDisplay.getTextSize();
                    }
                    int layoutWidth = bacronymsLayout.getWidth();
                    if (layoutWidth * .75 < left && left < layoutWidth) {
                        isLaidOut = true;
                    }
                    else {
                        float textSize = 0;
                        for (WordDisplay wordDisplay : wordDisplays) {
                            if (textSize == 0) {
                                float oldTextSize = wordDisplay.getTextSize();
                                float correction = layoutWidth * 0.9f / left;
                                textSize = oldTextSize * correction;
                                stateText.setTop((int) (textSize*3));
                            }
                            wordDisplay.setTextSize(textSize);
                            wordDisplay.setTop((int) textSize);
                        }
                        bacronymsLayout.requestLayout();
                    }
                }
            }
        });
        start(savedInstanceState);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("solvedCount", controller.getSolvedCount());
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        start(savedInstanceState);
    }
    
    private void start(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int solvedCount = savedInstanceState.getInt("solvedCount", 1);
            controller.setSolvedCount(solvedCount - 1);
        }
        controller.next();
    }
}
