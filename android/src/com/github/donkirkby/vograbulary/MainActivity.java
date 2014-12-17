package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.github.donkirkby.vograbulary.russian.Puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView puzzleText;
    private TextView targetWord1;
    private TextView targetWord2;
    private int puzzleIndex;
    private ArrayList<String> puzzleSource = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        puzzleText = (TextView)findViewById(R.id.clue);
        targetWord1 = (TextView)findViewById(R.id.targetWord1);
        targetWord2 = (TextView)findViewById(R.id.targetWord2);
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getAssets().open("russianDolls.txt")));
            try {
                String line;
                while (null != (line = reader.readLine())) {
                    puzzleSource.add(line);
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            puzzleSource.add("Failed to open file. " + e.getMessage());
        }
        displayPuzzle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void next(View view) {
        puzzleIndex = Math.min(puzzleIndex+1, puzzleSource.size()-1);
        displayPuzzle();
    }
    
    public void previous(View view) {
        puzzleIndex = Math.max(puzzleIndex-1, 0);
        displayPuzzle();
    }
    
    private void displayPuzzle() {
        Puzzle puzzle = new Puzzle(puzzleSource.get(puzzleIndex));
        puzzleText.setText(puzzle.getClue());
        targetWord1.setText(puzzle.getTarget(0));
        targetWord2.setText(puzzle.getTarget(1));
    }
}
