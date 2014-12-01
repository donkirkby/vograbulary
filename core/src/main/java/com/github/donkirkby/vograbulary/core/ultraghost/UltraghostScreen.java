package com.github.donkirkby.vograbulary.core.ultraghost;

import java.io.StringReader;

import playn.core.PlayN;
import playn.core.util.Callback;
import tripleplay.platform.NativeTextField;
import tripleplay.platform.TPPlatform;
import tripleplay.ui.Button;
import tripleplay.ui.Field;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

import com.github.donkirkby.vograbulary.core.ChallengeScreen;

public class UltraghostScreen extends ChallengeScreen {
    //stopJesting
    private Match match;
    private Label letters;
    private Label studentName;
    private Field solution;
    private Field response;
    private Label hint;
    private Label result;
    private Label scores;
    private Button solveButton;
    private Button respondButton;
    private Button nextButton;
    private Button[] focusButtons;
    private Button menuButton;
    private Controller ultraghostController = new Controller();
    private boolean isComputerOpponent;
    private boolean isHyperghost;

    @Override
    public Button getMenuButton() {
        return menuButton;
    }

    @Override
    public String getName() {
        return "Ultraghost";
    }

    @Override
    protected Group createBody() {
        menuButton = new Button("Menu");
        PlayN.assets().getText("wordlist.txt", new Callback<String>() {
            @Override
            public void onSuccess(String text) {
                WordList wordList = new WordList();
                
                wordList.read(text);
                ultraghostController.setWordList(wordList);
            }
            
            @Override
            public void onFailure(Throwable cause) {
                // TODO: display error
//                loadPuzzles("Failed to load words: " + cause.getMessage());
            }
        });
        ultraghostController.setScreen(this);
        studentName = new Label(" ");
        letters = new Label(" ");
        solution = new Field();
        
//  table.add(solution).expandX().fillX().pad(5);
//  solveButton = new TextButton("Solve", skin);
//  table.add(solveButton).left();
//  table.row();
//  
//  response = new TextField("", skin);
//  table.add(response).expandX().fillX().pad(5);
//  respondButton = new TextButton("Respond", skin);
//  table.add(respondButton).left();
//  table.row();
//  
//  result = new Label(" ", skin);
//  table.add(result).fillX();
//  nextButton = new TextButton("Next", skin);
//  table.add(nextButton).left();
//  table.row();
//  
//  hint = new Label(" ", skin);
//  table.add(hint).fillX();
//  table.row();
//  
//  scores = new Label(" \n ", skin);
//  table.add(scores).fillX();
//  TextButton menuButton = new TextButton("Menu", skin);
//  table.add(menuButton).left().top();
        Group table = new Group(AxisLayout.vertical().offStretch())
            .add(studentName)
            .add(letters)
            .add(solution)
            .add(menuButton);
//  
//  focusButtons = 
//          new TextButton[] {solveButton, respondButton, nextButton};
//  
//  nextButton.addListener(new ChangeListener() {
//      @Override
//      public void changed(ChangeEvent event, Actor actor) {
//          controller.start();
//      }
//  });
//  solveButton.addListener(new ChangeListener() {
//      @Override
//      public void changed(ChangeEvent event, Actor actor) {
//          match.getPuzzle().setSolution(solution.getText());
//          controller.solve();
//      }
//  });
//  respondButton.addListener(new ChangeListener() {
//      @Override
//      public void changed(ChangeEvent event, Actor actor) {
//          match.getPuzzle().setResponse(response.getText());
//      }
//  });
//  menuButton.addListener(new ChangeListener() {
//      @Override
//      public void changed(ChangeEvent event, Actor actor) {
//          controller.cancelMatch();
//          app.showMenu();
//      }
//  });
//  table.addListener(new InputListener() {
//      @Override
//      public boolean keyUp(InputEvent event, int keycode) {
//          if (keycode == Input.Keys.ENTER) {
//              ChangeListener.ChangeEvent changeEvent = 
//                      new ChangeListener.ChangeEvent();
//              for (TextButton button : focusButtons) {
//                  if (button.isVisible()) {
//                      button.fire(changeEvent);
//                      return true;
//                  }
//              }
//          }
//          return false;
//      }
//  });
//  // On some platforms, setting the focus in an enter key handler is
//  // not compatible with the default focus traversal. Disable it.
//  solution.setFocusTraversal(false);
//  response.setFocusTraversal(false);
//  
//  focusNextButton();
//}
        return table;
    }
  //}
    //
    //public void setComputerOpponent(boolean isComputerOpponent) {
    //this.isComputerOpponent = isComputerOpponent;
    //}
    //
    //public void setHyperghost(boolean isHyperghost) {
    //this.isHyperghost = isHyperghost;
    //}
//
//@Override
//public void show() {
//view.clear();
//VograbularyPreferences preferences = getApp().getPreferences();
//ultraghostController.clearStudents();
//if (isComputerOpponent) {
//  ComputerStudent computerStudent = new ComputerStudent(preferences);
//  computerStudent.setSearchBatchSize(30);
//  computerStudent.setMaxSearchBatchCount(1000); // 10s
//  ultraghostController.addStudent(computerStudent);
//  ultraghostController.addStudent(new Student("You"));
//}
//else {
//  String studentSelections = preferences.getStudentSelections();
//  int i = 0;
//  for (String studentName : preferences.getStudentNames()) {
//      if (studentSelections.charAt(i++) == 'Y') {
//          ultraghostController.addStudent(new Student(studentName));
//      }
//  }
//}
//Match match = ultraghostController.getMatch();
//match.setMinimumWordLength(
//      preferences.getUltraghostMinimumWordLength());
//match.setHyperghost(isHyperghost);
//
//super.show();
//}

}
