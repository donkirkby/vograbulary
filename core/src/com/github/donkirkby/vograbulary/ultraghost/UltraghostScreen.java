package com.github.donkirkby.vograbulary.ultraghost;


public interface UltraghostScreen {
    public void clear();

    /**
     * Set the display's focus in the solution field.
     */
    public void focusSolution();

    /**
     * Set the display's focus in the response field.
     */
    public void focusResponse();

    /**
     * Set the display's focus on the Next button.
     */
    public void focusNextButton();

    /**
     * Disable all the buttons, except menu, while the computer student is
     * thinking of a solution.
     */
    public void showThinking();

    /**
     * Set the match state for display. This will update several fields.
     */
    public void setMatch(Match match);
    public Match getMatch();

    /**
     * Update the display to show all parts of the puzzle.
     */
    public void refreshPuzzle();
}
