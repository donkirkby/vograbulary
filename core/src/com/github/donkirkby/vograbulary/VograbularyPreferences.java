package com.github.donkirkby.vograbulary;

import java.util.Arrays;
import java.util.List;

//import com.badlogic.gdx.Preferences;

public class VograbularyPreferences {
//    //stopJesting
//    private static final String DEFAULT_STUDENT_NAMES = "Alice|Bob";
//    private static final String DEFAULT_STUDENT_SELECTIONS = "YY";
//    private static final int DEFAULT_VOCABULARY_SIZE = 5000;
//    private static final int DEFAULT_ULTRAGHOST_MINIMUM_WORD_LENGTH = 4;
//    //resumeJesting
//
//    private enum Fields { 
//        STUDENT_NAMES,
//        STUDENT_SELECTIONS,
//        COMPUTER_STUDENT_VOCABULARY_SIZE,
//        ULTRAGHOST_MINIMUM_WORD_LENGTH
//    };
//    
//    private Preferences preferences;
//    
//    public VograbularyPreferences(Preferences preferences) {
//        this.preferences = preferences;
//    }
//
//    public List<String> getStudentNames() {
//        String studentNames = preferences.getString(
//                Fields.STUDENT_NAMES.name(), 
//                DEFAULT_STUDENT_NAMES);
//        List<String> nameList = Arrays.asList(studentNames.split("\\|"));
//        return nameList;
//    }
//    
//    public void setStudentNames(String... studentNames) {
//        StringBuilder builder = new StringBuilder();
//        for (String name : studentNames) {
//            if (builder.length() > 0) {
//                builder.append("|");
//            }
//            builder.append(name.replace('|', '/'));
//        }
//        preferences.putString(Fields.STUDENT_NAMES.name(), builder.toString());
//        preferences.flush();
//    }
//    
//    public String getStudentSelections() {
//        return preferences.getString(
//                Fields.STUDENT_SELECTIONS.name(),
//                DEFAULT_STUDENT_SELECTIONS);
//    }
//    public void setStudentSelections(String studentSelections) {
//        preferences.putString(
//                Fields.STUDENT_SELECTIONS.name(), 
//                studentSelections);
//        preferences.flush();
//    }
//    
    public int getComputerStudentVocabularySize() {
        return 0;
//        return preferences.getInteger(
//                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
//                DEFAULT_VOCABULARY_SIZE);
    }
//    public void setComputerStudentVocabularySize(int vocabularySize) {
//        preferences.putInteger(
//                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
//                vocabularySize);
//        preferences.flush();
//    }
//    
//    public int getUltraghostMinimumWordLength() {
//        return preferences.getInteger(
//                Fields.ULTRAGHOST_MINIMUM_WORD_LENGTH.name(), 
//                DEFAULT_ULTRAGHOST_MINIMUM_WORD_LENGTH);
//    }
//    public void setUltraghostMinimumWordLength(int wordLength) {
//        preferences.putInteger(
//                Fields.ULTRAGHOST_MINIMUM_WORD_LENGTH.name(), 
//                wordLength);
//        preferences.flush();
//    }
}
