package com.github.donkirkby.vograbulary.ultraghost;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    ControllerTest.class,
    UltraghostRandomTest.class,
    WordListChallengeTest.class,
    WordListMatchTest.class,
    WordListSolutionTest.class,
    WordListTest.class
})
public class UltraghostSuite { }
