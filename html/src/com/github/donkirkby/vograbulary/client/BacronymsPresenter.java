package com.github.donkirkby.vograbulary.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.donkirkby.vograbulary.WordDisplay;
import com.github.donkirkby.vograbulary.WordDisplay.WordDisplayListener;
import com.github.donkirkby.vograbulary.bacronyms.BacronymsScreen;
import com.github.donkirkby.vograbulary.bacronyms.Controller;
import com.github.donkirkby.vograbulary.bacronyms.Puzzle;
import com.github.donkirkby.vograbulary.ultraghost.WordList;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class BacronymsPresenter
extends VograbularyPresenter implements BacronymsScreen {
    public static final String HISTORY_TOKEN = "bacronyms";

    private class WordAnimation extends Animation {
        private WordDisplay word;
        private double startRotation;
        private double targetRotation;
        
        public WordAnimation(WordDisplay word) {
            this.word = word;
        }
        
        public WordDisplay getWord() {
            return word;
        }
        
        @Override
        protected void onComplete() {
            controller.solve();
        }
        
        @Override
        protected void onUpdate(double progress) {
            word.setRotation(
                    startRotation + (targetRotation - startRotation) * progress);
        }
        
        public void rotateWord(double targetRotation) {
            startRotation = word.getRotation();
            this.targetRotation = targetRotation;
            word.setTop(word.getTop());
            run(1000);
        }
    }

    private Controller controller;
    private Puzzle puzzle;
    private State state;
    private List<WordDisplay> wordDisplays;
    private List<WordAnimation> animations;
    private GwtLetterDisplayFactory displayFactory;
    private GwtScheduler scheduler = new GwtScheduler();
    private WordDisplayListener wordListener = new WordDisplayListener() {
        @Override
        public void onClick(WordDisplay wordDisplay) {
            for (int i = 0; i < animations.size(); i++) {
                WordAnimation wordAnimation = animations.get(i);
                if (wordAnimation.getWord() == wordDisplay &&
                        Math.abs(wordDisplay.getRotation()) < 0.0001) {
                    wordAnimation.rotateWord(Math.PI);
                    puzzle.setSelectedIndex(i);
                }
                else {
                    wordAnimation.rotateWord(0);
                }
            }
        }
    };

    interface BacronymsCompositeUiBinder extends
    UiBinder<Widget, BacronymsPresenter> {
    }
    
    @UiField
    AbsolutePanel wordPanel;

    @UiField
    ParagraphElement stateText;
    
    @UiField
    Button nextButton;
    
    private static BacronymsCompositeUiBinder uiBinder =
            GWT.create(BacronymsCompositeUiBinder.class);
    
    public BacronymsPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
        
        displayFactory = new GwtLetterDisplayFactory(wordPanel);
        wordDisplays = new ArrayList<>();
        animations = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            WordDisplay wordDisplay = new WordDisplay(displayFactory);
            wordDisplay.addListener(wordListener);
            wordDisplays.add(wordDisplay);
            
            animations.add(new WordAnimation(wordDisplay));
        }

        String puzzleLines = Assets.INSTANCE.bacronyms().getText();
        String wordListText = Assets.INSTANCE.wordList().getText();
        WordList wordList = new WordList();
        wordList.read(Arrays.asList(wordListText.split("\\n")));
        controller = new Controller();
        controller.setScreen(this);
        controller.setWordList(wordList);
        controller.loadPuzzles(Arrays.asList(puzzleLines.split("\\n")));
        controller.next();
    }
    
    @Override
    public Puzzle getPuzzle() {
        return puzzle;
    }
    @Override
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        displayWords();
    }
    
    @Override
    public State getState() {
        return state;
    }
    @Override
    public void setState(State state) {
        this.state = state;
        stateText.setInnerText(state.name());
        nextButton.setVisible(state == State.SOLVED);
    }
    
    private void displayWords() {
        for (int i = 0; i < 3; i++) {
            wordDisplays.get(i).setWord(puzzle.getWord(i));
        }
        scheduler.scheduleDeferred(new Runnable() {
            @Override
            public void run() {
                int left = 0;
                for (WordDisplay word: wordDisplays) {
                    word.setLeft(left);
                    left += word.getWidth() + 20;
                }
            }
        });
    }
    
    @UiHandler("nextButton")
    void next(ClickEvent e) {
        controller.next();
    }
}
