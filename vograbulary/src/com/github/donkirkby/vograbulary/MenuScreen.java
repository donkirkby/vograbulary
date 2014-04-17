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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {
    //stopJesting
    private VograbularyApp app;
    private Stage stage;
    private TextField student1Field;
    private TextField student2Field;
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
        table.add(humanButton).colspan(2).row();
        TextButton computerButton = new TextButton("Human vs. Computer", skin);
        table.add(computerButton).colspan(2).row();
        
        table.add(new Label("Student 1:", skin));
        String blankName = "          ";
        student1Field = new TextField(blankName, skin);
        table.add(student1Field).fillX().row();
        table.add(new Label("Student 2:", skin));
        student2Field = new TextField(blankName, skin);
        table.add(student2Field).fillX().row();
        skillLabel = new Label("", skin);
        table.add(skillLabel).colspan(2).row();
        boolean isVertical = false;
        vocabularySize = new Slider(1, 65000, 100, isVertical, skin);
        table.add(vocabularySize).colspan(2).fillX();
        
        computerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                savePreferences();
                boolean isComputerOpponent = true;
                app.startUltraghost(isComputerOpponent);
            }
        });
        
        humanButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                savePreferences();
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
        VograbularyPreferences preferences = app.getPreferences();
        student1Field.setText(preferences.getStudent1Name());
        student2Field.setText(preferences.getStudent2Name());
        int currentVocabularySize = 
                preferences.getComputerStudentVocabularySize();
        vocabularySize.setValue(currentVocabularySize);
        setVocabularySize(currentVocabularySize);
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
    }

    private void savePreferences() {
        VograbularyPreferences preferences = app.getPreferences();
        preferences.setStudent1Name(student1Field.getText());
        preferences.setStudent2Name(student2Field.getText());
        preferences.setComputerStudentVocabularySize(
                (int)vocabularySize.getValue());
    }
    //resumeJesting
}
