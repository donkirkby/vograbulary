package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.russian.Controller;
import com.github.donkirkby.vograbulary.russian.Puzzle;
import com.github.donkirkby.vograbulary.russian.PuzzleDisplay;
import com.github.donkirkby.vograbulary.russian.RussianDollsScreen;
import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class MainActivity extends Activity implements RussianDollsScreen {
    private TextView puzzleText;
    private TextView targetWord1;
    private TextView targetWord2;
    private Button nextButton;
    private int[] location = new int[2];
    private Controller controller = new Controller();
    private PuzzleDisplay puzzleDisplay = new PuzzleDisplay();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewGroup insertLayout = (ViewGroup)findViewById(R.id.insertLayout);
        puzzleText = (TextView)findViewById(R.id.clue);
        targetWord1 = (TextView)findViewById(R.id.targetWord1);
        targetWord2 = (TextView)findViewById(R.id.targetWord2);
        nextButton = (Button)findViewById(R.id.nextButton);
        final ImageView insertButton = (ImageView)findViewById(R.id.insertImage);
        
        List<String> puzzleSource;
        List<String> wordSource;
        try {
            puzzleSource = loadTextAsset("russianDolls.txt");
            wordSource = loadTextAsset("wordlist.txt");
        } catch (IOException e) {
            puzzleSource = Arrays.asList(
                    "Failed to open file. " + e.getMessage());
            wordSource = new ArrayList<String>();
        }
        WordList wordList = new WordList();
        wordList.read(wordSource);
        controller.setScreen(this);
        controller.setWordList(wordList);
        controller.loadPuzzles(puzzleSource);
        
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
                    targetWord1.getLocationOnScreen(location);
                    int word1Left = location[0];
                    targetWord2.getLocationOnScreen(location);
                    puzzleDisplay.setTargetPositions(
                            word1Left,
                            targetWord1.getWidth(),
                            location[0],
                            targetWord2.getWidth());
                    break;
                case MotionEvent.ACTION_MOVE:
                    layoutParams.leftMargin = eventX - _xDelta;
                    layoutParams.rightMargin = -250;
                    view.setLayoutParams(layoutParams);
                    
                    insertButton.getLocationOnScreen(location);
                    int insertX = location[0] +
                            insertButton.getWidth()*24/64;
                    puzzleDisplay.calculateInsertion(insertX);
                    break;
                }
                insertLayout.invalidate();
                return true;
            }
        });
    }

    private ArrayList<String> loadTextAsset(String assetName)
            throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                getAssets().open(assetName)));
        try {
            String line;
            while (null != (line = reader.readLine())) {
                lines.add(line);
            }
        } finally {
            reader.close();
        }
        return lines;
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
        Puzzle puzzle = getPuzzle();
        if (puzzle.isSolved()) {
            controller.next();
        }
        else {
            controller.solve();
            if (puzzle.isSolved()) {
                nextButton.setText("Next");
                targetWord1.setText(puzzle.getCombination());
                targetWord2.setText("");
            }
        }
    }
    
    public void previous(View view) {
        controller.back();
    }
    
    @Override
    public Puzzle getPuzzle() {
        return puzzleDisplay.getPuzzle();
    }

    @Override
    public void setPuzzle(Puzzle puzzle) {
        puzzleText.setText(puzzle.getClue());
        targetWord1.setText(puzzle.getTarget(0));
        targetWord2.setText(puzzle.getTarget(1));
        nextButton.setText("Solve");
        puzzleDisplay.setPuzzle(puzzle);
    }
}
