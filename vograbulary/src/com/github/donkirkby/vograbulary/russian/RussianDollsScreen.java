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
    private Label puzzleLabel;
    private Cell<Label> puzzleCell;
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
        puzzleCell = table.add(puzzleLabel).colspan(3);
        // TODO: remove width() when resize() is working.
        puzzleCell.width(Gdx.graphics.getWidth());
        puzzleCell.row();
        backButton = new TextButton("Back", skin);
        nextButton = new TextButton("Next", skin);
        menuButton = new TextButton("Menu", skin);
        table.add(backButton);
        table.add(nextButton);
        table.add(menuButton).row();
        
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

    public void setPuzzle(String puzzle) {
        puzzleLabel.setText(puzzle);
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
//        puzzleCell.width(width);
//        table.setSize(width, height);
//        table.invalidate();
//        table.invalidateHierarchy();
//        table.layout();
//        Gdx.app.log("russian dolls screen", "Resizing");
    }
}
