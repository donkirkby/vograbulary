package com.github.donkirkby.vograbulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.bacronyms.BacronymsScreen;
import com.github.donkirkby.vograbulary.bacronyms.Controller;
import com.github.donkirkby.vograbulary.bacronyms.Puzzle;
import com.github.donkirkby.vograbulary.ultraghost.WordList;

public class BacronymsActivity
extends VograbularyActivity implements BacronymsScreen {
    private Controller controller = new Controller();
    private Puzzle puzzle;
    private State state;
    private TextView stateText;
    private List<Button> wordButtons;
    private Button nextButton;

    @Override
    public Puzzle getPuzzle() {
        return puzzle;
    }
    @Override
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        displayWords();
    }
    
    private void displayWords() {
        for (int i = 0; i < wordButtons.size(); i++) {
            wordButtons.get(i).setText(puzzle.getWord(i));
        }
    }
    
    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
        stateText.setText(state.name());
        nextButton.setVisibility(
                state == State.SOLVED
                ? View.VISIBLE
                : View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacronyms);
        wordButtons = Arrays.asList(
                (Button)findViewById(R.id.word1),
                (Button)findViewById(R.id.word2),
                (Button)findViewById(R.id.word3));
        stateText = (TextView)findViewById(R.id.stateText);
        nextButton = (Button)findViewById(R.id.nextButton);

        List<String> puzzleLines;
        List<String> wordSource;
        try {
            puzzleLines = loadTextAsset("bacronyms.txt");
            wordSource = loadTextAsset("wordlist.txt");
        } catch (IOException e) {
            puzzleLines = Arrays.asList(
                    "Failed to open file. " + e.getMessage());
            wordSource = new ArrayList<String>();
        }
        WordList wordList = new WordList();
        wordList.read(wordSource);
        controller.setScreen(this);
        controller.setWordList(wordList);
        controller.loadPuzzles(puzzleLines);
        
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.next();
            }
        });
        for (int i = 0; i < wordButtons.size(); i++) {
            final int wordIndex = i;
            wordButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    puzzle.setSelectedIndex(wordIndex);
                    controller.solve();
                    displayWords();
                }
            });
        }
        controller.next();
    }
}
