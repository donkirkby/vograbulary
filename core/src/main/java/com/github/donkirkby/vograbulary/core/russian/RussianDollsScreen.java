package com.github.donkirkby.vograbulary.core.russian;

import java.io.Reader;
import java.io.StringReader;

import playn.core.Image.Region;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer.Adapter;
import playn.core.Pointer.Event;
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
    private Label target1Label;
    private Label target2Label;
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
        final Shim insertShim = new Shim(64, 64);
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
                            positionInImage.x + insertShim.size().width()/2 - 32,
                            positionInImage.y);
                }
            };
        }.offEqualize();
        Group outerTable = new Group(layout)
        .add(
                puzzleLabel = new Label("").addStyles(Style.TEXT_WRAP.on),
                insertShim,
                new Group(AxisLayout.horizontal()).add(
                        AxisLayout.stretch(new Shim(1, 1)),
                        target1Label = new Label("1"),
                        AxisLayout.stretch(new Shim(1, 1)),
                        target2Label = new Label("2"),
                        AxisLayout.stretch(new Shim(1, 1))),
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
        
        Region insertImage = PlayN.assets().getImage(
                "images/insert.png").subImage(0, 0, 64, 64);
        insertLayer = PlayN.graphics().createImageLayer(insertImage);
        layer.addAt(insertLayer, 150, 160);
        insertLayer.addListener(new Adapter() {
            private float prevX;

            @Override
            public void onPointerStart(Event event) {
                prevX = event.x();
            }
            
            @Override
            public void onPointerDrag(Event event) {
                insertLayer.transform().translateX(event.x() - prevX);
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
                positionInImage.set(insertLayer.width()/2, 0);
                Layers.transform(
                        positionInImage,
                        insertLayer,
                        target2Label.layer,
                        positionInLabel);
                Label target;
                int wordIndex;
                if (positionInLabel.x >= 0) {
                    target = target2Label;
                    wordIndex = 1;
                }
                else {
                    target = target1Label;
                    wordIndex = 0;
                    Layers.transform(
                            positionInImage,
                            insertLayer,
                            target1Label.layer,
                            positionInLabel);
                }
                puzzle.setTargetWord(wordIndex);
                String word = puzzle.getTarget(wordIndex);
                int charIndex = 
                        (int)(0.5 + positionInLabel.x /
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
