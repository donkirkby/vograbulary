package com.github.donkirkby.vograbulary.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.github.donkirkby.vograbulary.ultraghost.ComputerStudent;
import com.github.donkirkby.vograbulary.ultraghost.Controller;
import com.github.donkirkby.vograbulary.ultraghost.Match;
import com.github.donkirkby.vograbulary.ultraghost.Puzzle;
import com.github.donkirkby.vograbulary.ultraghost.Student;
import com.github.donkirkby.vograbulary.ultraghost.UltraghostScreen;
import com.github.donkirkby.vograbulary.ultraghost.WordList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UltraghostPresenter extends VograbularyPresenter implements UltraghostScreen {
    public static final String HISTORY_TOKEN = "ultraghost";
    public static final String HISTORY_TOKEN_HYPER = "hyperghost";

    private static UltraghostPresenterUiBinder uiBinder = GWT
            .create(UltraghostPresenterUiBinder.class);

    interface UltraghostPresenterUiBinder extends
            UiBinder<Widget, UltraghostPresenter> {
    }
    
    @UiField
    HeadingElement ownerName;
    
    @UiField
    HeadingElement letters;
    
    @UiField
    TextBox solution;
    
    @UiField
    Button solveButton;
    
    @UiField
    TextBox response;
    
    @UiField
    Button respondButton;
    
    @UiField
    SpanElement hint;
    
    @UiField
    Button nextButton;
    
    @UiField
    Button pauseButton;
    
    @UiField
    ParagraphElement result;
    
    @UiField
    DivElement summary;
    
    private Controller controller = 
            new Controller();
    private Match match;
    private List<Button> focusButtons;
    private List<TextBox> focusFields;
    private GwtPreferences preferences;
    private GwtScheduler scheduler;

    public UltraghostPresenter(GwtPreferences preferences) {
        initWidget(uiBinder.createAndBindUi(this));

        letters.setId("letters");
        ownerName.setId("ownerName");
        
        this.preferences = preferences;
        String wordListText = Assets.INSTANCE.wordList().getText();
        WordList wordList = new WordList();
        wordList.read(Arrays.asList(wordListText.split("\\n")));
        controller.setPreferences(preferences);
        controller.setWordList(wordList);
        scheduler = new GwtScheduler();
        controller.setScheduler(scheduler);
  
        focusButtons = Arrays.asList(solveButton, respondButton, nextButton);
        focusFields = Arrays.asList(solution, response);
        
        controller.setScreen(this);
    }
    
    @Override
    protected void onLoad() {
        super.onLoad();
        controller.start();
    }
    
    @UiHandler("solution")
    void solutionKeyPress(KeyPressEvent e) {
        if (e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            solve(null);
        }
    }
    
    @UiHandler("solveButton")
    void solve(ClickEvent e) {
        match.getPuzzle().setSolution(solution.getText());
        controller.solve();
    }
    
    @UiHandler("response")
    void responseKeyPress(KeyPressEvent e) {
        if (e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            respond(null);
        }
    }
    
    @UiHandler("respondButton")
    void respond(ClickEvent e) {
        match.getPuzzle().setResponse(response.getText());
    }
    
    @UiHandler("nextButton")
    void next(ClickEvent e) {
        controller.start();
    }
    
    @UiHandler("pauseButton")
    void pause(ClickEvent e) {
        match.getPuzzle().togglePause();
    }
    
    private void focusField(final TextBox target) {
        for (TextBox field : focusFields) {
            field.setEnabled(target == field);
        }
        if (target != null) {
            target.setFocus(true);
            scheduler.scheduleDeferred(new Runnable() {
                @Override
                public void run() {
                    target.selectAll();
                }
            });
        }
    }

    private void focusButton(Button target) {
        boolean isFinished = match.getWinner() != null;
        for (Button button : focusButtons) {
            button.setVisible(button == target && ! isFinished);
        }
        pauseButton.setVisible(target != nextButton && ! isFinished);
    }

    @Override
    public void clear() {
    }
    
    @Override
    public void focusSolution() {
        focusButton(solveButton);
        focusField(solution);
    }
    
    @Override
    public void focusResponse() {
        focusButton(respondButton);
        focusField(response);
    }
    
    @Override
    public void focusNextButton() {
        focusButton(nextButton);
        focusField(null);
        nextButton.setFocus(true);
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
    
    @Override
    public void refreshPuzzle() {
        Student winner = match.getWinner();
        Puzzle puzzle = match.getPuzzle();
        if (winner != null) {
            String resultText = winner.getName() + " win";
            if (winner.getName() != "You") {
                resultText += "s";
            }
            ownerName.setInnerText(resultText);
            focusButton(null);
        }
        else {
            ownerName.setInnerText(puzzle.getOwner().getName());
        }
        String letterText = puzzle.getLetters();
        if (puzzle.getPreviousWord() != null) {
            letterText = letterText + " after " + puzzle.getPreviousWord();
        }
        letters.setInnerText(letterText);
        solution.setText(puzzle.getSolution());
        response.setText(puzzle.getResponse());
        hint.setInnerText(puzzle.getHint());
        refreshScore();
        summary.setInnerText(match.getSummary());
        pauseButton.setText(puzzle.isPaused() ? "Resume" : "Pause");
    }

    @Override
    public void refreshScore() {
        Puzzle puzzle = match.getPuzzle();
        result.setInnerText(puzzle.getResultDisplay());
    }

    public void setStudents(Collection<String> students) {
        for (String studentName : students) {
            controller.addStudent(new Student(studentName));
        }
        if (students.size() == 0) {
            controller.addStudent(new Student("You"));
        }
        if (students.size() < 2) {
            ComputerStudent computerStudent = new ComputerStudent(preferences);
            computerStudent.setSearchBatchSize(30);
            computerStudent.setMaxSearchBatchCount(1000); // 10s
            controller.addStudent(computerStudent);
        }
    }

    public UltraghostPresenter setHyperghost(boolean isHyperghost) {
        controller.getMatch().setHyperghost(isHyperghost);
        return this;
    }
    public boolean isHyperghost() {
        return controller.getMatch().isHyperghost();
    }
}
