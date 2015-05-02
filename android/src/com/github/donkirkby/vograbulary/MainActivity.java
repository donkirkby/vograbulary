package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private ListView challengeList;
    private List<Integer> menuStringIds;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        challengeList = (ListView)findViewById(R.id.challengeList);
        menuStringIds = Arrays.asList(
                R.string.title_activity_ultraghost,
                R.string.title_activity_hyperghost,
                R.string.title_activity_russian_dolls,
                R.string.title_activity_bacronyms,
                R.string.title_activity_students);
        List<String> names = new ArrayList<String>();
        for (Integer menuStringId : menuStringIds) {
            names.add(getString(menuStringId));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                names);
        challengeList.setAdapter(adapter);
        challengeList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {
                        start(position);
                    }
        });
    }
    
    private void start(int position) {
        int menuStringId = menuStringIds.get(position);
        Intent intent = new Intent();
        if (menuStringId == R.string.title_activity_ultraghost) {
            intent.setClass(this, StudentChooserActivity.class);
        }
        else if (menuStringId == R.string.title_activity_hyperghost) {
            intent.setClass(this, StudentChooserActivity.class);
            intent.putExtra(UltraghostActivity.INTENT_EXTRA_IS_HYPERGHOST, true);
        }
        else if (menuStringId == R.string.title_activity_russian_dolls) {
            intent.setClass(this, RussianDollsActivity.class);
        }
        else if (menuStringId == R.string.title_activity_bacronyms) {
            intent.setClass(this, BacronymsActivity.class);
        }
        else {
            intent.setClass(this, StudentEditorActivity.class);
        }
        startActivity(intent);
    }
}
