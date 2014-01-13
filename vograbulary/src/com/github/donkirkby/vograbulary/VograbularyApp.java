package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.donkirkby.vograbulary.ultraghost.Controller;
import com.github.donkirkby.vograbulary.ultraghost.View;

//stopJesting
public class VograbularyApp implements ApplicationListener {
	private Stage stage;
	private Controller ultraghostController = 
	        new Controller();
	
	@Override
	public void create() {		
        Skin skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
        
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        
        View view = new View();
        view.create(table, skin, ultraghostController);
        ultraghostController.readWordList(
                Gdx.files.internal("data/wordlist.txt").reader());
        ultraghostController.setSearchBatchSize(100);
        ultraghostController.setMaxSearchBatchForComputer(1000); // 10s
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
//resumeJesting