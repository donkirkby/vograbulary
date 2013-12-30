package com.github.donkirkby.vograbulary;

import java.util.Random;

public class UltraghostDefaultGenerator implements UltraghostGenerator {
    private Random random;
    
    // Each entry holds the odds of that letter appearing in that position.
    private double[] startingOdds;
    private double[] interiorOdds;
    private double[] endingOdds;

    public UltraghostDefaultGenerator() {
        this(new Random());
    }

    public UltraghostDefaultGenerator(Random random) {
        this.random = random;
    }

    @Override
    public String generate() {
        StringBuilder builder = new StringBuilder(3);
        builder.append(generateLetter(startingOdds));
        builder.append(generateLetter(interiorOdds));
        builder.append(generateLetter(endingOdds));
        
        return builder.toString();
    }

    private char generateLetter(double[] letterOdds) {
        double remaining = random.nextDouble();
        for (int i = 0; i < letterOdds.length; i++) {
            remaining -= letterOdds[i];
            if (remaining < 0) {
                return (char) ('A' + i);
            }
        }
        // Should never get here.
        throw new RuntimeException("Invalid odds for letters.");
    }

    @Override
    public void loadWordList(Iterable<String> wordList) {
        int alphabetSize = 26;
        startingOdds = new double[alphabetSize];
        interiorOdds = new double[alphabetSize];
        endingOdds = new double[alphabetSize];
        int wordCount = 0;
        int interiorCount = 0;
        for (String word : wordList) {
            wordCount++;
            startingOdds[word.charAt(0) - 'A'] += 1;
            endingOdds[word.charAt(word.length()-1) - 'A'] += 1;
            for (int i = 1; i < word.length()-1; i++) {
                interiorCount++;
                interiorOdds[word.charAt(i) - 'A'] += 1;
            }
        }
        
        for (int i = 0; i < alphabetSize; i++) {
            startingOdds[i] /= wordCount;
            interiorOdds[i] /= interiorCount;
            endingOdds[i] /= wordCount;
        }
    }
}
