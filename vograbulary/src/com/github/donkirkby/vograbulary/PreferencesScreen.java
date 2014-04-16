package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PreferencesScreen implements Screen {
    private VograbularyGame game;
    private Stage stage;
    private Table table;
    private TextField player1Field;
    private TextField player2Field;
    private TextButton confirmButton;
    private TextButton cancelButton;

    public PreferencesScreen(final VograbularyGame game) {
        this.game = game;
        Skin skin = game.getSkin();
        
        stage = new Stage();
        table = new Table();
        stage.addActor(table);
        table.setFillParent(true);
        player1Field = new TextField("", skin);
        table.add(player1Field).colspan(2).row();
        player2Field = new TextField("", skin);
        table.add(player2Field).colspan(2).row();
        confirmButton = new TextButton("OK", skin);
        table.add(confirmButton);
        cancelButton = new TextButton("Cancel", skin);
        table.add(cancelButton).row();
        
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                VograbularyPreferences preferences = game.getPreferences();
                preferences.setPlayer1Name(player1Field.getText());
                preferences.setPlayer2Name(player2Field.getText());
                game.showMain();
            }
        });
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showMain();
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
        VograbularyPreferences preferences = game.getPreferences();
        player1Field.setText(preferences.getPlayer1Name());
        player2Field.setText(preferences.getPlayer2Name());
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
