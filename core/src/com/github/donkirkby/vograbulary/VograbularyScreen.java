package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class VograbularyScreen implements Screen {
    //stopJesting
    private VograbularyApp app;
    private Stage stage;
    
    public VograbularyScreen(VograbularyApp app) {
        this.app = app;
        this.stage = new Stage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
    @Override
    public void pause() {
    }
    
    @Override
    public void resume() {
    }
    
    @Override
    public void hide() {
    }
    
    @Override
    public void resize(int width, int height) {
        stage.setViewport(width, height);
    }
    
    public VograbularyApp getApp() {
        return app;
    }
    public Stage getStage() {
        return stage;
    }
    //resumeJesting
}
