package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Preferences;

public class VograbularyPreferences {
    //stopJesting
    private static final String DEFAULT_STUDENT1_NAME = "Alice";
    private static final String DEFAULT_STUDENT2_NAME = "Bob";
    private static final int DEFAULT_VOCABULARY_SIZE = 5000;
    private static final int DEFAULT_ULTRAGHOST_MINIMUM_WORD_LENGTH = 4;
    //resumeJesting

    private enum Fields { 
        STUDENT1_NAME, 
        STUDENT2_NAME, 
        COMPUTER_STUDENT_VOCABULARY_SIZE,
        ULTRAGHOST_MINIMUM_WORD_LENGTH
    };
    
    private Preferences preferences;
    
    public VograbularyPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getStudent1Name() {
        return preferences.getString(Fields.STUDENT1_NAME.name(), DEFAULT_STUDENT1_NAME);
    }
    public void setStudent1Name(String student1Name) {
        preferences.putString(Fields.STUDENT1_NAME.name(), student1Name);
        preferences.flush();
    }
    
    public String getStudent2Name() {
        return preferences.getString(Fields.STUDENT2_NAME.name(), DEFAULT_STUDENT2_NAME);
    }
    public void setStudent2Name(String student2Name) {
        preferences.putString(Fields.STUDENT2_NAME.name(), student2Name);
        preferences.flush();
    }
    
    public int getComputerStudentVocabularySize() {
        return preferences.getInteger(
                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
                DEFAULT_VOCABULARY_SIZE);
    }
    public void setComputerStudentVocabularySize(int vocabularySize) {
        preferences.putInteger(
                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
                vocabularySize);
        preferences.flush();
    }
    
    public int getUltraghostMinimumWordLength() {
        return preferences.getInteger(
                Fields.ULTRAGHOST_MINIMUM_WORD_LENGTH.name(), 
                DEFAULT_ULTRAGHOST_MINIMUM_WORD_LENGTH);
    }
    public void setUltraghostMinimumWordLength(int wordLength) {
        preferences.putInteger(
                Fields.ULTRAGHOST_MINIMUM_WORD_LENGTH.name(), 
                wordLength);
        preferences.flush();
    }
}
