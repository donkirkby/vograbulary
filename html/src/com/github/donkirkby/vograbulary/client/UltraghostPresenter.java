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
import com.github.donkirkby.vograbulary.ultraghost.WordResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
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
    ParagraphElement ownerName;
    
    @UiField
    ParagraphElement letters;
    
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
    ParagraphElement result;
    
    @UiField
    DivElement summary;
    
    private Controller controller = 
            new Controller();
    private Match match;
    private List<Button> focusButtons;
    private GwtPreferences preferences;

    public UltraghostPresenter(GwtPreferences preferences) {
        initWidget(uiBinder.createAndBindUi(this));

        this.preferences = preferences;
        String wordListText = Assets.INSTANCE.wordList().getText();
        WordList wordList = new WordList();
        wordList.read(Arrays.asList(wordListText.split("\\n")));
        controller.setWordList(wordList);
        controller.setScheduler(new GwtScheduler());

//      Match match = controller.getMatch();
//      match.setMinimumWordLength(
//              preferences.getUltraghostMinimumWordLength());
//      match.setHyperghost(isHyperghost);
  
        focusButtons = Arrays.asList(solveButton, respondButton, nextButton);
        
        controller.setScreen(this);
    }
    
    @Override
    protected void onLoad() {
        super.onLoad();
        controller.start();
    }
    
    @UiHandler("solveButton")
    void solve(ClickEvent e) {
        match.getPuzzle().setSolution(solution.getText());
        controller.solve();
    }
    
    @UiHandler("respondButton")
    void respond(ClickEvent e) {
        match.getPuzzle().setResponse(response.getText());
    }
    
    @UiHandler("nextButton")
    void next(ClickEvent e) {
        controller.start();
    }
//      table.addListener(new InputListener() {
//          @Override
//          public boolean keyUp(InputEvent event, int keycode) {
//              if (keycode == Input.Keys.ENTER) {
//                  ChangeListener.ChangeEvent changeEvent = 
//                          new ChangeListener.ChangeEvent();
//                  for (TextButton button : focusButtons) {
//                      if (button.isVisible()) {
//                          button.fire(changeEvent);
//                          return true;
//                      }
//                  }
//              }
//              return false;
//          }
//      });
//      // On some platforms, setting the focus in an enter key handler is
//      // not compatible with the default focus traversal. Disable it.
//      solution.setFocusTraversal(false);
//      response.setFocusTraversal(false);
//      
//      focusNextButton();
//  }
//  
//  public void clear() {
//      studentName.setText(" ");
//      letters.setText(" ");
//      solution.setText("");
//      response.setText("");
//      hint.setText(" ");
//      result.setText("");
//      scores.setText("");
//  }

    private void focusButton(Button target) {
        Student winner = match.getWinner();
        for (Button button : focusButtons) {
            button.setVisible(winner == null && button == target);
        }
    }

    @Override
    public void clear() {
    }
    
    @Override
    public void focusSolution() {
        focusButton(solveButton);
        solution.setFocus(true);
    }
    
    @Override
    public void focusResponse() {
        focusButton(respondButton);
        response.setFocus(true);
    }
    
    @Override
    public void focusNextButton() {
        focusButton(nextButton);
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
//      if (letters == null) {
//      // must be in unit tests, nothing to do.
//      return;
//  }
//  if (match == null) {
//      return;
//  }
//  Student winner = match.getWinner();
//  if (winner != null) {
//      String resultText = winner.getName() + " win";
//      if (winner.getName() != "You") {
//          resultText += "s";
//      }
//      result.setText(resultText);
//      studentName.setText(" ");
//      letters.setText(" ");
//      solution.setText(" ");
//      response.setText(" ");
//      hint.setText(" ");
//      for (TextButton button : focusButtons) {
//          button.setVisible(false);
//      }
//      return;
//  }
//  Puzzle puzzle = match.getPuzzle();
//  if (puzzle != null) {
//      Student owner = puzzle.getOwner();
//      studentName.setText(owner == null ? "" : owner.getName());
//      hint.setText(blankForNull(puzzle.getHint()) + " ");
//  }
        Student winner = match.getWinner();
        Puzzle puzzle = match.getPuzzle();
        if (winner != null) {
            String resultText = winner.getName() + " win";
            if (winner.getName() != "You") {
                resultText += "s";
            }
            result.setInnerText(resultText);
        }
        else {
            WordResult puzzleResult = puzzle.getResult();
            result.setInnerText(
                    puzzleResult == WordResult.UNKNOWN 
                    ? "" 
                    : puzzleResult.toString());
            
        }
        ownerName.setInnerText(puzzle.getOwner().getName());
        letters.setInnerText(puzzle.getLetters());
        solution.setText(puzzle.getSolution());
        response.setText(puzzle.getResponse());
        hint.setInnerText(puzzle.getHint());
        summary.setInnerText(match.getSummary());
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
