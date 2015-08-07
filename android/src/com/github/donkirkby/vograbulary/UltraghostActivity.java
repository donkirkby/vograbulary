package com.github.donkirkby.vograbulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.ultraghost.ComputerStudent;
import com.github.donkirkby.vograbulary.ultraghost.Controller;
import com.github.donkirkby.vograbulary.ultraghost.Match;
import com.github.donkirkby.vograbulary.ultraghost.Puzzle;
import com.github.donkirkby.vograbulary.ultraghost.Student;
import com.github.donkirkby.vograbulary.ultraghost.Student.StudentListener;
import com.github.donkirkby.vograbulary.ultraghost.UltraghostRandom;
import com.github.donkirkby.vograbulary.ultraghost.UltraghostScreen;
import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class UltraghostActivity
extends VograbularyActivity implements UltraghostScreen, StudentListener {
    public static final String INTENT_EXTRA_STUDENT_NAMES =
            "com.github.donkirkby.vograbulary.ultraghost.studentnames";
    public static final String INTENT_EXTRA_IS_HYPERGHOST =
            "com.github.donkirkby.vograbulary.ultraghost.ishyperghost";
    
    private TextView ownerName;
    private TextView letters;
    private EditText solution;
    private EditText response;
    private TextView hint;
    private TextView result;
    private TextView summary;
    private Button solveButton;
    private Button respondButton;
    private Button nextButton;
    private List<Button> focusButtons;
    
    private Match match;
    private Controller controller;
    private Handler handler;
    private ViewGroup ultraghostLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultraghost);
        
        Intent intent = getIntent();
        String[] studentNames = 
                intent.getStringArrayExtra(INTENT_EXTRA_STUDENT_NAMES);
        
        ownerName = (TextView)findViewById(R.id.ownerName);
        letters = (TextView)findViewById(R.id.letters);
        solution = (EditText)findViewById(R.id.solution);
        response = (EditText)findViewById(R.id.response);
        hint = (TextView)findViewById(R.id.hint);
        result = (TextView)findViewById(R.id.result);
        summary = (TextView)findViewById(R.id.summary);
        solveButton = (Button)findViewById(R.id.solveButton);
        respondButton = (Button)findViewById(R.id.respondButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        focusButtons = Arrays.asList(solveButton, respondButton, nextButton);
        handler = new Handler();
        
        TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() != KeyEvent.ACTION_UP) {
                    return true;
                }
                if (solveButton.getVisibility() == View.VISIBLE) {
                    solve(null);
                }
                else if (respondButton.getVisibility() == View.VISIBLE) {
                    respond(null);
                }
                else if (nextButton.getVisibility() == View.VISIBLE) {
                    next(null);
                }
                return true;
            }
        };
        solution.setOnEditorActionListener(actionListener);
        response.setOnEditorActionListener(actionListener);
        
        ultraghostLayout = (ViewGroup)findViewById(R.id.ultraghostLayout);
        ultraghostLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    private boolean isLandscape;
                    
                    @Override
                    public void onGlobalLayout() {
                        Rect bounds = new Rect();
                        Paint paint = response.getPaint();
                        int desiredWidth = 22;
                        StringBuilder testString = new StringBuilder();
                        for (int i = 0; i < desiredWidth; i++) {
                            testString.append("X");
                        }
                        paint.getTextBounds(
                                testString.toString(),
                                0,
                                desiredWidth,
                                bounds);
                        Rect windowBounds = new Rect();
                        ultraghostLayout.getWindowVisibleDisplayFrame(windowBounds);
                        int windowWidth = windowBounds.right - windowBounds.left;
                        int windowHeight = windowBounds.bottom - windowBounds.top;
                        if (windowWidth > windowHeight && ! isLandscape) {
                            isLandscape = true;
                            RelativeLayout.LayoutParams layoutParams = 
                                    (LayoutParams) summary.getLayoutParams();
                            layoutParams.addRule(RelativeLayout.BELOW, R.id.letters);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            
                            layoutParams =
                                    (LayoutParams) solveButton.getLayoutParams();
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                            layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.summary);
                        }
                        float correctionX = (float)windowWidth / bounds.width();
                        float correctionY = windowHeight / (bounds.height()*15f);
                        float correction = Math.min(correctionX, correctionY);
                        if (correction < 1 || 1.5 < correction) {
                            float oldTextSize = response.getTextSize();
                            float textSize = oldTextSize*correction;
                            solution.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    textSize);
                            solveButton.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    textSize);
                            response.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    textSize);
                            respondButton.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    textSize);
                            hint.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    textSize * .75f);
                            nextButton.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    textSize);
                        }
                    }
                });
        AndroidPreferences preferences = new AndroidPreferences(this);
        controller = new Controller();
        controller.setPreferences(preferences);
        controller.setRandom(new UltraghostRandom());
        controller.setScheduler(new AndroidScheduler());
        controller.setScreen(this);
        List<String> wordSource;
        try {
            wordSource = loadTextAsset("wordlist.txt");
        } catch (IOException e) {
            wordSource = new ArrayList<String>();
        }
        WordList wordList = new WordList();
        wordList.read(wordSource);
        if (studentNames.length < 2) {
            ComputerStudent computerStudent = new ComputerStudent(preferences);
            computerStudent.setWordList(wordList);
            computerStudent.setListener(this);
            computerStudent.setSearchBatchSize(30);
            computerStudent.setMaxSearchBatchCount(1000); // 10s
            controller.addStudent(computerStudent);
            controller.addStudent(new Student("You"));
        }
        else {
            for (int i = 0; i < studentNames.length; i++) {
                String name = studentNames[i];
                controller.addStudent(new Student(name));
            }
        }
        controller.setWordList(wordList);
        if (intent.getBooleanExtra(INTENT_EXTRA_IS_HYPERGHOST, true)) {
            controller.getMatch().setHyperghost(true);
        }
        controller.start();
    }

    @Override
    public void refreshPuzzle() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Puzzle puzzle = match.getPuzzle();
                Student winner = match.getWinner();
                if (winner != null) {
                    String resultText = winner.getName() + " win";
                    if (winner.getName() != "You") {
                        resultText += "s";
                    }
                    ownerName.setText(resultText);
                    focusButton(null);
                }
                else {
                    ownerName.setText(puzzle.getOwner().getName());
                }
                String letterText = puzzle.getLetters();
                if (puzzle.getPreviousWord() != null) {
                    letterText = letterText + " after " +
                            puzzle.getPreviousWord();
                }
                letters.setText(letterText);
                solution.setText(puzzle.getSolution());
                response.setText(puzzle.getResponse());
                hint.setText(puzzle.getHint());
                result.setText(puzzle.getResultDisplay());
                summary.setText(match.getSummary());
            }
        });
    }

    @Override
    public void refreshScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.setText(match.getPuzzle().getResultDisplay());
            }
        });
    }

    public void solve(View view) {
        match.getPuzzle().setSolution(solution.getText().toString());
        controller.solve();
    }
    
    public void respond(View view) {
        match.getPuzzle().setResponse(response.getText().toString());
    }
    
    public void next(View view) {
        controller.start();
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }

    private void focusField(final EditText field) {
        field.requestFocus();
        handler.post(new Runnable() {
            @Override
            public void run() {
                field.selectAll();
            }
        });
    }

    @Override
    public void focusSolution() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                focusField(solution);
                focusButton(solveButton);
            }
        });
    }

    @Override
    public void focusResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                focusField(response);
                focusButton(respondButton);
            }
        });
    }

    @Override
    public void focusNextButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                focusButton(nextButton);
            }
        });
    }

    @Override
    public void showThinking() {
        focusButton(null);
    }

    @Override
    public void setMatch(Match match) {
        this.match = match;
    }
    @Override
    public Match getMatch() {
        return match;
    }

    private void focusButton(Button target) {
        boolean isFinished = match.getWinner() != null;
        for (Button button : focusButtons) {
            button.setVisibility(
                    button == target && !isFinished
                    ? Button.VISIBLE
                    : Button.INVISIBLE);
        }
    }

    @Override
    public void askForSolution() {
        focusSolution();
    }

    @Override
    public void askForResponse() {
        focusResponse();
    }
}
