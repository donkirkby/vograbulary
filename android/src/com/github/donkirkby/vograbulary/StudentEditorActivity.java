package com.github.donkirkby.vograbulary;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class StudentEditorActivity
extends VograbularyActivity implements AdapterView.OnItemClickListener {
    private static final int NO_SELECTION = -1;
    private int selectedIndex = NO_SELECTION;
    private ListView studentList;
    private EditText studentName;
    private List<String> studentNames;
    private AndroidPreferences preferences;
    private ArrayAdapter<String> adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_editor);
        
        studentList = (ListView)findViewById(R.id.studentList);
        studentName = (EditText)findViewById(R.id.studentName);
        
        preferences = new AndroidPreferences(this);
        studentNames = preferences.getStudentNames();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                studentNames);
        studentList.setAdapter(adapter);
        studentList.setOnItemClickListener(this);
        studentList.setItemChecked(0, true);
        onItemClick(null, null, 0, 0);
    }

    @Override
    public void onItemClick(
            AdapterView<?> parent,
            View view,
            int position,
            long id) {
        changeSelection(position);
    }

    private void changeSelection(int position) {
        if (selectedIndex != NO_SELECTION) {
            String oldName = studentNames.get(selectedIndex);
            String newName = studentName.getText().toString();
            if ( ! oldName.equals(newName)) {
                adapter.remove(oldName);
                adapter.insert(newName, selectedIndex);
                saveStudentNames();
            }
        }
        if (position != NO_SELECTION) {
            studentName.setText(studentNames.get(position));
        }
        selectedIndex = position;
    }

    private void saveStudentNames() {
        preferences.setStudentNames(studentNames);
        preferences.apply();
    }
    
    public void add(View view) {
        changeSelection(NO_SELECTION);
        adapter.insert("", 0);
        changeSelection(0);
        studentList.setItemChecked(0, true);
    }
    
    public void delete(View view) {
        int oldIndex = selectedIndex;
        changeSelection(NO_SELECTION);
        adapter.remove(studentNames.get(oldIndex));
        changeSelection(Math.min(oldIndex, studentNames.size() - 1));
        saveStudentNames();
    }
}
