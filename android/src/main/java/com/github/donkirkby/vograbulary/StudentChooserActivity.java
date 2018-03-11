package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class StudentChooserActivity extends VograbularyActivity {
    private static final int MIN_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 10;
    private static final int MAX_VOCABULARY_SIZE = 65000;
    private static final int VOCABULARY_SIZE_STEP = 500;
    private ListView studentList;
    private List<String> names;
    private TextView wordLengthLabel;
    private SeekBar wordLengthSlider;
    private TextView vocabularySizeLabel;
    private SeekBar vocabularySizeSlider;
    private AndroidPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_chooser);
        
        studentList = (ListView)findViewById(R.id.studentList);
        wordLengthLabel = (TextView)findViewById(R.id.wordLengthLabel);
        wordLengthSlider = (SeekBar)findViewById(R.id.wordLengthSlider);
        vocabularySizeLabel = (TextView)findViewById(R.id.vocabularySizeLabel);
        vocabularySizeSlider = (SeekBar)findViewById(R.id.vocabularySizeSlider);
        
        preferences = new AndroidPreferences(this);
        names = preferences.getStudentNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                names);
        studentList.setAdapter(adapter);

        wordLengthSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onProgressChanged(
                    SeekBar seekBar,
                    int progress,
                    boolean fromUser) {
                if (fromUser) {
                    preferences.setUltraghostMinimumWordLength(
                            progress + MIN_WORD_LENGTH);
                }
                wordLengthLabel.setText(getString(R.string.choose_word_length).replace(
                        "0",
                        Integer.toString(preferences.getUltraghostMinimumWordLength())));
            }
        });
        vocabularySizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onProgressChanged(
                    SeekBar seekBar,
                    int progress,
                    boolean fromUser) {
                if (fromUser) {
                    preferences.setComputerStudentVocabularySize(
                            (progress + 1) * VOCABULARY_SIZE_STEP);
                }
                vocabularySizeLabel.setText(getString(R.string.choose_vocabulary_size).replace(
                        "0",
                        Integer.toString(preferences.getComputerStudentVocabularySize())));
            }
        });
        wordLengthSlider.setMax(MAX_WORD_LENGTH - MIN_WORD_LENGTH);
        wordLengthSlider.setProgress(
                preferences.getUltraghostMinimumWordLength() - MIN_WORD_LENGTH);
        vocabularySizeSlider.setMax(MAX_VOCABULARY_SIZE/VOCABULARY_SIZE_STEP-1);
        vocabularySizeSlider.setProgress(
                preferences.getComputerStudentVocabularySize()/VOCABULARY_SIZE_STEP-1);
    }

    public void start(View view) {
        List<String> chosenNames = new ArrayList<String>();
        for (int i = 0; i < studentList.getCount(); i++) {
            if (studentList.isItemChecked(i)) {
                chosenNames.add(names.get(i));
            }
        }
        Intent oldIntent = getIntent();
        Intent intent = new Intent(this, UltraghostActivity.class);
        intent.putExtra(
                UltraghostActivity.INTENT_EXTRA_IS_HYPERGHOST,
                oldIntent.getBooleanExtra(
                        UltraghostActivity.INTENT_EXTRA_IS_HYPERGHOST,
                        false));
        intent.putExtra(
                UltraghostActivity.INTENT_EXTRA_STUDENT_NAMES,
                chosenNames.toArray(new String[chosenNames.size()]));
        startActivity(intent);
    }
}
