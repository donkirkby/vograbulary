package com.github.donkirkby.vograbulary.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.github.donkirkby.vograbulary.core.VograbularyApp;

public class VograbularyAppActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new VograbularyApp());
  }
}
