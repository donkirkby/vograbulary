package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class VograbularyGame extends Game {
    private MainScreen mainScreen;
    private PreferencesScreen preferencesScreen;
    private VograbularyPreferences preferences;
    private Skin skin;
    
	@Override
	public void create() {
        skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
	    mainScreen = new MainScreen(this);
	    preferencesScreen = new PreferencesScreen(this);
	    setScreen(mainScreen);
	}
	
	public Skin getSkin() {
        return skin;
    }
	
	public void showPreferences() {
	    setScreen(preferencesScreen);
	}
	
	public VograbularyPreferences getPreferences() {
	    if (preferences == null) {
            preferences = new VograbularyPreferences(
                    Gdx.app.getPreferences("vograbulary"));
        }
        return preferences;
    }
	
	public void showMain() {
	    setScreen(mainScreen);
	}

	@Override
	public void dispose() {
	    skin.dispose();
	    mainScreen.dispose();
	    preferencesScreen.dispose();
	}
}
