package com.github.donkirkby.vograbulary.client;

import java.util.Arrays;

import com.github.donkirkby.vograbulary.russian.Controller;
import com.github.donkirkby.vograbulary.russian.Puzzle;
import com.github.donkirkby.vograbulary.russian.PuzzleDisplay;
import com.github.donkirkby.vograbulary.russian.RussianDollsScreen;
import com.github.donkirkby.vograbulary.ultraghost.WordList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class RussianDollsPresenter
extends VograbularyPresenter implements RussianDollsScreen {

    public static final String HISTORY_TOKEN = "russian";
    private static final int INSERT_BUTTON_TOP_MARGIN = 4;
    private PuzzleDisplay puzzleDisplay = new PuzzleDisplay();
    private Controller controller = new Controller();
    private Dragger dragger;
    private GwtScheduler scheduler = new GwtScheduler();

    interface RussianDollsCompositeUiBinder extends
    UiBinder<Widget, RussianDollsPresenter> {
    }
    
    @UiField
    AbsolutePanel insertPanel;
    
    @UiField
    Image insertButton;
    
    @UiField
    ParagraphElement clue;
    
    @UiField
    SpanElement targetWord1;
    
    @UiField
    SpanElement targetWord2;
    
    @UiField
    Image dragButton1;
    
    @UiField
    Image dragButton2;
    
    @UiField
    Button nextButton;

    @UiField
    ParagraphElement scoreDisplay;
    
    @UiField
    ParagraphElement totalDisplay;
    
    private static RussianDollsCompositeUiBinder uiBinder = GWT
            .create(RussianDollsCompositeUiBinder.class);

    public RussianDollsPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
        
        dragger = new Dragger(insertButton);
        insertButton.addMouseDownHandler(dragger);
        insertButton.addMouseMoveHandler(dragger);
        insertButton.addMouseUpHandler(dragger);
        insertButton.addTouchStartHandler(dragger);
        insertButton.addTouchMoveHandler(dragger);
        insertButton.addTouchEndHandler(dragger);

        String puzzleText = Assets.INSTANCE.russianDolls().getText();
        String wordListText = Assets.INSTANCE.wordList().getText();
        WordList wordList = new WordList();
        wordList.read(Arrays.asList(wordListText.split("\\n")));
        controller.setScreen(this);
        controller.setWordList(wordList);
        controller.loadPuzzles(Arrays.asList(puzzleText.split("\\n")));

        dragButton1.setVisible(false);
        dragButton2.setVisible(false);
        final int periodMilliseconds = 100;
        final float periodSeconds = periodMilliseconds / 1000.0f;
        scheduler.scheduleRepeating(
                new Runnable() {
                    @Override
                    public void run() {
                        String score = controller.adjustScore(periodSeconds);
                        Puzzle puzzle = puzzleDisplay.getPuzzle();
                        String total = puzzle.getTotalScoreDisplay();
                        scoreDisplay.setInnerText("Score: " + score);
                        totalDisplay.setInnerText("Total: " + total);
                        boolean shouldHide =
                                ! dragButton1.isVisible() &&
                                puzzle.getScore().intValue() < 50;
                        if (shouldHide) {
                            dragButton1.setVisible(true);
                            dragButton2.setVisible(true);
                        }
                    }
                },
                periodMilliseconds);
    }
    
    @UiHandler("nextButton")
    void next(ClickEvent e) {
        Puzzle puzzle = getPuzzle();
        if (puzzle.isSolved()) {
            controller.next();
        }
        else {
            controller.solve();
            if (puzzle.isSolved()) {
                nextButton.setText("Next");
                targetWord1.setInnerText(puzzle.getCombination());
                targetWord2.setInnerText("");
            }
        }
    }

    private class Dragger
    implements MouseDownHandler, MouseMoveHandler, MouseUpHandler,
    TouchStartHandler, TouchMoveHandler, TouchEndHandler {
        private UIObject target;
        private int startX;
        private boolean isDragging;
        
        public Dragger(UIObject target) {
            this.target = target;
        }
        
        public void centre() {
            int centreX = (targetWord2.getAbsoluteLeft() + 
                    targetWord2.getOffsetWidth() -
                    insertButton.getOffsetWidth()) / 2;
            insertPanel.setWidgetPosition(
                    insertButton,
                    centreX, 
                    INSERT_BUTTON_TOP_MARGIN);
        }
        
        @Override
        public void onMouseDown(MouseDownEvent event) {
            event.preventDefault();
            onStart(event.getClientX());
        }
        
        @Override
        public void onTouchStart(TouchStartEvent event) {
            event.preventDefault();
            onStart(event.getTouches().get(0).getClientX());
        }

        private void onStart(int x) {
            Event.setCapture(target.getElement());
            startX = x - insertPanel.getWidgetLeft(insertButton);
            puzzleDisplay.setTargetPositions(
                    targetWord1.getAbsoluteLeft(),
                    targetWord1.getOffsetWidth(),
                    targetWord2.getAbsoluteLeft(),
                    targetWord2.getOffsetWidth());
            isDragging = true;
        }
        
        @Override
        public void onMouseMove(MouseMoveEvent event) {
            onMove(event.getClientX());
        }
        
        @Override
        public void onTouchMove(TouchMoveEvent event) {
            onMove(event.getTouches().get(0).getClientX());
        }

        private void onMove(int x) {
            if (isDragging) {
                insertPanel.setWidgetPosition(
                        insertButton,
                        x - startX, 
                        INSERT_BUTTON_TOP_MARGIN);
                int insertX =
                        insertButton.getAbsoluteLeft() +
                        insertButton.getOffsetWidth() * 24/64;
                puzzleDisplay.calculateInsertion(insertX);
            }
        }

        @Override
        public void onMouseUp(MouseUpEvent event) {
            onStop();
        }
        
        @Override
        public void onTouchEnd(TouchEndEvent event) {
            onStop();
        }

        private void onStop() {
            Event.releaseCapture(target.getElement());
            isDragging = false;
        }
    }

    @Override
    public Puzzle getPuzzle() {
        return puzzleDisplay.getPuzzle();
    }

    @Override
    public void setPuzzle(Puzzle puzzle) {
        clue.setInnerText(puzzle.getClue());
        targetWord1.setInnerText(puzzle.getTarget(0));
        targetWord2.setInnerText(puzzle.getTarget(1));
        nextButton.setText("Solve");
        dragger.centre();
        puzzleDisplay.setPuzzle(puzzle);
    }
}
