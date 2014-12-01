package com.github.donkirkby.vograbulary.core.ultraghost;

import tripleplay.ui.Button;
import tripleplay.ui.Field;
import tripleplay.ui.Label;

//import com.github.donkirkby.vograbulary.VograbularyApp;

public class OldView {
//    
//    
//    public void clear() {
//        studentName.setText(" ");
//        letters.setText(" ");
//        solution.setText("");
//        response.setText("");
//        hint.setText(" ");
//        result.setText("");
//        scores.setText("");
//    }
//
//    /**
//     * Schedule a task with the timer.
//     */
//    public void schedule(Task task, float delaySeconds, float intervalSeconds) {
//        Timer.schedule(task, delaySeconds, intervalSeconds);
//    }
//
//    /**
//     * Set the display's focus in the solution field.
//     */
//    public void focusSolution() {
//        focusButton(solveButton);
//        focusField(solution);
//    }
//
//    private void focusField(TextField field) {
//        field.getStage().setKeyboardFocus(field);
//        field.getOnscreenKeyboard().show(true);
//        field.selectAll();
//    }
//    
//    /**
//     * Set the display's focus in the response field.
//     */
//    public void focusResponse() {
//        focusButton(respondButton);
//        focusField(response);
//    }
//    
//    private void focusButton(TextButton target) {
//        for (TextButton button : focusButtons) {
//            button.setVisible(button == target);
//        }
//    }
//    
//    /**
//     * Set the display's focus on the Next button.
//     */
//    public void focusNextButton() {
//        focusButton(nextButton);
//        nextButton.getStage().setKeyboardFocus(nextButton);
//        solution.getOnscreenKeyboard().show(false);
//    }
//    
//    /**
//     * Disable all the buttons, except menu, while the computer student is
//     * thinking of a solution.
//     */
//    public void showThinking() {
//        focusButton(null);
//    }
//    
//    public Puzzle getPuzzle() {
//        return match.getPuzzle();
//    }
//
//    /**
//     * Set the match state for display. This will update several fields.
//     */
//    public void setMatch(Match match) {
//        this.match = match;
//        refreshPuzzle();
//    }
//    
//    public Match getMatch() {
//        return match;
//    }
//    
//    /**
//     * Update the display to show all parts of the puzzle.
//     */
//    public void refreshPuzzle() {
//        if (letters == null) {
//            // must be in unit tests, nothing to do.
//            return;
//        }
//        if (match == null) {
//            return;
//        }
//        Student winner = match.getWinner();
//        scores.setText(match.getSummary());
//        if (winner != null) {
//            String resultText = winner.getName() + " win";
//            if (winner.getName() != "You") {
//                resultText += "s";
//            }
//            result.setText(resultText);
//            studentName.setText(" ");
//            letters.setText(" ");
//            solution.setText(" ");
//            response.setText(" ");
//            hint.setText(" ");
//            for (TextButton button : focusButtons) {
//                button.setVisible(false);
//            }
//            return;
//        }
//        Puzzle puzzle = match.getPuzzle();
//        if (puzzle != null) {
//            Student owner = puzzle.getOwner();
//            studentName.setText(owner == null ? "" : owner.getName());
//            letters.setText(blankForNull(puzzle.getLettersDisplay()));
//            setFieldContents(solution, puzzle.getSolution());
//            setFieldContents(response, puzzle.getResponse());
//            hint.setText(blankForNull(puzzle.getHint()) + " ");
//            WordResult puzzleResult = puzzle.getResult();
//            result.setText(
//                    puzzleResult == WordResult.UNKNOWN 
//                    ? "" 
//                    : puzzleResult.toString());
//        }
//    }
//
//    private void setFieldContents(TextField field, String contents) {
//        if (field.getText() != contents) {
//            // Only set it when the value changes, otherwise the selection
//            // gets lost.
//            field.setText(blankForNull(contents));
//        }
//    }
//    
//    private String blankForNull(Object o) {
//        return o == null ? "" : o.toString();
//    }
    //resumeJesting
}
