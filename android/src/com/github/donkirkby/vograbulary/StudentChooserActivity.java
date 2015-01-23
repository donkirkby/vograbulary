package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StudentChooserActivity extends VograbularyActivity {
    private ListView studentList;
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_chooser);
        
        studentList = (ListView)findViewById(R.id.studentList);
        
        AndroidPreferences preferences = new AndroidPreferences(this);
        names = preferences.getStudentNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                names);
        studentList.setAdapter(adapter);
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
