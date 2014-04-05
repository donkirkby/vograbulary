package com.github.donkirkby.vograbulary.ultraghost;

public class Puzzle {
    public static String NOT_SET = null;
    public static String NO_SOLUTION = "";
    
    private String letters;
    private String solution;
    private String response;
    private String hint;
    private Student owner;
    private WordList wordList;
    
    public Puzzle(String letters, Student owner, WordList wordList) {
        if (letters == null) {
            throw new IllegalArgumentException("Puzzle letters were null.");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Puzzle owner was null.");
        }
        if (wordList == null) {
            throw new IllegalArgumentException("Puzzle word list was null.");
        }
        this.letters = letters;
        this.owner = owner;
        this.wordList = wordList;
    }
    
    /**
     * The three letters that must be matched in a valid solution.
     */
    public String getLetters() {
        return letters;
    }
    
    /**
     * A solution to the puzzle. If it matches the three letters, then it's 
     * valid. Null means it hasn't been set yet, and empty string means it
     * has been skipped.
     */
    public String getSolution() {
        return solution;
    }
    public void setSolution(String solution) {
        this.solution = solution;
    }
    
    /**
     * Another solution that tries to improve on the original solution by being
     * shorter or the same length and earlier in the dictionary. Null means
     * it hasn't been set yet, and empty string means that no response is 
     * wanted.
     */
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    
    /**
     * Another solution that could have been used, if any exists.
     */
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    
    /**
     * The result of this puzzle, including the change in score. It will be
     * UKNOWN until a solution and response have been entered.
     */
    public WordResult getResult() {
        if (solution == NOT_SET) {
            return WordResult.UNKNOWN;
        }
        if (response == NOT_SET) {
            return wordList.checkSolution(letters, solution);
        }
        return wordList.checkResponse(letters, solution, response);
    }
    
    /**
     * The student who was assigned this puzzle. Any score will be given to
     * that student.
     */
    public Student getOwner() {
        return owner;
    }
    
    @Override
    public String toString() {
        return String.format("Puzzle(%1$s, %2$s)", letters, owner.getName());
    }
}
