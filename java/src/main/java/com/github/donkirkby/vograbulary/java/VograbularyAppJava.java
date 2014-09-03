package com.github.donkirkby.vograbulary.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.github.donkirkby.vograbulary.core.VograbularyApp;

public class VograbularyAppJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new VograbularyApp());
  }
}
