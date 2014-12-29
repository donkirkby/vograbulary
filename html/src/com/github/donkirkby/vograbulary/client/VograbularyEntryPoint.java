package com.github.donkirkby.vograbulary.client;

import com.github.donkirkby.vograbulary.russian.Puzzle;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VograbularyEntryPoint implements EntryPoint {

    private static final int INSERT_BUTTON_TOP_MARGIN = 4;
    private Label puzzleLabel = new Label();
    private AbsolutePanel insertPanel = new AbsolutePanel();
    private Image insertButton = new Image(GWT.getHostPageBaseURL() + "images/insert.png");
    private Label targetWord1 = new Label();
    private Label targetWord2 = new Label();
    private int puzzleIndex;
    private String[] puzzleLines;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        String puzzleText = Assets.INSTANCE.russianDolls().getText();
        puzzleLines = puzzleText.split("\\n");
        puzzleIndex = 0;
        
        insertPanel.setSize("100%", "40px");
        insertPanel.add(insertButton, 50, INSERT_BUTTON_TOP_MARGIN);
        final Button nextButton = new Button("Next");
        final Label errorLabel = new Label();

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("puzzleLabelContainer").add(puzzleLabel);
        RootPanel.get("insertButton").add(insertPanel);
        RootPanel.get("targetWord1").add(targetWord1);
        RootPanel.get("targetWord2").add(targetWord2);
        RootPanel.get("nextButtonContainer").add(nextButton);
        RootPanel.get("errorLabelContainer").add(errorLabel);

        // Focus the cursor on the name field when the app loads
        nextButton.setFocus(true);
        displayPuzzle();

        nextButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                puzzleIndex = (puzzleIndex+1) % puzzleLines.length;
                displayPuzzle();
            }
        });
        
        Dragger dragger = new Dragger(insertButton);
        insertButton.addMouseDownHandler(dragger);
        insertButton.addMouseMoveHandler(dragger);
        insertButton.addMouseUpHandler(dragger);
    }
    
    private void displayPuzzle() {
        Puzzle puzzle = new Puzzle(puzzleLines[puzzleIndex]);
        puzzleLabel.setText(puzzle.getClue());
        targetWord1.setText(puzzle.getTarget(0));
        targetWord2.setText(puzzle.getTarget(1));
    }
    
    private class Dragger
    implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {
        private UIObject target;
        private int startX;
        private boolean isDragging;
        
        public Dragger(UIObject target) {
            this.target = target;
        }
        
        @Override
        public void onMouseDown(MouseDownEvent event) {
            event.preventDefault();
            Event.setCapture(target.getElement());
            startX = event.getClientX() - insertPanel.getWidgetLeft(insertButton);
            isDragging = true;
        }
        
        @Override
        public void onMouseMove(MouseMoveEvent event) {
            if (isDragging) {
                insertPanel.setWidgetPosition(
                        insertButton,
                        event.getClientX() - startX, 
                        INSERT_BUTTON_TOP_MARGIN);
            }
        }

        @Override
        public void onMouseUp(MouseUpEvent event) {
            Event.releaseCapture(target.getElement());
            isDragging = false;
        }
    }
}
