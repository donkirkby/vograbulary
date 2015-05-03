Contributing
============
If you like Vograbulary and want to make it better, help out. It could be as
simple as sending Don a nice note on [Google+][g+], you could report a bug,
or pitch in with some development work. Would you like to join the
[testers group][testers]? Anyone with some visual design skills want to make
this thing look amazing? I'm also looking for an iOS collaborator.

Bug Reports and Enhancement Requests
------------------------------------
Be as specific as possible. Which version are you using? What did you do? What
did you expect to happen? Are you planning to submit your own fix in a pull
request?

New Word Challenges
-------------------
Do you have ideas for other word challenges to include in Vograbulary? Let me
know how it would work.

Development
-----------
Vograbulary is built using Eclipse. The HTML version is built with
[Google Web Toolkit (GWT)][gwt], and the Android version is built with the
[Android Developer Tools][adt]. If you want to contribute to the development,
you should install one or both of these. Clone this project from git, and then
import the project files into Eclipse.

If you build iOS apps, I'm looking for someone to build a presentation layer for
that platform. Hopefully, we can use the core logic unchanged with the
[J2ObjC converter][j2objc].

Building a Release
------------------
Each release is tagged and published in three places: the web page, the Android
app on Google Play (still in private beta), and the Android APK file on the
releases page.

Before publishing a release, check the following:

* The unit tests not only pass, but cover the code enough to keep Jester happy.
    Use the `jester.sh` script in the vograbulary-test project.
* All the instructions are up to date in the README and CONTRIBUTING files.
* The version numbers have been incremented in the Android manifest file.

To publish a release:

* Right click the vograbulary-html project, and choose Google: GWT Compile. That
    takes several minutes, and sometimes complains about some classes or methods
    that aren't supported by the GWT compiler.
* If the compile is successful, run the `deploy.sh` script in the
    vograbulary-html project to copy all the files to the web site project. If
    you checked out the master branch as `~/git/vograbulary`, then the script
    assumes that you have checked out the `gh-pages` branch as
    `~/git/vograbulary-gh-pages`.
* Commit the `gh-pages` branch and check that the new version works on the web
    site.
* Check if the `master` branch has anything to commit. It usually doesn't.
* Open the Android manifest file, scroll down, and click on the Export Wizard
    link.
* Go to the [Google Play page][google], and upload the new version's APK file.
    Also check that the description is up to date, particularly the list of word
    challenges.
* If you need to update screenshots, you can either take a screenshot on the
    device by pressing power and volume down at the same time, or you can take
    a screenshot from the Android emulator. In Eclipse, choose Window: Show
    View: Other: Android: Devices. Select the emulator, then click the camera
    icon.
* Go to the GitHub [releases page][releases], create a new release using a tag
    like `v0.x.0-alpha`. Attach the APK file.

[g+]: http://google.com/+donkirkby
[testers]: https://plus.google.com/communities/103264778621024783530
[gwt]: http://www.gwtproject.org/
[adt]: https://developer.android.com/tools/help/adt.html
[j2objc]: http://j2objc.org/
[releases]: https://github.com/donkirkby/vograbulary/releases
[google]: https://play.google.com/apps/publish
