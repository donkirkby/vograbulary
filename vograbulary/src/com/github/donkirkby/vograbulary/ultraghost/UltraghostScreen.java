package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.donkirkby.vograbulary.VograbularyApp;
import com.github.donkirkby.vograbulary.VograbularyPreferences;
import com.github.donkirkby.vograbulary.VograbularyScreen;

public class UltraghostScreen extends VograbularyScreen {
    //stopJesting
    private Controller ultraghostController = 
            new Controller();
    private boolean isComputerOpponent;
    private View view;

    public UltraghostScreen(final VograbularyApp app) {
        super(app);
        Stage stage = getStage();
        
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        view = new View();
        view.create(table, app, ultraghostController);
        WordList wordList = new WordList();
        wordList.read(
                Gdx.files.internal("data/wordlist.txt").reader());
        ultraghostController.setWordList(wordList);
    }
    
    public void setComputerOpponent(boolean isComputerOpponent) {
        this.isComputerOpponent = isComputerOpponent;
    }

    @Override
    public void show() {
        view.clear();
        VograbularyPreferences preferences = getApp().getPreferences();
        ComputerStudent computerStudent = new ComputerStudent(preferences);
        computerStudent.setSearchBatchSize(30);
        computerStudent.setMaxSearchBatchCount(1000); // 10s
        String student1Name = 
                isComputerOpponent 
                ? "You"
                : preferences.getStudent1Name();
        ultraghostController.clearStudents();
        ultraghostController.addStudent(new Student(student1Name));
        ultraghostController.addStudent(
                isComputerOpponent
                ? computerStudent
                : new Student(preferences.getStudent2Name()));
        ultraghostController.getWordList().setMinimumWordLength(
                preferences.getUltraghostMinimumWordLength());

        super.show();
    }
    //resumeJesting
}
