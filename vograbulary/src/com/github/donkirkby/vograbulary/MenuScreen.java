package com.github.donkirkby.vograbulary;

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

public class MenuScreen extends VograbularyScreen {
    //stopJesting
    private TextField student1Field;
    private TextField student2Field;
    private Label wordLengthLabel;
    private Slider wordLength;
    private Label skillLabel;
    private Slider vocabularySize;

    public MenuScreen(final VograbularyApp app) {
        super(app);
        Stage stage = getStage();
        
        Skin skin = app.getSkin();
        Table table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);
        table.align(Align.top);
        TextButton humanButton = new TextButton("Human vs. Human", skin);
        table.add(humanButton).colspan(2).row();
        TextButton computerButton = new TextButton("Human vs. Computer", skin);
        table.add(computerButton).colspan(2).row();
        TextButton russianDollsButton = new TextButton("Russian Dolls", skin);
        table.add(russianDollsButton).colspan(2).row();
        
        table.add("Student 1:");
        String blankName = "          ";
        student1Field = new TextField(blankName, skin);
        table.add(student1Field).fillX().row();
        table.add("Student 2:");
        student2Field = new TextField(blankName, skin);
        table.add(student2Field).fillX().row();
        wordLengthLabel = new Label("", skin);
        table.add(wordLengthLabel).colspan(2).row();
        boolean isVertical = false;
        wordLength = new Slider(4, 9, 1, isVertical, skin);
        table.add(wordLength).colspan(2).fillX().row();
        skillLabel = new Label("", skin);
        table.add(skillLabel).colspan(2).row();
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
        
        russianDollsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                savePreferences();
                app.startRussianDolls();
            }
        });
        
        vocabularySize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setVocabularySize((int)vocabularySize.getValue());
            }
        });
        
        wordLength.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setWordLength((int)wordLength.getValue());
            }
        });
    }

    @Override
    public void show() {
        VograbularyPreferences preferences = getApp().getPreferences();
        student1Field.setText(preferences.getStudent1Name());
        student2Field.setText(preferences.getStudent2Name());
        int currentWordLength =
                preferences.getUltraghostMinimumWordLength();
        wordLength.setValue(currentWordLength);
        setWordLength(currentWordLength);
        int currentVocabularySize = 
                preferences.getComputerStudentVocabularySize();
        vocabularySize.setValue(currentVocabularySize);
        setVocabularySize(currentVocabularySize);
        super.show();
    }

    private void setVocabularySize(int skill) {
        skillLabel.setText("Skill " + skill);
    }

    private void setWordLength(int wordLength) {
        wordLengthLabel.setText("Minimum " + wordLength + " letters");
    }

    private void savePreferences() {
        VograbularyPreferences preferences = getApp().getPreferences();
        preferences.setStudent1Name(student1Field.getText());
        preferences.setStudent2Name(student2Field.getText());
        preferences.setComputerStudentVocabularySize(
                (int)vocabularySize.getValue());
        preferences.setUltraghostMinimumWordLength(
                (int)wordLength.getValue());
    }
    //resumeJesting
}
