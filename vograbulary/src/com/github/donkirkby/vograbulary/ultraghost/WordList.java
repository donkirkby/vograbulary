package com.github.donkirkby.vograbulary.ultraghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class WordList implements Iterable<String> {
    private ArrayList<String> wordList = new ArrayList<String>();

    /** Read all the words from a reader and add them to the list.
     * 
     * @param reader an open reader. This method will close it.
     */
    public void read(Reader reader) {
        read(new BufferedReader(reader));
    }

    /** Read all the words from a reader and add them to the list.
     * 
     * @param reader an open reader. This method will close it.
     */
    public void read(BufferedReader lineReader) {
        try {
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
        if (solution == null || solution.length() == 0) {
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
            return solution == null || solution.length() == 0
                    ? WordResult.SKIPPED
                    : WordResult.NOT_IMPROVED;
        }
        String challengeUpper = challenge.toUpperCase();
        if ( ! wordList.contains(challengeUpper)) {
            return solution == null || solution.length() == 0
                    ? WordResult.IMPROVED_SKIP_NOT_A_WORD
                    : WordResult.IMPROVEMENT_NOT_A_WORD;
        }
        if (solution == null || solution.length() == 0) {
            return WordResult.WORD_FOUND;
        }
        return challengeWord(solution.toUpperCase(), challengeUpper);
    }

    /**
     * Compare a new word with the current best solution. Both words must
     * be all in upper case.
     */
    public WordResult challengeWord(String solution, String challenge) {
        return challenge.length() > solution.length()
                ? WordResult.LONGER
                : challenge.length() == solution.length()
                && challenge.compareTo(solution) > 0
                ? WordResult.LATER
                : challenge.length() < solution.length()
                ? WordResult.SHORTER
                : challenge.equals(solution)
                ? WordResult.NOT_IMPROVED
                : WordResult.EARLIER;
    }

    @Override
    public Iterator<String> iterator() {
        return wordList.iterator();
    }

    public int size() {
        return wordList.size();
    }
    
}
