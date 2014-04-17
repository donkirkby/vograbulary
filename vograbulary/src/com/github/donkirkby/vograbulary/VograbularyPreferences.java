package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Preferences;

public class VograbularyPreferences {
    private enum Fields { 
        STUDENT1_NAME, 
        STUDENT2_NAME, 
        COMPUTER_STUDENT_VOCABULARY_SIZE 
    };
    
    private Preferences preferences;
    
    public VograbularyPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getStudent1Name() {
        return preferences.getString(Fields.STUDENT1_NAME.name(), "Alice");
    }
    public void setStudent1Name(String student1Name) {
        preferences.putString(Fields.STUDENT1_NAME.name(), student1Name);
        preferences.flush();
    }
    
    public String getStudent2Name() {
        return preferences.getString(Fields.STUDENT2_NAME.name(), "Bob");
    }
    public void setStudent2Name(String student2Name) {
        preferences.putString(Fields.STUDENT2_NAME.name(), student2Name);
        preferences.flush();
    }
    
    public int getComputerStudentVocabularySize() {
        return preferences.getInteger(
                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
                5000);
    }
    public void setComputerStudentVocabularySize(int vocabularySize) {
        preferences.putInteger(
                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
                vocabularySize);
        preferences.flush();
    }
}
