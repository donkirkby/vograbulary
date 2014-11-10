package com.github.donkirkby.vograbulary.core.russian;

import java.io.Reader;
import java.io.StringReader;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.util.Callback;
import pythagoras.f.Point;
import react.UnitSlot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Layout;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Layers;

import com.github.donkirkby.vograbulary.core.ChallengeScreen;
import com.github.donkirkby.vograbulary.core.ultraghost.WordList;

public class RussianDollsScreen extends ChallengeScreen {
    private Puzzle puzzle;
    private Label puzzleLabel;
    private ImageLayer insertLayer;
    private TargetDisplay target1;
    private TargetDisplay target2;
    private Label solutionLabel;
    private Button solveButton;
    private Label puzzleScore;
    private Label totalScore;
    private Button menuButton;
    private Controller controller;
    private Point positionOfShim;
    private Point positionOfShimPrevious;
    private Point positionInShim;
    private Point positionInImage;
    private Point positionInLabel;

    @Override
    protected Group createBody() {
        positionOfShim = new Point();
        positionOfShimPrevious = new Point();
        positionInShim = new Point();
        positionInImage = new Point();
        positionInLabel = new Point();
        Button backButton = new Button("Back");
        solveButton = new Button("Solve");
        final Shim insertShim = new Shim(32, 32);
        final Shim dragShim = new Shim(1, 80);
        Layout layout = new AxisLayout.Vertical() {
            public void layout(
                    tripleplay.ui.Container<?> elems,
                    float left,
                    float top,
                    float width,
                    float height) {
                super.layout(elems, left, top, width, height);
                positionInShim.set(0, 0);
                Layers.transform(
                        positionInShim,
                        insertShim.layer,
                        RussianDollsScreen.this.layer,
                        positionOfShim);
                if ( ! positionOfShim.equals(positionOfShimPrevious)) {
                    positionOfShimPrevious.set(positionOfShim);
                    Layers.transform(
                            positionInShim,
                            insertShim.layer,
                            insertLayer,
                            positionInImage);
                    insertLayer.transform().translate(
                            positionInImage.x + insertShim.size().width()/2 - 16,
                            positionInImage.y);
                    Layers.transform(
                            positionInShim,
                            dragShim.layer,
                            target1.getLayer(),
                            positionInImage);
                    target1.getLayer().transform().translate(
                            positionInImage.x + insertShim.size().width()/2 - 100,
                            positionInImage.y);
                    Layers.transform(
                            positionInShim,
                            dragShim.layer,
                            target2.getLayer(),
                            positionInImage);
                    target2.getLayer().transform().translate(
                            positionInImage.x + insertShim.size().width()/2 + 100,
                            positionInImage.y);
                }
            };
        }.offEqualize();
        final Group targetTable = new Group(AxisLayout.horizontal());
        Group outerTable = new Group(layout)
        .add(
                puzzleLabel = new Label("").addStyles(Style.TEXT_WRAP.on),
                insertShim,
                targetTable.add(
                        AxisLayout.stretch(new Shim(1, 1)),
                        AxisLayout.stretch(solutionLabel = new Label("")),
                        AxisLayout.stretch(new Shim(1, 1))),
                dragShim,
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
        Image insertImage = PlayN.assets().getImage("images/insert.png");
        insertLayer = PlayN.graphics().createImageLayer(insertImage);
        layer.add(insertLayer);
        new DragAdapter().setDraggingLayer(insertLayer);
        target1 = new TargetDisplay();
        layer.add(target1.getLayer());
        target2 = new TargetDisplay();
        target1.setOpposite(target2);
        layer.add(target2.getLayer());
        
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
                if (puzzle.isSolved()) {
                    prepareToSolve();
                    controller.next();
                    return;
                }
                if (target1.isVisible() && target2.isVisible()) {
                    positionInImage.set(insertLayer.width()/2, 0);
                    Layers.transform(
                            positionInImage,
                            insertLayer,
                            target2.getLayer(),
                            positionInLabel);
                    ImageLayer target;
                    int wordIndex;
                    if (positionInLabel.x >= 0 && 
                            positionInLabel.x < target2.getLayer().width()) {
                        target = target2.getLayer();
                        wordIndex = 1;
                    }
                    else {
                        target = target1.getLayer();
                        wordIndex = 0;
                        Layers.transform(
                                positionInImage,
                                insertLayer,
                                target,
                                positionInLabel);
                    }
                    puzzle.setTargetWord(wordIndex);
                    String word = puzzle.getTarget(wordIndex);
                    int charIndex =
                            (int)(0.5 + positionInLabel.x /
                            target.width() * word.length());
                    puzzle.setTargetCharacter(charIndex);
                }
                controller.solve();
                if (puzzle.isSolved()) {
                    target1.setVisible(false);
                    solutionLabel.text.update(puzzle.getCombination());
                    target2.setVisible(false);
                    totalScore.text.update(puzzle.getTotalScoreDisplay());
                    insertLayer.setVisible(false);
                    solveButton.text.update("Next");
                }
            }
        });
        backButton.onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                prepareToSolve();
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
        target1.setPuzzle(puzzle);
        target1.setVisible(true);
        target2.setPuzzle(puzzle);
        target2.setVisible(true);
        puzzleLabel.text.update(puzzle.getClue());
        solutionLabel.text.update("");
        puzzleScore.text.update(puzzle.getScoreDisplay());
        totalScore.text.update(puzzle.getTotalScoreDisplay());
    }
    
    private void prepareToSolve() {
        insertLayer.setVisible(true);
        solveButton.text.update("Solve");
    }
    
    @Override
    public void update(int delta) {
        puzzleScore.text.update(controller.adjustScore(delta/1000f));
        super.update(delta);
    }
}
