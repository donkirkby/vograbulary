package com.github.donkirkby.vograbulary.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.github.donkirkby.vograbulary.core.VograbularyApp;

public class VograbularyAppHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform platform = HtmlPlatform.register(config);
    platform.assets().setPathPrefix("vograbulary/");
    PlayN.run(new VograbularyApp());
  }
}
