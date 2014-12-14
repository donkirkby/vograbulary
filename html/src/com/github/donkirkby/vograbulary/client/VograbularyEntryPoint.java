package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VograbularyEntryPoint implements EntryPoint {

    private int puzzleIndex;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        String puzzleText = Assets.INSTANCE.russianDolls().getText();
        final String[] puzzleLines = puzzleText.split("\\n");
        puzzleIndex = 0;
        
        final Button nextButton = new Button("Next");
        final Label puzzleLabel = new Label();
        puzzleLabel.setText(puzzleLines[0]);
        final Label errorLabel = new Label();

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("puzzleLabelContainer").add(puzzleLabel);
        RootPanel.get("nextButtonContainer").add(nextButton);
        RootPanel.get("errorLabelContainer").add(errorLabel);

        // Focus the cursor on the name field when the app loads
        nextButton.setFocus(true);


        // Create a handler for the sendButton and nameField
        class MyHandler implements ClickHandler {
            /**
             * Fired when the user clicks on the sendButton.
             */
            public void onClick(ClickEvent event) {
                puzzleLabel.setText(puzzleLines[++puzzleIndex]);
            }
        }

        // Add a handler to send the name to the server
        MyHandler handler = new MyHandler();
        nextButton.addClickHandler(handler);
    }
}
