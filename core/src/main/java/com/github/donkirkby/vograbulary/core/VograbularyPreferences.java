package com.github.donkirkby.vograbulary.core;

import java.util.Arrays;
import java.util.List;

import playn.core.Storage;

public class VograbularyPreferences {
    //stopJesting
    private static final String DEFAULT_STUDENT_NAMES = "Alice|Bob";
    private static final String DEFAULT_STUDENT_SELECTIONS = "YY";
    private static final int DEFAULT_VOCABULARY_SIZE = 5000;
    private static final int DEFAULT_ULTRAGHOST_MINIMUM_WORD_LENGTH = 4;
    //resumeJesting

    private enum Fields { 
        STUDENT_NAMES,
        STUDENT_SELECTIONS,
        COMPUTER_STUDENT_VOCABULARY_SIZE,
        ULTRAGHOST_MINIMUM_WORD_LENGTH
    };
    
    private Storage preferences;
    
    public VograbularyPreferences(Storage preferences) {
        this.preferences = preferences;
    }

    public List<String> getStudentNames() {
        String studentNames = getString(
                Fields.STUDENT_NAMES.name(),
                DEFAULT_STUDENT_NAMES);
        List<String> nameList = Arrays.asList(studentNames.split("\\|"));
        return nameList;
    }
    
    public void setStudentNames(String... studentNames) {
        StringBuilder builder = new StringBuilder();
        for (String name : studentNames) {
            if (builder.length() > 0) {
                builder.append("|");
            }
            builder.append(name.replace('|', '/'));
        }
        preferences.setItem(Fields.STUDENT_NAMES.name(), builder.toString());
    }
    
    public String getStudentSelections() {
        return getString(
                Fields.STUDENT_SELECTIONS.name(),
                DEFAULT_STUDENT_SELECTIONS);
    }
    public void setStudentSelections(String studentSelections) {
        preferences.setItem(
                Fields.STUDENT_SELECTIONS.name(), 
                studentSelections);
    }
    
    public int getComputerStudentVocabularySize() {
        return getInteger(
                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
                DEFAULT_VOCABULARY_SIZE);
    }
    public void setComputerStudentVocabularySize(int vocabularySize) {
        preferences.setItem(
                Fields.COMPUTER_STUDENT_VOCABULARY_SIZE.name(), 
                Integer.toString(vocabularySize));
    }
    
    public int getUltraghostMinimumWordLength() {
        return getInteger(
                Fields.ULTRAGHOST_MINIMUM_WORD_LENGTH.name(), 
                DEFAULT_ULTRAGHOST_MINIMUM_WORD_LENGTH);
    }
    public void setUltraghostMinimumWordLength(int wordLength) {
        preferences.setItem(
                Fields.ULTRAGHOST_MINIMUM_WORD_LENGTH.name(), 
                Integer.toString(wordLength));
    }
    
    private String getString(String name, String defaultValue) {
        String value = preferences.getItem(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }
    
    private int getInteger(String name, int defaultValue) {
        String text = getString(name, null);
        return text == null ? defaultValue : Integer.parseInt(text);
    }
}
