package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class View {
    private Label letters;
    private Label playerName;
    private TextField solution;
    private TextField challenge;
    private Label result;
    private TextButton button;
    
    public void create(
            final Table table, 
            final Skin skin, 
            final Controller controller) {
        controller.setView(this);
        table.align(Align.top);
        playerName = new Label(" ", skin);
        table.add(playerName);
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
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.next();
            }
        });
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
     * Display the active player.
     */
    public void setActivePlayer(String name) {
        playerName.setText(name);
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
        challenge.getStage().setKeyboardFocus(challenge);
        challenge.getOnscreenKeyboard().show(true);
    }
    
    /**
     * Set the display's focus on the Next button.
     */
    public void focusNextButton() {
        button.getStage().setKeyboardFocus(button);
        solution.getOnscreenKeyboard().show(false);
    }
}