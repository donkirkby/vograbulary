package com.github.donkirkby.vograbulary.ultraghost;

//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.donkirkby.vograbulary.VograbularyApp;
import com.github.donkirkby.vograbulary.VograbularyPreferences;
import com.github.donkirkby.vograbulary.VograbularyScreen;

public class UltraghostScreen extends VograbularyScreen {
//    //stopJesting
//    private Controller ultraghostController = 
//            new Controller();
//    private boolean isComputerOpponent;
//    private boolean isHyperghost;
//    private View view;
//
//    public UltraghostScreen(final VograbularyApp app) {
//        super(app);
//        Stage stage = getStage();
//        
//        Table table = new Table();
//        table.setFillParent(true);
//        stage.addActor(table);
//        
//        view = new View();
//        view.create(table, app, ultraghostController);
//        WordList wordList = new WordList();
//        wordList.read(
//                Gdx.files.internal("data/wordlist.txt").reader());
//        ultraghostController.setWordList(wordList);
//    }
//    
//    public void setComputerOpponent(boolean isComputerOpponent) {
//        this.isComputerOpponent = isComputerOpponent;
//    }
//    
//    public void setHyperghost(boolean isHyperghost) {
//        this.isHyperghost = isHyperghost;
//    }
//
//    @Override
//    public void show() {
//        view.clear();
//        VograbularyPreferences preferences = getApp().getPreferences();
//        ultraghostController.clearStudents();
//        if (isComputerOpponent) {
//            ComputerStudent computerStudent = new ComputerStudent(preferences);
//            computerStudent.setSearchBatchSize(30);
//            computerStudent.setMaxSearchBatchCount(1000); // 10s
//            ultraghostController.addStudent(computerStudent);
//            ultraghostController.addStudent(new Student("You"));
//        }
//        else {
//            String studentSelections = preferences.getStudentSelections();
//            int i = 0;
//            for (String studentName : preferences.getStudentNames()) {
//                if (studentSelections.charAt(i++) == 'Y') {
//                    ultraghostController.addStudent(new Student(studentName));
//                }
//            }
//        }
//        Match match = ultraghostController.getMatch();
//        match.setMinimumWordLength(
//                preferences.getUltraghostMinimumWordLength());
//        match.setHyperghost(isHyperghost);
//
//        super.show();
//    }
//    //resumeJesting
}
