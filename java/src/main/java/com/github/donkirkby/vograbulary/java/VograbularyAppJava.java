package com.github.donkirkby.vograbulary.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;
import tripleplay.platform.JavaTPPlatform;

import com.github.donkirkby.vograbulary.core.VograbularyApp;

public class VograbularyAppJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform platform = JavaPlatform.register(config);
    JavaTPPlatform.register(platform, config);
    PlayN.run(new VograbularyApp());
  }
}
