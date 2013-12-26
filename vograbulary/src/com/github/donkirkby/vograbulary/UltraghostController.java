package com.github.donkirkby.vograbulary;

import java.util.Random;

public class UltraghostController {
    private Random random = new Random();

    public String next() {
        int numLetters = 3;
        int alphabetSize = 26;
        StringBuilder builder = new StringBuilder(numLetters);
        for (int i = 0; i < numLetters; i++) {
            char j = (char) ('A' + random.nextInt(alphabetSize));
            builder.append(j);
        }
        return builder.toString();
    }

}
