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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.VograbularyApp;

public class View {
    //stopJesting
    private static final String NO_ANSWER = "None";
    private Puzzle puzzle;
    private Label letters;
    private Label studentName;
    private TextField solution;
    private String solutionWord;
    private TextField response;
    private String responseWord;
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
        TextButton menuButton = new TextButton("Menu", skin);
        table.add(menuButton).left();
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
        
        scores = new Label(" \n ", skin);
        table.add(scores).fillX();
        
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
                puzzle.setSolution(solution.getText());
                controller.solve();
            }
        });
        respondButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                puzzle.setResponse(response.getText());
                controller.respond();
            }
        });
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.showMenu();
            }
        });
//        table.addListener(new InputListener() {
//            @Override
//            public boolean keyUp(InputEvent event, int keycode) {
//                if (keycode == Input.Keys.ENTER) {
//                    controller.next();
//                    return true;
//                }
//                return false;
//            }
//        });
        // On some platforms, setting the focus in an enter key handler is
        // not compatible with the default focus traversal. Disable it.
        solution.setFocusTraversal(false);
        response.setFocusTraversal(false);
        
        solution.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                solutionWord = solution.getText();
            }
        });
        response.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                responseWord = response.getText();
            }
        });
        focusNextButton();
    }
    
    public void clear() {
        setPuzzle(" ");
        setSolution(" ");
        setChallenge("");
        setResult("");
        setScores("");
    }

    /**
     * Schedule a task with the timer.
     */
    public void schedule(Task task, float delaySeconds, float intervalSeconds) {
        Timer.schedule(task, delaySeconds, intervalSeconds);
    }

    /**
     * Display the puzzle letters.
     */
    public void setPuzzle(String letters) {
        this.letters.setText(letters);
    }

    /**
     * Display the active student.
     */
    public void setActiveStudent(String name) {
        studentName.setText(name);
    }
    
    /**
     * Display a solution to the puzzle.
     */
    public void setSolution(String solution) {
        solutionWord = solution;
        this.solution.setText(solution != null ? solution : NO_ANSWER);
    }
    
    /**
     * Get the entered solution to the puzzle.
     */
    public String getSolution() {
        return solutionWord;
    }
    
    /**
     * Display a challenge to the solution.
     */
    public void setChallenge(String challenge) {
        responseWord = challenge;
        this.response.setText(challenge != null ? challenge : NO_ANSWER);
    }
 
    /**
     * Get the entered challenge.
     */
    public String getChallenge() {
        return responseWord;
    }
    
    /**
     * Display the result of a solution or challenge.
     */
    public void setResult(String result) {
        this.result.setText(result);
    }
    
    /**
     * Get the displayed result.
     */
    public String getResult() {
        return result.getText().toString();
    }
    
    /**
     * Display a list of scores, on multiple lines.
     */
    public void setScores(String scores) {
        this.scores.setText(scores);
    }
    
    /**
     * Set the display's focus in the solution field.
     */
    public void focusSolution() {
        focusButton(solveButton);
        solution.getStage().setKeyboardFocus(solution);
        solution.getOnscreenKeyboard().show(true);
    }
    
    /**
     * Set the display's focus in the response field.
     */
    public void focusResponse() {
        focusButton(respondButton);
        response.getStage().setKeyboardFocus(response);
        response.getOnscreenKeyboard().show(true);
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
        return puzzle;
    }

    /**
     * Set the puzzle state for display. This will update several fields.
     */
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
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
        Student owner = puzzle.getOwner();
        studentName.setText(owner == null ? "" : owner.getName());
        letters.setText(blankForNull(puzzle.getLetters()));
        solution.setText(blankForNull(puzzle.getSolution()));
        response.setText(blankForNull(puzzle.getResponse()));
        WordResult puzzleResult = puzzle.getResult();
        result.setText(
                puzzleResult.getScore() == 0 
                ? "" 
                : puzzleResult.toString());
    }
    
    private String blankForNull(Object o) {
        return o == null ? "" : o.toString();
    }
    //resumeJesting
}
