package com.github.donkirkby.vograbulary;

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
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        challengeList = (ListView)findViewById(R.id.challengeList);
        names = Arrays.asList("Ultraghost", "Russian Dolls");
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
        Class<?> activityClass;
        if (position == 0) {
            activityClass = StudentChooserActivity.class;
        }
        else {
            activityClass = RussianDollsActivity.class;
        }
        startActivity(new Intent(this, activityClass));
    }
}
