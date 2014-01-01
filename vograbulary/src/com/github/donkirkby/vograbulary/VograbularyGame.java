package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.github.donkirkby.vograbulary.UltraghostController.State;

public class VograbularyGame implements ApplicationListener {
	private Stage stage;
	private TextField letters;
	private TextButton button;
	private UltraghostController ultraghostController = 
	        new UltraghostController();
	
	@Override
	public void create() {		
        Skin skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float scaleX = width / 150;
        float scaleY = height / 20;
        float scale = Math.min(scaleX, scaleY);
        
//        skin.getFont("default-font").setScale(scale);
        letters = new TextField("", skin);
        table.add(letters).expand().fillX();
        button = new TextButton("Next", skin);
        table.add(button);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                letters.setText(ultraghostController.next());
                if (ultraghostController.getState() == State.PUZZLE) {
                    Timer.schedule(
                            ultraghostController.createSearchTask(30), 
                            0.01f, 
                            0.01f);
                }
            }
        });
        
        ultraghostController.readWordList(
                Gdx.files.internal("data/wordlist.txt").reader());
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
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
