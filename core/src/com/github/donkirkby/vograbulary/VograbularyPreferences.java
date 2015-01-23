package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class VograbularyPreferences {
//    //stopJesting
//    private static final String DEFAULT_STUDENT_NAMES = "Alice|Bob";
//    private static final String DEFAULT_STUDENT_SELECTIONS = "YY";
//    private static final int DEFAULT_VOCABULARY_SIZE = 5000;
//    private static final int DEFAULT_ULTRAGHOST_MINIMUM_WORD_LENGTH = 4;
//    //resumeJesting
//
    private enum Fields { 
        STUDENT_NAMES,
        STUDENT_SELECTIONS,
        COMPUTER_STUDENT_VOCABULARY_SIZE,
        ULTRAGHOST_MINIMUM_WORD_LENGTH
    };
    
    private static final Set<String> DEFAULT_STUDENT_NAMES = 
            new HashSet<>(Arrays.asList("Student1", "Student2"));

    private boolean isHyperghost;
    private boolean isComputerOpponent;
    
    public boolean isComputerOpponent() {
        return isComputerOpponent;
    }
    public void setComputerOpponent(boolean isComputerOpponent) {
        this.isComputerOpponent = isComputerOpponent;
    }
    public boolean isHyperghost() {
        return isHyperghost;
    }
    public void setHyperghost(boolean isHyperghost) {
        this.isHyperghost = isHyperghost;
    }
    
    
    protected abstract void putStringSet(String key, Set<String> values);
    protected abstract Set<String> getStringSet(
            String key,
            Set<String> defValues);

    /**
     * Store any changes submitted through the put methods.
     * 
     */
    public void apply() {
    }
//    
//    private Preferences preferences;
//    
//    public VograbularyPreferences(Preferences preferences) {
//        this.preferences = preferences;
//    }
//
    public List<String> getStudentNames() {
        Set<String> names = getStringSet(
                Fields.STUDENT_NAMES.name(),
                DEFAULT_STUDENT_NAMES);
        ArrayList<String> nameList = new ArrayList<>(names);
        Collections.sort(nameList);
        return nameList;
    }

    public void setStudentNames(List<String> studentNames) {
        Set<String> nameSet = new HashSet<>(studentNames);
        putStringSet(Fields.STUDENT_NAMES.name(), nameSet);
    }
    
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
        return 10000;
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
