package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.utils.Timer.Task;

public interface View {

    /**
     * Schedule a task with the timer.
     */
    void schedule(Task task, float delaySeconds, float intervalSeconds);

    /**
     * Display the puzzle letters.
     */
    void setPuzzle(String letters);

    /**
     * Display the active player.
     */
    void setActivePlayer(String name);
    
    /**
     * Display a solution to the puzzle.
     */
    void setSolution(String solution);
    
    /**
     * Set the display's focus in the solution field.
     */
    void focusSolution();
    
    /**
     * Set the display's focus on the Next button.
     */
    void focusNextButton();
}