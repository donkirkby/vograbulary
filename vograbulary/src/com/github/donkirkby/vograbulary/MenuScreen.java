package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {
    private VograbularyApp app;
    private Stage stage;

    public MenuScreen(VograbularyApp vograbularyApp) {
        app = vograbularyApp;
        stage = new Stage();
        
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.align(Align.top);
        Skin skin = app.getSkin();
        TextButton computerButton = new TextButton("Human vs. Computer", skin);
        table.add(computerButton);
        table.row();
        TextButton humanButton = new TextButton("Human vs. Human", skin);
        table.add(humanButton);
        
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
}
