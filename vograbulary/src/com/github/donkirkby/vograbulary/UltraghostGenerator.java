package com.github.donkirkby.vograbulary;

public interface UltraghostGenerator {
    /**
     * Generate a new puzzle.
     * @return a string with three upper-case letters
     */
    String generate();
    
    /**
     * Load a list of words that can be used to generate puzzles.
     */
    void loadWordList(Iterable<String> wordList);
}
