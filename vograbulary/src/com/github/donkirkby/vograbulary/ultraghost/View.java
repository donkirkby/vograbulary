package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.VograbularyApp;

public class View {
    //stopJesting
    private Match match;
    private Label letters;
    private Label studentName;
    private TextField solution;
    private TextField response;
    private Label hint;
    private Label result;
    private Label scores;
    private TextButton solveButton;
    private TextButton respondButton;
    private TextButton nextButton;
    private TextButton[] focusButtons;
    
    public void create(
            final Table table, 
            final VograbularyApp app, 
            final Controller controller) {
        controller.setView(this);
        Skin skin = app.getSkin();
        table.top();
        studentName = new Label(" ", skin);
        table.add(studentName);
        table.row();
        
        letters = new Label(" ", skin);
        table.add(letters);
        table.row();
        
        solution = new TextField("", skin);
        table.add(solution).expandX().fillX().pad(5);
        solveButton = new TextButton("Solve", skin);
        table.add(solveButton).left();
        table.row();
        
        response = new TextField("", skin);
        table.add(response).expandX().fillX().pad(5);
        respondButton = new TextButton("Respond", skin);
        table.add(respondButton).left();
        table.row();
        
        result = new Label(" ", skin);
        table.add(result).fillX();
        nextButton = new TextButton("Next", skin);
        table.add(nextButton).left();
        table.row();
        
        hint = new Label(" ", skin);
        table.add(hint).fillX();
        table.row();
        
        scores = new Label(" \n ", skin);
        table.add(scores).fillX();
        TextButton menuButton = new TextButton("Menu", skin);
        table.add(menuButton).left().top();
        
        focusButtons = 
                new TextButton[] {solveButton, respondButton, nextButton};
        
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.start();
            }
        });
        solveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                match.getPuzzle().setSolution(solution.getText());
                controller.solve();
            }
        });
        respondButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                match.getPuzzle().setResponse(response.getText());
                controller.respond();
            }
        });
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.cancelMatch();
                app.showMenu();
            }
        });
        table.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    ChangeListener.ChangeEvent changeEvent = 
                            new ChangeListener.ChangeEvent();
                    for (TextButton button : focusButtons) {
                        if (button.isVisible()) {
                            button.fire(changeEvent);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        // On some platforms, setting the focus in an enter key handler is
        // not compatible with the default focus traversal. Disable it.
        solution.setFocusTraversal(false);
        response.setFocusTraversal(false);
        
        focusNextButton();
    }
    
    public void clear() {
        studentName.setText(" ");
        letters.setText(" ");
        solution.setText("");
        response.setText("");
        hint.setText(" ");
        result.setText("");
        scores.setText("");
    }

    /**
     * Schedule a task with the timer.
     */
    public void schedule(Task task, float delaySeconds, float intervalSeconds) {
        Timer.schedule(task, delaySeconds, intervalSeconds);
    }

    /**
     * Set the display's focus in the solution field.
     */
    public void focusSolution() {
        focusButton(solveButton);
        focusField(solution);
    }

    private void focusField(TextField field) {
        field.getStage().setKeyboardFocus(field);
        field.getOnscreenKeyboard().show(true);
        field.selectAll();
    }
    
    /**
     * Set the display's focus in the response field.
     */
    public void focusResponse() {
        focusButton(respondButton);
        focusField(response);
    }
    
    private void focusButton(TextButton target) {
        for (TextButton button : focusButtons) {
            button.setVisible(button == target);
        }
    }
    
    /**
     * Set the display's focus on the Next button.
     */
    public void focusNextButton() {
        focusButton(nextButton);
        nextButton.getStage().setKeyboardFocus(nextButton);
        solution.getOnscreenKeyboard().show(false);
    }
    
    /**
     * Disable all the buttons, except menu, while the computer student is
     * thinking of a solution.
     */
    public void showThinking() {
        focusButton(null);
    }
    
    public Puzzle getPuzzle() {
        return match.getPuzzle();
    }

    /**
     * Set the match state for display. This will update several fields.
     */
    public void setMatch(Match match) {
        this.match = match;
        refreshPuzzle();
    }
    
    /**
     * Update the display to show all parts of the puzzle.
     */
    public void refreshPuzzle() {
        if (letters == null) {
            // must be in unit tests, nothing to do.
            return;
        }
        Student winner = match.getWinner();
        scores.setText(match.getSummary());
        if (winner != null) {
            result.setText(winner.getName() + " wins");
            studentName.setText(" ");
            letters.setText(" ");
            solution.setText(" ");
            response.setText(" ");
            hint.setText(" ");
            for (TextButton button : focusButtons) {
                button.setVisible(false);
            }
            return;
        }
        Puzzle puzzle = match.getPuzzle();
        Student owner = puzzle.getOwner();
        studentName.setText(owner == null ? "" : owner.getName());
        letters.setText(blankForNull(puzzle.getLetters()));
        setFieldContents(solution, puzzle.getSolution());
        setFieldContents(response, puzzle.getResponse());
        hint.setText(blankForNull(puzzle.getHint()) + " ");
        WordResult puzzleResult = puzzle.getResult();
        result.setText(
                puzzleResult == WordResult.UNKNOWN 
                ? "" 
                : puzzleResult.toString());
    }

    private void setFieldContents(TextField field, String contents) {
        if (field.getText() != contents) {
            // Only set it when the value changes, otherwise the selection
            // gets lost.
            field.setText(blankForNull(contents));
        }
    }
    
    private String blankForNull(Object o) {
        return o == null ? "" : o.toString();
    }
    //resumeJesting
}
