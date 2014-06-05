package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.BaseTableLayout;

public class MenuScreen extends VograbularyScreen {
    //stopJesting
    private Table studentTable;
    private List<CheckBox> studentCheckBoxes;
    private List<TextField> studentFields;
    private Label wordLengthLabel;
    private Slider wordLength;
    private Label skillLabel;
    private Slider vocabularySize;
    private final float PADDING = 5;

    public MenuScreen(final VograbularyApp app) {
        super(app);
        Stage stage = getStage();
        
        Skin skin = app.getSkin();
        Table mainTable = new Table(skin);
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        mainTable.align(Align.top);
        studentTable = new Table(skin);
        mainTable.add(studentTable).align(BaseTableLayout.TOP).fillX();
        studentFields = new ArrayList<TextField>();
        studentCheckBoxes = new ArrayList<CheckBox>();
        
        Table challengeTable = new Table(skin);
        mainTable.add(challengeTable).align(BaseTableLayout.TOP);
        TextButton ultraghostButton = new TextButton("Ultraghost", skin);
        challengeTable.add(ultraghostButton).colspan(2).pad(PADDING).row();
        TextButton russianDollsButton = new TextButton("Russian Dolls", skin);
        challengeTable.add(russianDollsButton).colspan(2).pad(PADDING).row();
        
        wordLengthLabel = new Label("", skin);
        challengeTable.add(wordLengthLabel).colspan(2).row();
        boolean isVertical = false;
        wordLength = new Slider(4, 9, 1, isVertical, skin);
        challengeTable.add(wordLength).colspan(2).fillX().row();
        skillLabel = new Label("", skin);
        challengeTable.add(skillLabel).colspan(2).row();
        vocabularySize = new Slider(1, 65000, 100, isVertical, skin);
        challengeTable.add(vocabularySize).colspan(2).fillX();
        
        ultraghostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                savePreferences();
                boolean isComputerOpponent = 
                        studentCheckBoxes.get(0).isChecked();
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
        Skin skin = getApp().getSkin();
        VograbularyPreferences preferences = getApp().getPreferences();
        studentTable.clear();
        studentFields.clear();
        studentCheckBoxes.clear();
        addStudentCheckBox(false, skin);
        studentTable.add("Computer").pad(PADDING).row();

        String studentSelections = preferences.getStudentSelections();
        int i = 0;
        for (String name : preferences.getStudentNames()) {
            boolean isSelected = studentSelections.charAt(i++) == 'Y';
            addStudentField(name, skin, isSelected);
        }
        addStudentField("", skin, true);
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

    private void addStudentField(
            final String name, 
            final Skin skin, 
            final boolean isSelected) {
        final CheckBox checkBox = addStudentCheckBox(isSelected, skin);
        TextField studentField = new TextField(name, skin);
        studentField.setMessageText("New...");
        studentTable.add(studentField).fillX().pad(PADDING).row();
        studentFields.add(studentField);
        checkBox.setVisible(studentField.getText().length() > 0);
        
        studentField.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if (key == 0 || key == '\t') {
                    // Ignore keys like shift, ctrl, and tab.
                    return;
                }
                if (textField == studentFields.get(studentFields.size() - 1)) {
                    addStudentField("", skin, true);
                }
                checkBox.setVisible(textField.getText().length() > 0);
            }
        });
    }

    private CheckBox addStudentCheckBox(boolean isSelected, Skin skin) {
        CheckBox checkBox = new CheckBox("", skin);
        checkBox.setChecked(isSelected);
        studentCheckBoxes.add(checkBox);
        studentTable.add(checkBox).pad(PADDING);
        return checkBox;
    }

    private void setVocabularySize(int skill) {
        skillLabel.setText("Skill " + skill);
    }

    private void setWordLength(int wordLength) {
        wordLengthLabel.setText("Min " + wordLength + "");
    }

    private void savePreferences() {
        VograbularyPreferences preferences = getApp().getPreferences();
        StringBuilder selectionsBuilder = new StringBuilder();
        String[] studentNames = new String[studentFields.size() - 1];
        for (int i = 0; i < studentFields.size() - 1; i++) {
            studentNames[i] = studentFields.get(i).getText();
            selectionsBuilder.append(
                    studentCheckBoxes.get(i + 1).isChecked()
                    ? "Y"
                    : "N");
        }
        preferences.setStudentNames(studentNames);
        preferences.setStudentSelections(selectionsBuilder.toString());
        preferences.setComputerStudentVocabularySize(
                (int)vocabularySize.getValue());
        preferences.setUltraghostMinimumWordLength(
                (int)wordLength.getValue());
    }
    //resumeJesting
}
