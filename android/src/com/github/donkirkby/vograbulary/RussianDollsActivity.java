package com.github.donkirkby.vograbulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.russian.Controller;
import com.github.donkirkby.vograbulary.russian.Puzzle;
import com.github.donkirkby.vograbulary.russian.PuzzleDisplay;
import com.github.donkirkby.vograbulary.russian.RussianDollsScreen;
import com.github.donkirkby.vograbulary.russian.TargetDisplay;
import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class RussianDollsActivity
extends VograbularyActivity implements RussianDollsScreen {
    private TextView puzzleText;
    private TargetDisplay targetDisplay1;
    private TargetDisplay targetDisplay2;
    private TextView scoreDisplay;
    private Button nextButton;
    private int[] location = new int[2];
    private Controller controller = new Controller();
    private PuzzleDisplay puzzleDisplay = new PuzzleDisplay();
    private AndroidScheduler scheduler = new AndroidScheduler();
    private Puzzle puzzle;
    private ImageView insertButton;
    private ViewGroup russianDollsLayout;
    private boolean isLaidOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_russian_dolls);
        russianDollsLayout = (ViewGroup)findViewById(R.id.russianDollsLayout);
        puzzleText = (TextView)findViewById(R.id.clue);
        nextButton = (Button)findViewById(R.id.nextButton);
        scoreDisplay = (TextView)findViewById(R.id.scoreDisplay);
        insertButton = (ImageView)findViewById(R.id.insertImage);
        final ImageView dragButton1 = (ImageView)findViewById(R.id.dragImage1);
        final ImageView dragButton2 = (ImageView)findViewById(R.id.dragImage2);
        
        insertButton.setOnTouchListener(new InsertTouchListener());
        targetDisplay1 = new AndroidTargetDisplay(
                dragButton1,
                insertButton,
                russianDollsLayout);
        targetDisplay2 = new AndroidTargetDisplay(
                dragButton2,
                insertButton,
                russianDollsLayout);
        targetDisplay1.setOther(targetDisplay2);
        
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
        
        targetDisplay1.setDragVisible(false);
        targetDisplay2.setDragVisible(false);
        final int periodMilliseconds = 100;
        scheduler.scheduleRepeating(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String display = "Score: " + controller.adjustScore(
                                0.001f * periodMilliseconds) + "\nTotal: " +
                                puzzle.getTotalScoreDisplay();
                        scoreDisplay.setText(display);
                        boolean shouldHide =
                                ! targetDisplay1.isDragVisible() &&
                                ! targetDisplay2.isDragVisible() &&
                                puzzle.getScore().intValue() < 50;
                        if (shouldHide) {
                            targetDisplay1.setDragVisible(true);
                            targetDisplay2.setDragVisible(true);
                        }
                    }
                });
            }
        },
        periodMilliseconds);
        russianDollsLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ( ! isLaidOut) {
                    targetDisplay1.setScreenWidth(russianDollsLayout.getWidth());
                    targetDisplay1.layout();
                    isLaidOut = true;
                }
            }
        });
    }
    
    private class InsertTouchListener implements View.OnTouchListener {
        private int _xDelta;
        
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int eventX = (int) event.getRawX();
            RelativeLayout.LayoutParams layoutParams = 
                    (RelativeLayout.LayoutParams) view.getLayoutParams();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                _xDelta = eventX - layoutParams.leftMargin;
                puzzleDisplay.setTargetPositions(
                        targetDisplay1.getLettersLeft(),
                        targetDisplay1.getLettersWidth(),
                        targetDisplay2.getLettersLeft(),
                        targetDisplay2.getLettersWidth());
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
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
            }
            russianDollsLayout.invalidate();
            return true;
        }
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
                isLaidOut = false;
                targetDisplay1.setText(puzzle.getCombination());
                targetDisplay2.setText("");
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
        this.puzzle = puzzle;
        puzzleText.setText(puzzle.getClue());
        isLaidOut = false;
        targetDisplay1.setPuzzle(puzzle);
        nextButton.setText("Solve");
        puzzleDisplay.setPuzzle(puzzle);
    }
}
