package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.russian.Puzzle;

public class MainActivity extends Activity {
    private TextView puzzleText;
    private TextView targetWord1;
    private TextView targetWord2;
    private int puzzleIndex;
    private ArrayList<String> puzzleSource = new ArrayList<String>();
    private int[] insertLocation = new int[2];
    private int[] targetLocation = new int[2];
    private int wordIndex = -1;
    private int charIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewGroup insertLayout = (ViewGroup)findViewById(R.id.insertLayout);
        puzzleText = (TextView)findViewById(R.id.clue);
        targetWord1 = (TextView)findViewById(R.id.targetWord1);
        targetWord2 = (TextView)findViewById(R.id.targetWord2);
        final ImageView insertButton = (ImageView)findViewById(R.id.insertImage);
        
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
        
        insertButton.setOnTouchListener(new View.OnTouchListener() {
            private int _xDelta;
            
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int eventX = (int) event.getRawX();
                RelativeLayout.LayoutParams layoutParams = 
                        (RelativeLayout.LayoutParams) view.getLayoutParams();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    _xDelta = eventX - layoutParams.leftMargin;
                    break;
                case MotionEvent.ACTION_MOVE:
                    layoutParams.leftMargin = eventX - _xDelta;
                    layoutParams.rightMargin = -250;
                    view.setLayoutParams(layoutParams);
                    
                    insertButton.getLocationOnScreen(insertLocation);
                    int insertX = insertLocation[0] +
                            insertButton.getWidth()*24/64;
                    wordIndex = -1;
                    calculateInsertionPoint(insertX, targetWord1);
                    calculateInsertionPoint(insertX, targetWord2);
                    puzzleText.setText(wordIndex + " : " + charIndex);
                    break;
                }
                insertLayout.invalidate();
                return true;
            }
        });
    }
    
    private void calculateInsertionPoint(int insertX, TextView targetWord) {
        if (wordIndex >= 0) {
            return; // already found insertion point on other word
        }
        targetWord.getLocationOnScreen(targetLocation);
        if (insertX < targetLocation[0] ||
                targetLocation[0] + targetWord.getWidth() < insertX) {
            return;
        }
        wordIndex = targetWord == targetWord1 ? 0 : 1;
        int wordPixel = insertX - targetLocation[0];
        int wordLength = targetWord.getText().length();
        int wordWidth = targetWord.getWidth();
        charIndex = (wordPixel * wordLength + wordWidth/2) / wordWidth;
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
