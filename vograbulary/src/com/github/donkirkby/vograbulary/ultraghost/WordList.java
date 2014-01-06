package com.github.donkirkby.vograbulary.ultraghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class WordList implements Iterable<String> {
    private ArrayList<String> wordList = new ArrayList<String>();

    public void read(Reader reader) {
        try {
            BufferedReader lineReader = new BufferedReader(reader);
            try {
                String line;
                while ((line = lineReader.readLine()) != null) {
                    if (line.length() > 3) {
                        wordList.add(line.toUpperCase());
                    }
                }
            } 
            finally {
                lineReader.close();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Reading word list failed.", e);
        }
    }
    
    /**
     * Check if a word is a solution to the puzzle, but don't check if it is
     * in the word list.
     * @param puzzle three capital letters.
     * @param word all capital letters
     * @return true if the word matches the puzzle.
     */
    public boolean isMatch(String puzzle, String word) {
        if (word.charAt(word.length()-1) != puzzle.charAt(2)) {
            return false;
        }
        if (word.charAt(0) != puzzle.charAt(0)) {
            return false;
        }
        int foundAt = word.indexOf(puzzle.charAt(1), 1);
        return 0 < foundAt && foundAt < word.length() - 1;
    }

    
    public WordResult checkSolution(String puzzle, String solution) {
        if (solution.length() == 0) {
            return WordResult.SKIPPED;
        }
        String solutionUpper = solution.toUpperCase();
        if ( ! wordList.contains(solutionUpper)) {
            return WordResult.NOT_A_WORD;
        }
        return isMatch(puzzle, solutionUpper)
                ? WordResult.VALID 
                : WordResult.NOT_A_MATCH;
    }

    public WordResult checkChallenge(
            String puzzle, 
            String solution,
            String challenge) {
        if (challenge == null || challenge.length() == 0) {
            return WordResult.NOT_IMPROVED;
        }
        String challengeUpper = challenge.toUpperCase();
        if ( ! wordList.contains(challengeUpper)) {
            return solution.length() == 0
                    ? WordResult.CHALLENGED_SKIP_NOT_A_WORD
                    : WordResult.CHALLENGE_NOT_A_WORD;
        }
        String solutionUpper = solution.toUpperCase();
        return solution.length() == 0
                ? WordResult.IMPROVED
                : challenge.length() > solution.length()
                ? WordResult.LONGER
                : challenge.length() == solution.length()
                && challengeUpper.compareTo(solutionUpper) > 0
                ? WordResult.LATER
                : challengeUpper.equals(solutionUpper)
                ? WordResult.NOT_IMPROVED
                : WordResult.IMPROVED;
    }

    @Override
    public Iterator<String> iterator() {
        return wordList.iterator();
    }
    
}
