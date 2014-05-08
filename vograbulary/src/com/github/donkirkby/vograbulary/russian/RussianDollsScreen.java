package com.github.donkirkby.vograbulary.russian;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;
import com.github.donkirkby.vograbulary.VograbularyApp;
import com.github.donkirkby.vograbulary.VograbularyScreen;

public class RussianDollsScreen extends VograbularyScreen {
    //stopJesting
    private Label puzzleLabel;
    private Cell<Label> puzzleCell;
    private Label target1Label;
    private Label target2Label;
    private TextButton backButton;
    private TextButton nextButton;
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
        target1Label = new Label("", skin);
        target2Label = new Label("", skin);
        table.add(target1Label).colspan(3);
        table.add(target2Label).colspan(3).row();
        backButton = new TextButton("Back", skin);
        nextButton = new TextButton("Next", skin);
        menuButton = new TextButton("Menu", skin);
        table.add(backButton).colspan(2);
        table.add(nextButton).colspan(2);
        table.add(menuButton).colspan(2).row();
        
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.showMenu();
            }
        });
        
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.next();
            }
        });
        
        controller = new Controller();
        controller.setScreen(this);
        Reader reader = Gdx.files.internal("data/russianDolls.txt").reader();
        controller.loadPuzzles(reader); // closes the reader
    }

    public void setPuzzle(Puzzle puzzle) {
        puzzleLabel.setText(puzzle.getClue());
        target1Label.setText(puzzle.getTarget1());
        target2Label.setText(puzzle.getTarget2());
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        puzzleCell.width(width);
    }
    //resumeJesting
}
