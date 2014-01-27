package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class VograbularyGame implements ApplicationListener {
	private Stage stage;
	private TextField text1;
    private TextField text2;
    private TextField text3;
	private TextButton button;
	private boolean isFocusMovedAutomatically;
	
	@Override
	public void create() {		
        Skin skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        
        stage = new Stage();
        text1 = new TextField("", skin);
        text2 = new TextField("", skin);
        text3 = new TextField("", skin);
        button = new TextButton("Click Me!", skin);
        stage.addActor(text1);
        stage.addActor(text2);
        stage.addActor(text3);
        stage.addActor(button);
        button.addListener(new ChangeListener() {
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nextField();
            }
        });
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    nextField();
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
	    stage.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	    float fieldHeight = text1.getHeight();
        text1.setPosition(
                (width-button.getWidth())/2, 
                height-fieldHeight);
        text2.setPosition(
                (width-button.getWidth())/2, 
                height-fieldHeight*2);
        text3.setPosition(
                (width-button.getWidth())/2, 
                height-fieldHeight*3);
        button.setPosition(
                (width-button.getWidth())/2, 
                height-fieldHeight*4);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

    private void nextField() {
        button.setText("Clicked!");
        TextField nextField = 
                stage.getKeyboardFocus() == text1
                ? text2
                : stage.getKeyboardFocus() == text2
                ? text3
                : text1;
        nextField.setText("");
        if ( ! isFocusMovedAutomatically) {
            stage.setKeyboardFocus(nextField);
        }
    }
    
    public void setFocusMovedAutomatically(boolean isFocusMovedAutomatically) {
        this.isFocusMovedAutomatically = isFocusMovedAutomatically;
    }
}
