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
    private List<Label> targets;
    private TextButton backButton;
    private TextButton solveButton;
    private TextButton menuButton;
    private Controller controller;
    private Table table;
    
    // TODO: Remove @SuppressWarnings after migrating to libgdx 1.0.1.
    @SuppressWarnings("unchecked")
    public RussianDollsScreen(final VograbularyApp app) {
        super(app);
        Stage stage = getStage();
        
        Skin skin = app.getSkin();
        table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);
        
        puzzleLabel = new Label("", skin);
        puzzleLabel.setWrap(true);
        puzzleCell = table.add(puzzleLabel).colspan(6);
        puzzleCell.width(Gdx.graphics.getWidth());
        puzzleCell.row();
        insertButton = new ImageButton(skin.getDrawable("insert"));
        table.add(insertButton).colspan(6).row();
        target1Label = new Label("", skin);
        target2Label = new Label("", skin);
        target1Cell = table.add(target1Label).colspan(3);
        table.add(target2Label).colspan(3).row();
        backButton = new TextButton("Back", skin);
        solveButton = new TextButton("Solve", skin);
        menuButton = new TextButton("Menu", skin);
        table.add(backButton).colspan(2);
        table.add(solveButton).colspan(2);
        table.add(menuButton).colspan(2).row();
        
        targets = Arrays.asList(target1Label, target2Label);
        
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.showMenu();
            }
        });
        
        solveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (puzzle.isSolved()) {
                    target1Cell.colspan(3);
                    target2Label.setVisible(true);
                    insertButton.setVisible(true);
                    controller.next();
                    return;
                }
                float insertX = insertButton.getX() + insertButton.getWidth()/2;
                boolean touchableOnly = false;
                Actor target = getStage().hit(
                        insertX, 
                        target1Label.getY() + insertButton.getHeight()/2, 
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
                    target1Cell.colspan(6);
                    target2Label.setVisible(false);
                    insertButton.setVisible(false);
                }
            }
        });
        
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
        
        controller = new Controller();
        controller.setScreen(this);
        Reader reader = Gdx.files.internal("data/russianDolls.txt").reader();
        controller.loadPuzzles(reader); // closes the reader
        WordList wordList = new WordList();
        wordList.read(
                Gdx.files.internal("data/wordlist.txt").reader());
        controller.setWordList(wordList);
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        puzzleLabel.setText(puzzle.getClue());
        target1Label.setText(puzzle.getTarget(0));
        target2Label.setText(puzzle.getTarget(1));
    }
    public Puzzle getPuzzle() {
        return puzzle;
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        puzzleCell.width(width);
    }
    //resumeJesting
}
