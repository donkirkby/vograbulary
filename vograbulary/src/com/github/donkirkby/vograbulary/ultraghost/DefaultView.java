package com.github.donkirkby.vograbulary.ultraghost;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.github.donkirkby.vograbulary.UltraghostController;

public class DefaultView implements View {
    private Label letters;
    private Label playerName;
    private TextField solution;
    private TextButton button;
    private UltraghostController controller;
    
    public DefaultView(UltraghostController controller) {
        this.controller = controller;
        controller.setView(this);
    }

    public void create(Table table, Skin skin) {
        playerName = new Label(" ", skin);
        table.add(playerName);
        table.row();
        letters = new Label(" ", skin);
        table.add(letters);
        table.row();
        solution = new TextField("", skin);
        table.add(solution).expand().fillX();
        button = new TextButton("Next", skin);
        table.add(button);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.next();
//                letters.setText(ultraghostController.next());
//                if (ultraghostController.getState() == State.PUZZLE) {
//                    Timer.schedule(
//                            ultraghostController.createSearchTask(30), 
//                            0.01f, 
//                            0.01f);
//                }
            }
        });
    }
    
    /* (non-Javadoc)
     * @see com.github.donkirkby.vograbulary.ultraghost.View#schedule(com.badlogic.gdx.utils.Timer.Task, float, float)
     */
    @Override
    public void schedule(Task task, float delaySeconds, float intervalSeconds) {
        Timer.schedule(task, delaySeconds, intervalSeconds);
    }
    
    /* (non-Javadoc)
     * @see com.github.donkirkby.vograbulary.ultraghost.View#setPuzzle(java.lang.String)
     */
    @Override
    public void setPuzzle(String letters) {
        this.letters.setText(letters);
    }
    
    /* (non-Javadoc)
     * @see com.github.donkirkby.vograbulary.ultraghost.View#setActivePlayer(java.lang.String)
     */
    @Override
    public void setActivePlayer(String name) {
        playerName.setText(name);
    }

    @Override
    public void setSolution(String solution) {
        this.solution.setText(solution);
    }
}
