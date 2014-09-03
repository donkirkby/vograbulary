using System;
using MonoTouch.Foundation;
using MonoTouch.UIKit;

using playn.ios;
using playn.core;
using com.github.donkirkby.vograbulary.core;

namespace com.github.donkirkby.vograbulary
{
  [Register ("AppDelegate")]
  public partial class AppDelegate : IOSApplicationDelegate {
    public override bool FinishedLaunching (UIApplication app, NSDictionary options) {
      app.SetStatusBarHidden(true, true);
      var pconfig = new IOSPlatform.Config();
      // use pconfig to customize iOS platform, if needed
      IOSPlatform.register(app, pconfig);
      PlayN.run(new VograbularyApp());
      return true;
    }
  }

  public class Application {
    static void Main (string[] args) {
      UIApplication.Main (args, null, "AppDelegate");
    }
  }
}
