Add new words to a student's vocabulary and make the words stick (grab them).

Word Challenges
===============

This app will eventually have several word challenges like anagrams, word ladders, and Quizl, but so far it only has the first two word challenges: Ultraghost and Russian Dolls.
Students will be able to complete these challenges alone or with a timer, against other students, or against an artificial intelligence.

Ultraghost
----------

On each turn, you are given a three letter puzzle, like CIP. You have to find a word that starts with the first letter, contains the middle letter, and ends with the last letter. For example, CRISP starts with C, contains I, and ends with P. Once you enter your answer, your opponent has a few seconds to enter a better one. An answer is better if it is shorter, or if it is the same length and comes earlier in the dictionary. For example CLIP is shorter than CRISP. The app will then give you a hint if there are any better words that you didn't think of. For example, CHIP is earlier than CLIP in the dictionary.

If you'd like a few practice puzzles, try GHS, ORG, EOS. Scroll down for my answers.

Russian Dolls
-------------

So far, this is a solitaire challenge based on an episode of [Ask Me Another][ama]. Each puzzle gives a clue with two words highlighted. One of those words gets put completely inside the other to make the solution word. The letters in each word don't get rearranged, one complete word gets nested inside the other like two Russian dolls. Here's an example puzzle:

>When you are *unable* to find *comfort*.

The two words are "unable" and "comfort". Scroll down for the answer.

When you think you've solved the puzzle, drag the pointer over the place where one word should be inserted into the other. Press the Solve button to check your answer. If you got it right, it will display the combined word. Press the Next button to see the next puzzle.

Answers
-------

My answers for the Ultraghost puzzles from good, to better, to best:
 * GHS - gushes, ghosts, gashes
 * ORG - ordering, offering, orating
 * EOS - errors, eons, egos﻿

The answer to the Russian Dolls puzzle is "uncomfortable", "comfort" nests inside "unable" to form "uncomfortable".

Running Vograbulary
===================

You can try the app in a few ways:
 * in [a browser][browser] (requires [WebGL][webgl])
 * as an Android app, if you join my [beta testers community][testers]
 * as a Java application: you can download the Java jar from my [releases page][releases] and then run it from a command line: `java -jar vograbulary.jar`

As well as the word challenges, the app will include tools to measure current vocabulary size by testing sample words in an exponential distribution from a word list sorted by usage frequency. For more accurate measurement, the testing will zoom in on the estimated upper limit of the student's vocabulary. 

Once approximate vocabulary size is known, it can be expanded by drilling new words with spaced-repetition flashcards. 

Vograbulary will include definitions for the words, brief readings from public domain works, plus links to the complete works for further reading.

Contributing
============

Vograbulary is built using Eclipse. Install the tools listed under [libgdx prerequisites][tools], clone this project from git, and then import the project files into Eclipse.

To edit the graphics files, edit the contents of vograbulary-test/assets-raw, and then run vograbulary-test/src/com.github.donkirkby.vograbulary.AssetPacker. To change the font, follow the instructions for running [Hiero][hiero]. Launch Hiero, open the vograbulary-test/assets-raw/default.hiero settings file, and then save the font file over vograbulary-android/assets/data/ui/default.fnt. That will also generate default.png, which you need to move back to vograbulary-test/assets-raw/skin/default.png. Finally, run the AssetPacker again.

If you have the tools for building iOS apps, I'm looking for someone to package the iOS version. It sounds like it's [supported by libgdx][iOS], but [not trivial][iOStrouble].

License
=======

Vograbulary is released under the MIT license.

[Hamcrest][hamcrest] is released under the BSD 3-Clause License.

[Mockito][mockito] is released under the MIT license, and [libgdx][libgdx] is released under the Apache 2.0 license.

[Natural Language Toolkit][nltk] is released by the NLTK project under the Apache 2.0 license.

Icons by [PixelKit][icons] are released under a [CC-BY license][cc].

[browser]: http://donkirkby.github.io/vograbulary/run/
[webgl]: http://get.webgl.org/
[testers]: https://plus.google.com/u/0/communities/103264778621024783530
[releases]: https://github.com/donkirkby/vograbulary/releases
[tools]: https://github.com/libgdx/libgdx/wiki/Project-setup%2C-running-%26-debugging
[iOS]: http://www.badlogicgames.com/wordpress/?p=3156
[iOStrouble]: https://github.com/libgdx/libgdx/wiki/Robovm-notes
[hamcrest]: http://hamcrest.org/JavaHamcrest/
[mockito]: https://code.google.com/p/mockito/
[libgdx]: http://libgdx.badlogicgames.com/
[nltk]: http://nltk.org/
[hiero]: https://github.com/libgdx/libgdx/wiki/Hiero
[icons]: https://www.iconfinder.com/PixelKit
[cc]: http://creativecommons.org/licenses/by/3.0/
[ama]: http://www.npr.org/2014/01/30/268462155/russian-dolls

