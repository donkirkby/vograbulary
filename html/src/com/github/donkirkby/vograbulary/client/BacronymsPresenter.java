package com.github.donkirkby.vograbulary.client;

import java.util.Arrays;
import java.util.List;

import com.github.donkirkby.vograbulary.bacronyms.BacronymsScreen;
import com.github.donkirkby.vograbulary.bacronyms.Controller;
import com.github.donkirkby.vograbulary.bacronyms.Puzzle;
import com.github.donkirkby.vograbulary.ultraghost.WordList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class BacronymsPresenter
extends VograbularyPresenter implements BacronymsScreen {
    public static final String HISTORY_TOKEN = "bacronyms";

    private Controller controller;
    private Puzzle puzzle;
    private State state;
    private List<Button> wordButtons;

    interface BacronymsCompositeUiBinder extends
    UiBinder<Widget, BacronymsPresenter> {
    }

    @UiField
    Button word1;

    @UiField
    Button word2;
    
    @UiField
    Button word3;
    
    @UiField
    ParagraphElement stateText;
    
    @UiField
    Button nextButton;
    
    private static BacronymsCompositeUiBinder uiBinder =
            GWT.create(BacronymsCompositeUiBinder.class);
    
    public BacronymsPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
        
        wordButtons = Arrays.asList(word1, word2, word3);

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
    }

    private void selectWord(int selectedIndex) {
        puzzle.setSelectedIndex(selectedIndex);
        controller.solve();
        displayWords();
    }

    private void displayWords() {
        for (int i = 0; i < wordButtons.size(); i++) {
            wordButtons.get(i).setText(puzzle.getWord(i));
        }
    }
    
    @UiHandler("word1")
    void clickWord1(ClickEvent e) {
        selectWord(0);
    }
    
    @UiHandler("word2")
    void clickWord2(ClickEvent e) {
        selectWord(1);
    }
    
    @UiHandler("word3")
    void clickWord3(ClickEvent e) {
        selectWord(2);
    }
    
    @UiHandler("nextButton")
    void next(ClickEvent e) {
        controller.next();
    }
}
