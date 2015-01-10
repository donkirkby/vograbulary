package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UltraghostPresenter extends Composite {
    public static final String HISTORY_TOKEN = "ultraghost";

    private static UltraghostPresenterUiBinder uiBinder = GWT
            .create(UltraghostPresenterUiBinder.class);

    interface UltraghostPresenterUiBinder extends
            UiBinder<Widget, UltraghostPresenter> {
    }

    public UltraghostPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
    }

//  //stopJesting
//  private Controller ultraghostController = 
//          new Controller();
//  private boolean isComputerOpponent;
//  private boolean isHyperghost;
//  private View view;
//
//  public UltraghostScreen(final VograbularyApp app) {
//      super(app);
//      Stage stage = getStage();
//      
//      Table table = new Table();
//      table.setFillParent(true);
//      stage.addActor(table);
//      
//      view = new View();
//      view.create(table, app, ultraghostController);
//      WordList wordList = new WordList();
//      wordList.read(
//              Gdx.files.internal("data/wordlist.txt").reader());
//      ultraghostController.setWordList(wordList);
//  }
//  
//  public void setComputerOpponent(boolean isComputerOpponent) {
//      this.isComputerOpponent = isComputerOpponent;
//  }
//  
//  public void setHyperghost(boolean isHyperghost) {
//      this.isHyperghost = isHyperghost;
//  }
//
//  @Override
//  public void show() {
//      view.clear();
//      VograbularyPreferences preferences = getApp().getPreferences();
//      ultraghostController.clearStudents();
//      if (isComputerOpponent) {
//          ComputerStudent computerStudent = new ComputerStudent(preferences);
//          computerStudent.setSearchBatchSize(30);
//          computerStudent.setMaxSearchBatchCount(1000); // 10s
//          ultraghostController.addStudent(computerStudent);
//          ultraghostController.addStudent(new Student("You"));
//      }
//      else {
//          String studentSelections = preferences.getStudentSelections();
//          int i = 0;
//          for (String studentName : preferences.getStudentNames()) {
//              if (studentSelections.charAt(i++) == 'Y') {
//                  ultraghostController.addStudent(new Student(studentName));
//              }
//          }
//      }
//      Match match = ultraghostController.getMatch();
//      match.setMinimumWordLength(
//              preferences.getUltraghostMinimumWordLength());
//      match.setHyperghost(isHyperghost);
//
//      super.show();
//  }
//  //resumeJesting
//  //stopJesting
//  private Match match;
//  private Label letters;
//  private Label studentName;
//  private TextField solution;
//  private TextField response;
//  private Label hint;
//  private Label result;
//  private Label scores;
//  private TextButton solveButton;
//  private TextButton respondButton;
//  private TextButton nextButton;
//  private TextButton[] focusButtons;
//  
//  public void create(
//          final Table table, 
//          final VograbularyApp app, 
//          final Controller controller) {
//      controller.setView(this);
//      Skin skin = app.getSkin();
//      table.top();
//      studentName = new Label(" ", skin);
//      table.add(studentName);
//      table.row();
//      
//      letters = new Label(" ", skin);
//      table.add(letters);
//      table.row();
//      
//      solution = new TextField("", skin);
//      table.add(solution).expandX().fillX().pad(5);
//      solveButton = new TextButton("Solve", skin);
//      table.add(solveButton).left();
//      table.row();
//      
//      response = new TextField("", skin);
//      table.add(response).expandX().fillX().pad(5);
//      respondButton = new TextButton("Respond", skin);
//      table.add(respondButton).left();
//      table.row();
//      
//      result = new Label(" ", skin);
//      table.add(result).fillX();
//      nextButton = new TextButton("Next", skin);
//      table.add(nextButton).left();
//      table.row();
//      
//      hint = new Label(" ", skin);
//      table.add(hint).fillX();
//      table.row();
//      
//      scores = new Label(" \n ", skin);
//      table.add(scores).fillX();
//      TextButton menuButton = new TextButton("Menu", skin);
//      table.add(menuButton).left().top();
//      
//      focusButtons = 
//              new TextButton[] {solveButton, respondButton, nextButton};
//      
//      nextButton.addListener(new ChangeListener() {
//          @Override
//          public void changed(ChangeEvent event, Actor actor) {
//              controller.start();
//          }
//      });
//      solveButton.addListener(new ChangeListener() {
//          @Override
//          public void changed(ChangeEvent event, Actor actor) {
//              match.getPuzzle().setSolution(solution.getText());
//              controller.solve();
//          }
//      });
//      respondButton.addListener(new ChangeListener() {
//          @Override
//          public void changed(ChangeEvent event, Actor actor) {
//              match.getPuzzle().setResponse(response.getText());
//          }
//      });
//      menuButton.addListener(new ChangeListener() {
//          @Override
//          public void changed(ChangeEvent event, Actor actor) {
//              controller.cancelMatch();
//              app.showMenu();
//          }
//      });
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
//
//  /**
//   * Schedule a task with the timer.
//   */
//  public void schedule(Task task, float delaySeconds, float intervalSeconds) {
//      Timer.schedule(task, delaySeconds, intervalSeconds);
//  }
//
//  /**
//   * Set the display's focus in the solution field.
//   */
//  public void focusSolution() {
//      focusButton(solveButton);
//      focusField(solution);
//  }
//
//  private void focusField(TextField field) {
//      field.getStage().setKeyboardFocus(field);
//      field.getOnscreenKeyboard().show(true);
//      field.selectAll();
//  }
//  
//  /**
//   * Set the display's focus in the response field.
//   */
//  public void focusResponse() {
//      focusButton(respondButton);
//      focusField(response);
//  }
//  
//  private void focusButton(TextButton target) {
//      for (TextButton button : focusButtons) {
//          button.setVisible(button == target);
//      }
//  }
//  
//  /**
//   * Set the display's focus on the Next button.
//   */
//  public void focusNextButton() {
//      focusButton(nextButton);
//      nextButton.getStage().setKeyboardFocus(nextButton);
//      solution.getOnscreenKeyboard().show(false);
//  }
//  
//  /**
//   * Disable all the buttons, except menu, while the computer student is
//   * thinking of a solution.
//   */
//  public void showThinking() {
//      focusButton(null);
//  }
//  
//  public Puzzle getPuzzle() {
//      return match.getPuzzle();
//  }
//
//  /**
//   * Set the match state for display. This will update several fields.
//   */
//  public void setMatch(Match match) {
//      this.match = match;
//      refreshPuzzle();
//  }
//  
//  public Match getMatch() {
//      return match;
//  }
//  
//  /**
//   * Update the display to show all parts of the puzzle.
//   */
//  public void refreshPuzzle() {
//      if (letters == null) {
//          // must be in unit tests, nothing to do.
//          return;
//      }
//      if (match == null) {
//          return;
//      }
//      Student winner = match.getWinner();
//      scores.setText(match.getSummary());
//      if (winner != null) {
//          String resultText = winner.getName() + " win";
//          if (winner.getName() != "You") {
//              resultText += "s";
//          }
//          result.setText(resultText);
//          studentName.setText(" ");
//          letters.setText(" ");
//          solution.setText(" ");
//          response.setText(" ");
//          hint.setText(" ");
//          for (TextButton button : focusButtons) {
//              button.setVisible(false);
//          }
//          return;
//      }
//      Puzzle puzzle = match.getPuzzle();
//      if (puzzle != null) {
//          Student owner = puzzle.getOwner();
//          studentName.setText(owner == null ? "" : owner.getName());
//          letters.setText(blankForNull(puzzle.getLettersDisplay()));
//          setFieldContents(solution, puzzle.getSolution());
//          setFieldContents(response, puzzle.getResponse());
//          hint.setText(blankForNull(puzzle.getHint()) + " ");
//          WordResult puzzleResult = puzzle.getResult();
//          result.setText(
//                  puzzleResult == WordResult.UNKNOWN 
//                  ? "" 
//                  : puzzleResult.toString());
//      }
//  }
//
//  private void setFieldContents(TextField field, String contents) {
//      if (field.getText() != contents) {
//          // Only set it when the value changes, otherwise the selection
//          // gets lost.
//          field.setText(blankForNull(contents));
//      }
//  }
//  
//  private String blankForNull(Object o) {
//      return o == null ? "" : o.toString();
//  }
//  //resumeJesting
}
