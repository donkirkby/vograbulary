package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;

public class VograbularyActivity extends Activity {
    protected ArrayList<String> loadTextAsset(String assetName) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader reader =new BufferedReader(new InputStreamReader(
                getAssets().open(assetName)));
        try {
            String line;
            while (null != (line = reader.readLine())) {
                lines.add(line);
            }
        } finally {
            reader.close();
        }
        return lines;
    }
}
