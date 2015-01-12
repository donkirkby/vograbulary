package com.github.donkirkby.vograbulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.ultraghost.ComputerStudent;
import com.github.donkirkby.vograbulary.ultraghost.Controller;
import com.github.donkirkby.vograbulary.ultraghost.Match;
import com.github.donkirkby.vograbulary.ultraghost.Puzzle;
import com.github.donkirkby.vograbulary.ultraghost.Student;
import com.github.donkirkby.vograbulary.ultraghost.UltraghostRandom;
import com.github.donkirkby.vograbulary.ultraghost.UltraghostScreen;
import com.github.donkirkby.vograbulary.ultraghost.WordList;
import com.github.donkirkby.vograbulary.ultraghost.WordResult;
import com.github.donkirkby.vograbulary.ultraghost.Student.StudentListener;

public class UltraghostActivity
extends VograbularyActivity implements UltraghostScreen, StudentListener {
    public static final String INTENT_EXTRA_IS_COMPUTER = 
            "com.github.donkirkby.vograbulary.ultraghost.iscomputer";
    
    private TextView ownerName;
    private TextView letters;
    private TextView solution;
    private TextView response;
    private TextView hint;
    private TextView result;
    private TextView summary;
    private Button solveButton;
    private Button respondButton;
    private Button nextButton;
    private List<Button> focusButtons;
    
    private Match match;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultraghost);
        
        Intent intent = getIntent();
        boolean isComputer = false;
        isComputer =
                intent.getBooleanExtra(INTENT_EXTRA_IS_COMPUTER, isComputer);
        
        ownerName = (TextView)findViewById(R.id.ownerName);
        letters = (TextView)findViewById(R.id.letters);
        solution = (TextView)findViewById(R.id.solution);
        response = (TextView)findViewById(R.id.response);
        hint = (TextView)findViewById(R.id.hint);
        result = (TextView)findViewById(R.id.result);
        summary = (TextView)findViewById(R.id.summary);
        solveButton = (Button)findViewById(R.id.solveButton);
        respondButton = (Button)findViewById(R.id.respondButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        focusButtons = Arrays.asList(solveButton, respondButton, nextButton);

        controller = new Controller();
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
        if (isComputer) {
            ComputerStudent computerStudent =
                    new ComputerStudent(new VograbularyPreferences());
            computerStudent.setWordList(wordList);
            computerStudent.setListener(this);
            computerStudent.setSearchBatchSize(30);
            computerStudent.setMaxSearchBatchCount(1000); // 10s
            controller.addStudent(computerStudent);
            controller.addStudent(new Student("You"));
        }
        else {
            controller.addStudent(new Student("Alice"));
            controller.addStudent(new Student("Bob"));
        }
        controller.setWordList(wordList);
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
                    ownerName.setText("Winner: " + winner.getName());
                    letters.setText("");
                    focusButton(null);
                }
                else {
                    ownerName.setText(puzzle.getOwner().getName());
                    letters.setText(puzzle.getLetters());
                }
                solution.setText(puzzle.getSolution());
                response.setText(puzzle.getResponse());
                hint.setText(puzzle.getHint());
                if (puzzle.getResult() == WordResult.UNKNOWN) {
                    result.setText("");
                }
                else {
                    result.setText(puzzle.getResult().toString());
                }
                summary.setText(match.getSummary());
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

    @Override
    public void focusSolution() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                solution.requestFocus();
                focusButton(solveButton);
            }
        });
    }

    @Override
    public void focusResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                response.requestFocus();
                focusButton(respondButton);
            }
        });
    }

    @Override
    public void focusNextButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nextButton.requestFocus();
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
