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
     * @param letters three capital letters.
     * @param word all capital letters
     * @return true if the word matches the puzzle.
     */
    public boolean isMatch(String letters, String word) {
        if (word.charAt(word.length()-1) != letters.charAt(2)) {
            return false;
        }
        if (word.charAt(0) != letters.charAt(0)) {
            return false;
        }
        int foundAt = word.indexOf(letters.charAt(1), 1);
        return 0 < foundAt && foundAt < word.length() - 1;
    }

    /**
     * Check to see if a word is in the word list and a match for the puzzle
     * letters.
     * @param letters three capital letters that the solution must match.
     * @param solution a word to evaluate, case doesn't matter
     * @return VALID if the solution is valid, otherwise the reason the solution
     * was rejected.
     */
    public WordResult checkSolution(String letters, String solution) {
        if (solution == null || solution.length() == 0) {
            return WordResult.SKIPPED;
        }
        String solutionUpper = solution.toUpperCase();
        if ( ! wordList.contains(solutionUpper)) {
            return WordResult.NOT_A_WORD;
        }
        return isMatch(letters, solutionUpper)
                ? WordResult.VALID 
                : WordResult.NOT_A_MATCH;
    }

    /**
     * Compare a solution and response.
     * @param letters the puzzle letters to match
     * @param solution a valid solution to the puzzle, or null, or blank.
     * @param response an attempt to improve on the solution, case doesn't 
     * matter
     * @return the result of comparing the two solutions
     */
    public WordResult checkResponse(
            String letters, 
            String solution,
            String response) {
        if (response == null || response.length() == 0) {
            return solution == null || solution.length() == 0
                    ? WordResult.SKIPPED
                    : WordResult.NOT_IMPROVED;
        }
        String challengeUpper = response.toUpperCase();
        if ( ! isMatch(letters, challengeUpper)) {
            return solution == null || solution.length() == 0
                    ? WordResult.IMPROVED_SKIP_NOT_A_MATCH
                    : WordResult.IMPROVEMENT_NOT_A_MATCH;
        }
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
     * be valid solutions all in upper case.
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

    /**
     * Find the most popular letter that beats both the solution and the 
     * response.
     * @param letters the three letters that valid solutions must match
     * @param solution one possible solution that may be invalid
     * @param response another possible solution that may be invalid
     * @return a valid solution that beats both, or null if none found
     */
    public String findNextBetter(
            String letters, 
            String solution,
            String response) {
        
        String solutionUpper = 
                checkSolution(letters, solution) != WordResult.VALID 
                ? "" 
                : solution.toUpperCase();
        String responseUpper = 
                checkSolution(letters, response) != WordResult.VALID 
                ? "" 
                : response.toUpperCase();
        String bestSoFar = 
                isImproved(solutionUpper, responseUpper) 
                ? responseUpper 
                : solutionUpper;
        for (String word : wordList) {
            if (isMatch(letters, word) && isImproved(bestSoFar, word)) {
                return word;
            }
        }
        return null; // no improvement found.
    }

    /**
     * Compare two solutions to see if the second one actually improves on
     * the first one.
     * @param solution must be blank or all in upper case
     * @param response
     * @return
     */
    private boolean isImproved(String solution, String response) {
        if (response.length() == 0) {
            return false;
        }
        if (solution.length() == 0) {
            return true;
        }
        int maxScore = WordResult.NOT_IMPROVED.getScore();
        return challengeWord(solution, response).getScore() < maxScore;
    }
}
