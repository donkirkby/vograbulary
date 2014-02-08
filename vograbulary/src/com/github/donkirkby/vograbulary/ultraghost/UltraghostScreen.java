package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.donkirkby.vograbulary.VograbularyApp;

public class UltraghostScreen implements Screen {
    private VograbularyApp app;
    private Stage stage;
    private Controller ultraghostController = 
            new Controller();
    private boolean isComputerOpponent;
    private View view;

    public UltraghostScreen(VograbularyApp vograbularyApp) {
        app = vograbularyApp;
        stage = new Stage();
        
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        view = new View();
        view.setFocusMovedAutomatically(app.isFocusMovedAutomatically());
        view.create(table, app, ultraghostController);
        ultraghostController.readWordList(
                Gdx.files.internal("data/wordlist.txt").reader());
    }
    
    public void setComputerOpponent(boolean isComputerOpponent) {
        this.isComputerOpponent = isComputerOpponent;
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
        view.clear();
        ComputerStudent computerStudent = new ComputerStudent();
        computerStudent.setSearchBatchSize(30);
        computerStudent.setMaxSearchBatchCount(1000); // 10s
        ultraghostController.clearStudents();
        ultraghostController.addStudent(new Student("Don"));
        ultraghostController.addStudent(
                isComputerOpponent
                ? computerStudent
                : new Student("Sheila"));

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
