package com.github.donkirkby.vograbulary.russian;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.esotericsoftware.tablelayout.Cell;
import com.github.donkirkby.vograbulary.VograbularyApp;
import com.github.donkirkby.vograbulary.VograbularyScreen;
import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class RussianDollsScreen extends VograbularyScreen {
    //stopJesting
    private Puzzle puzzle;
    private Label puzzleLabel;
    private Cell<Label> puzzleCell;
    private ImageButton insertButton;
    private Label target1Label;
    private Label target2Label;
    private Cell<Label> target1Cell;
    private TextButton solveButton;
    private Label puzzleScore;
    private Label totalScore;
    private Controller controller;
    
    // TODO: Remove @SuppressWarnings after migrating to libgdx 1.0.1.
    @SuppressWarnings("unchecked")
    public RussianDollsScreen(final VograbularyApp app) {
        super(app);
        Stage stage = getStage();
        
        Skin skin = app.getSkin();
        VerticalGroup outerTable = new VerticalGroup();
        outerTable.setFillParent(true);
        stage.addActor(outerTable);
        Table table = new Table(skin);
        outerTable.addActor(table);
        
        puzzleLabel = new Label("", skin);
        puzzleLabel.setWrap(true);
        puzzleCell = table.add(puzzleLabel).colspan(2);
        puzzleCell.width(Gdx.graphics.getWidth());
        puzzleCell.row();
        insertButton = new ImageButton(skin.getDrawable("insert"));
        table.add(insertButton).colspan(2).row();
        target1Label = new Label("", skin);
        target2Label = new Label("", skin);
        target1Cell = table.add(target1Label);
        table.add(target2Label);
        
        table = new Table(skin);
        outerTable.addActor(table);
        float padding = 5;
        TextButton backButton = new TextButton("Back", skin);
        solveButton = new TextButton("Solve", skin);
        TextButton menuButton = new TextButton("Menu", skin);
        table.add(backButton).pad(padding);
        table.add(solveButton).pad(padding);
        table.add(menuButton).pad(padding);
        
        table = new Table(skin);
        outerTable.addActor(table);
        table.add("Score:");
        puzzleScore = new Label("0", skin);
        table.add(puzzleScore).row();
        table.add("Total:");
        totalScore = new Label("0", skin);
        table.add(totalScore);
        
        final List<Label> targets = Arrays.asList(target1Label, target2Label);
        
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.showMenu();
            }
        });
        
        controller = new Controller();
        controller.setScreen(this);
        Reader reader = Gdx.files.internal("data/russianDolls.txt").reader();
        controller.loadPuzzles(reader); // closes the reader
        WordList wordList = new WordList();
        wordList.read(
                Gdx.files.internal("data/wordlist.txt").reader());
        controller.setWordList(wordList);
        
        solveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (puzzle.isSolved()) {
                    prepareToSolve();
                    controller.next();
                    return;
                }
                float insertX = insertButton.getX() + insertButton.getWidth()/2;
                boolean touchableOnly = false;
                Actor target = getStage().hit(
                        insertX, 
                        target1Label.getHeight()/2 +
                            target1Label.getY() +
                            target1Label.getParent().getY(), 
                        touchableOnly);
                
                int wordIndex = targets.indexOf(target);
                if (wordIndex < 0) {
                    return;
                }
                puzzle.setTargetWord(wordIndex);
                String word = puzzle.getTarget(wordIndex);
                int charIndex = 
                        (int)(0.5 + (insertX - target.getX()) /
                        target.getWidth() * word.length());
                puzzle.setTargetCharacter(charIndex);
                controller.solve();
                if (puzzle.isSolved()) {
                    target1Label.setText(puzzle.getCombination());
                    totalScore.setText(puzzle.getTotalScoreDisplay());
                    target1Cell.colspan(2);
                    target2Label.setVisible(false);
                    insertButton.setVisible(false);
                    solveButton.setText("Next");
                }
            }
        });
        
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                prepareToSolve();
                controller.back();
            }
        });
        
        DragListener dragListener = new DragListener() {
            private float startDragX;
            
            @Override
            public void dragStart(
                    InputEvent event, 
                    float x, 
                    float y,
                    int pointer) {
                startDragX = x;
            }
            
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                insertButton.translate(x - startDragX, 0);
            }
        };
        dragListener.setTapSquareSize(2);
        insertButton.addListener(dragListener);
    }
    
    @Override
    public void render(float delta) {
        puzzleScore.setText(controller.adjustScore(delta));
        super.render(delta);
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        puzzleLabel.setText(puzzle.getClue());
        target1Label.setText(puzzle.getTarget(0));
        target2Label.setText(puzzle.getTarget(1));
        puzzleScore.setText(puzzle.getScoreDisplay());
        totalScore.setText(puzzle.getTotalScoreDisplay());
    }
    public Puzzle getPuzzle() {
        return puzzle;
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        puzzleCell.width(width);
    }
    
    private void prepareToSolve() {
        target1Cell.colspan(1);
        target2Label.setVisible(true);
        insertButton.setVisible(true);
        solveButton.setText("Solve");
    }
    //resumeJesting
}
