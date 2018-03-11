package com.github.donkirkby.vograbulary;

public abstract class LetterDisplayFactory {
    public abstract LetterDisplay create(String letter);
    
    public abstract void destroy(LetterDisplay letter);
}
