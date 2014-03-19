package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {
    //stopJesting
    private VograbularyApp app;
    private Stage stage;
    private Label skillLabel;
    private Slider vocabularySize;

    public MenuScreen(VograbularyApp vograbularyApp) {
        app = vograbularyApp;
        stage = new Stage();
        
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.align(Align.top);
        Skin skin = app.getSkin();
        TextButton humanButton = new TextButton("Human vs. Human", skin);
        table.add(humanButton).row();
        TextButton computerButton = new TextButton("Human vs. Computer", skin);
        table.add(computerButton).row();
        skillLabel = new Label("", skin);
        int defaultVocabularySize = 5000;
        setVocabularySize(defaultVocabularySize);
        table.add(skillLabel).row();
        boolean isVertical = false;
        vocabularySize = new Slider(1, 65000, 100, isVertical, skin);
        vocabularySize.setValue(defaultVocabularySize);
        table.add(vocabularySize).fillX();
        
        computerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isComputerOpponent = true;
                app.startUltraghost(isComputerOpponent);
            }
        });
        
        humanButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isComputerOpponent = false;
                app.startUltraghost(isComputerOpponent);
            }
        });
        
        vocabularySize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setVocabularySize((int)vocabularySize.getValue());
            }
        });
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private void setVocabularySize(int skill) {
        skillLabel.setText("Skill " + skill);
        app.getConfiguration().setVocabularySize(skill);
    }
    //resumeJesting
}
