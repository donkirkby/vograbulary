package com.github.donkirkby.vograbulary.core.russian;

import react.UnitSlot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;

import com.github.donkirkby.vograbulary.core.ChallengeScreen;

public class RussianDollsScreen extends ChallengeScreen {
    private Puzzle puzzle;
    private Label puzzleLabel;
    private Button insertButton;
    private Label target1Label;
    private Label target2Label;
    private Button solveButton;
    private Label puzzleScore;
    private Label totalScore;
    private Button menuButton;

    @Override
    protected Group createBody() {
        Button backButton = new Button("Back");
        solveButton = new Button("Solve");
        Group outerTable = new Group(AxisLayout.vertical().offEqualize())
        .add(
                puzzleLabel = new Label("").addStyles(Style.TEXT_WRAP.on),
                new Group(new AbsoluteLayout()).add(
                        insertButton = AbsoluteLayout.at(
                                new Button("Insert"),
                                0,
                                0)),
                new Group(AxisLayout.horizontal()).add(
                        target1Label = AxisLayout.stretch(new Label("1")),
                        target2Label = AxisLayout.stretch(new Label("2"))),
                new Group(AxisLayout.horizontal()).add(
                        backButton,
                        solveButton,
                        menuButton = new Button("Menu")),
                new Group(AxisLayout.horizontal()).add(
                        new Label("Score:"),
                        puzzleScore = new Label("0")),
                new Group(AxisLayout.horizontal()).add(
                        new Label("Total:"),
                        totalScore = new Label("0")));
        
        insertButton.onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                puzzleLabel.text.update("Clicked.");
            }
        });
        return outerTable;
    }

    @Override
    public Button getMenuButton() {
        return menuButton;
    }

    @Override
    public String getName() {
        return "Russian Dolls";
    }
    
    public Puzzle getPuzzle() {
        return puzzle;
    }
    
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }
}
