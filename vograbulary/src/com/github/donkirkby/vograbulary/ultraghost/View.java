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
    private Label letters;
    private Label studentName;
    private TextField solution;
    private TextField challenge;
    private Label result;
    private Label scores;
    private TextButton button;
    private boolean isFocusMovedAutomatically;
    
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
        button = new TextButton("Next", skin);
        table.add(button);
        table.row();
        challenge = new TextField("", skin);
        table.add(challenge).expandX().fillX().pad(5);
        table.row();
        result = new Label(" ", skin);
        table.add(result).fillX();
        TextButton menuButton = new TextButton("Menu", skin);
        table.add(menuButton);
        table.row();
        scores = new Label(" \n ", skin);
        table.add(scores).fillX();
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.next();
            }
        });
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.showMenu();
            }
        });
        table.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    controller.next();
                    return true;
                }
                return false;
            }
        });
        focusNextButton();
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
        this.solution.setText(solution);
    }
    
    /**
     * Get the entered solution to the puzzle.
     */
    public String getSolution() {
        return solution.getText();
    }
    
    /**
     * Display a challenge to the solution.
     */
    public void setChallenge(String challenge) {
        this.challenge.setText(challenge);
    }
 
    /**
     * Get the entered challenge.
     */
    public String getChallenge() {
        return challenge.getText();
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
        solution.getStage().setKeyboardFocus(solution);
        solution.getOnscreenKeyboard().show(true);
    }
    
    /**
     * Set the display's focus in the challenge field.
     */
    public void focusChallenge() {
        challenge.getStage().setKeyboardFocus(
                isFocusMovedAutomatically ? solution: challenge);
        challenge.getOnscreenKeyboard().show(true);
    }
    
    /**
     * Set the display's focus on the Next button.
     */
    public void focusNextButton() {
        button.getStage().setKeyboardFocus(button);
        solution.getOnscreenKeyboard().show(false);
    }
    
    public void setFocusMovedAutomatically(boolean isFocusMovedAutomatically) {
        this.isFocusMovedAutomatically = isFocusMovedAutomatically;
    }
}
//resumeJesting
