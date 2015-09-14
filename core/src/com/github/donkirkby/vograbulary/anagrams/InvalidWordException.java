package com.github.donkirkby.vograbulary.anagrams;

public class InvalidWordException extends Exception {
    private static final long serialVersionUID = -7256986530273121690L;

    /**
     * Initialize the object.
     * @param message The message describing why the word was invalid.
     */
    public InvalidWordException(String message) {
        super(message);
    }

}
