package com.github.donkirkby.vograbulary;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AndroidPreferences extends VograbularyPreferences {
    private SharedPreferences preferences;
    private Editor editor;
    
    /**
     * Initialize an instance.
     * @param context a context to retrieve the shared preferences from, such
     * as an Activity.
     */
    public AndroidPreferences(Context context) {
        this.preferences =
                context.getSharedPreferences("main", Context.MODE_PRIVATE);
    }
    
    @Override
    protected Set<String> getStringSet(String key, Set<String> defValues) {
        return preferences.getStringSet(key, defValues);
    }

    @Override
    protected void putStringSet(String key, Set<String> values) {
        if (editor == null) {
            editor = preferences.edit();
        }
        editor.putStringSet(key, values);
    }
    
    @Override
    public void apply() {
        editor.apply();
    }
}
