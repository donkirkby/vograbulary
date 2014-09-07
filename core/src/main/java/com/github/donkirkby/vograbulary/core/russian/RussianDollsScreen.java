package com.github.donkirkby.vograbulary.core.russian;

import java.io.Reader;
import java.io.StringReader;

import playn.core.PlayN;
import playn.core.Pointer.Event;
import playn.core.Pointer.Adapter;
import playn.core.util.Callback;
import react.UnitSlot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.ImageButton;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;

import com.github.donkirkby.vograbulary.core.ChallengeScreen;
import com.github.donkirkby.vograbulary.core.ultraghost.WordList;

public class RussianDollsScreen extends ChallengeScreen {
    private Puzzle puzzle;
    private Label puzzleLabel;
    private ImageButton insertButton;
    private Label target1Label;
    private Label target2Label;
    private Button solveButton;
    private Label puzzleScore;
    private Label totalScore;
    private Button menuButton;
    private Controller controller;

    @Override
    protected Group createBody() {
        Button backButton = new Button("Back");
        solveButton = new Button("Solve");
        insertButton = new ImageButton(PlayN.assets().getImage(
                "images/insert.png").subImage(0, 0, 64, 64));
        Group outerTable = new Group(AxisLayout.vertical().offEqualize())
        .add(
                puzzleLabel = new Label("").addStyles(Style.TEXT_WRAP.on),
                new Group(new AbsoluteLayout()).add(
                        AbsoluteLayout.at(
                                insertButton,
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
        
        insertButton.layer.addListener(new Adapter() {
            private float prevX;

            @Override
            public void onPointerStart(Event event) {
                prevX = event.x();
            }
            
            @Override
            public void onPointerDrag(Event event) {
                System.out.println(event.x());
                insertButton.layer.transform().translateX(event.x() - prevX);
                prevX = event.x();
            }
        });
        
        controller = new Controller();
        controller.setScreen(this);

        PlayN.assets().getText("russianDolls.txt", new Callback<String>() {
            @Override
            public void onSuccess(String text) {
                loadPuzzles(text);
            }

            @Override
            public void onFailure(Throwable cause) {
                loadPuzzles("Failed to load puzzles: " + cause.getMessage());
            }
        });
        
        PlayN.assets().getText("wordlist.txt", new Callback<String>() {
            @Override
            public void onSuccess(String text) {
                WordList wordList = new WordList();
                
                wordList.read(new StringReader(text)); // closes the reader
                controller.setWordList(wordList);
            }
            
            @Override
            public void onFailure(Throwable cause) {
                loadPuzzles("Failed to load words: " + cause.getMessage());
            }
        });
        
        solveButton.onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                float insertX = insertButton.x() + insertButton.size().width()/2;
                Label target;
                int wordIndex;
                if (insertX < target2Label.x()) {
                    target = target1Label;
                    wordIndex = 0;
                }
                else {
                    target = target2Label;
                    wordIndex = 1;
                }
                puzzle.setTargetWord(wordIndex);
                String word = puzzle.getTarget(wordIndex);
                int charIndex = 
                        (int)(0.5 + (insertX - target.x()) /
                        target.size().width() * word.length());
                puzzle.setTargetCharacter(charIndex);
                controller.solve();
                if (puzzle.isSolved()) {
                    controller.next();
                }
            }
        });
        backButton.onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                controller.back();
            }
        });
        return outerTable;
    }
    
    private void loadPuzzles(String text) {
        Reader reader = new StringReader(text);
        controller.loadPuzzles(reader); // closes the reader
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
        puzzleLabel.text.update(puzzle.getClue());
        target1Label.text.update(puzzle.getTarget(0));
        target2Label.text.update(puzzle.getTarget(1));
        puzzleScore.text.update(puzzle.getScoreDisplay());
        totalScore.text.update(puzzle.getTotalScoreDisplay());
    }
}
