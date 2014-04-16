package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.Preferences;

public class VograbularyPreferences {
    private enum Fields { Player1Name, Player2Name };
    private Preferences preferences;
    
    public VograbularyPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getPlayer1Name() {
        return preferences.getString(Fields.Player1Name.name(), "Alice");
    }
    public void setPlayer1Name(String player1Name) {
        preferences.putString(Fields.Player1Name.name(), player1Name);
        preferences.flush();
    }
    
    public String getPlayer2Name() {
        return preferences.getString(Fields.Player2Name.name(), "Bob");
    }
    public void setPlayer2Name(String player2Name) {
        preferences.putString(Fields.Player2Name.name(), player2Name);
        preferences.flush();
    }
}
