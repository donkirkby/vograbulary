package com.github.donkirkby.vograbulary;

import static org.junit.Assert.*;

import java.util.Arrays;

public class Assert {
    /**
     * Assert that two strings contain the same set of letters, where order
     * doesn't matter.
     * @param message A description of the contents
     * @param expected The expected set of letters
     * @param actual The actual set of letters
     */
    public static void assertEquivalent(String message, String expected, String actual) {
        char[] expectedLetters = expected.toCharArray();
        char[] actualLetters = actual.toCharArray();
        Arrays.sort(expectedLetters);
        Arrays.sort(actualLetters);
        assertEquals(
                message, 
                new String(expectedLetters), 
                new String(actualLetters));
    }

}
