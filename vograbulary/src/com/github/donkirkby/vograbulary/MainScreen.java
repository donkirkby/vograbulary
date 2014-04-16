package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreen implements Screen {
    private static final String EMPTY_PLAYER_LIST = "\n ";
    private Stage stage;
    private TextButton showButton;
    private TextButton preferencesButton;
    private Label playerNames;

    public MainScreen(final VograbularyGame game) {
        Skin skin = game.getSkin();
        
        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        showButton = new TextButton("Show Players", skin);
        table.add(showButton);
        preferencesButton = new TextButton("Preferences...", skin);
        table.add(preferencesButton).row();
        playerNames = new Label(EMPTY_PLAYER_LIST, skin);
        table.add(playerNames);
        showButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                VograbularyPreferences preferences = game.getPreferences();
                playerNames.setText(
                        preferences.getPlayer1Name() + "\n" +
                        preferences.getPlayer2Name()); 
            }
        });
        preferencesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showPreferences();
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
        playerNames.setText(EMPTY_PLAYER_LIST);
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
        stage.dispose();
    }

}
