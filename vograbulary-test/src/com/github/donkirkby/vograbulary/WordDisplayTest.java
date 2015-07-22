package com.github.donkirkby.vograbulary;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.donkirkby.vograbulary.LetterDisplayTest.DummyLetterDisplayFactory;
import com.github.donkirkby.vograbulary.WordDisplay.WordDisplayListener;

public class WordDisplayTest implements WordDisplayListener {
    private DummyLetterDisplayFactory factory;
    private WordDisplay wordDisplay;
    private List<WordDisplay> clickedWords;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
        factory = new DummyLetterDisplayFactory();
        wordDisplay = new WordDisplay(factory);
        wordDisplay.addListener(this);
        clickedWords = new ArrayList<>();
    }

    @Override
    public void onClick(WordDisplay wordDisplay) {
        clickedWords.add(wordDisplay);
    }

    @Test
    public void setWord() {
        wordDisplay.setWord("PIE");
        
        assertThat("letter count", factory.getActive().size(), is(3));
    }
    
    @Test
    public void getLetters() {
        wordDisplay.setWord("PIE");
        
        List<LetterDisplay> letters = wordDisplay.getLetters();
        
        assertThat("letter count", letters.size(), is(3));
        assertThat("second letter", letters.get(1).getLetter(), is("I"));
        
        thrown.expect(UnsupportedOperationException.class);
        letters.add(new LetterDisplayTest.DummyLetterDisplay("X"));
    }
    
    @Test
    public void setWordTwice() {
        wordDisplay.setWord("PIE");
        wordDisplay.setWord("CAKE");
        
        assertThat("active letter count", factory.getActive().size(), is(4));
        assertThat("letter count", wordDisplay.getLetters().size(), is(4));
    }
    
    @Test
    public void setWordAfterRotation() {
        wordDisplay.setWord("PIE");
        wordDisplay.setRotation(Math.PI);
        wordDisplay.setWord("CAKE");
        
        assertThat("rotation", wordDisplay.getRotation(), closeTo(0, 0.0001));
    }
    
    @Test
    public void getWidth() {
        wordDisplay.setWord("ROPE");
        int width = wordDisplay.getWidth();
        
        assertThat("width", width, is(4*LetterDisplayTest.LETTER_WIDTH));
    }
    
    @Test
    public void setLeft() {
        wordDisplay.setWord("TO");
        
        wordDisplay.setLeft(10);
        
        assertThat("left 0", factory.getActive().get(0).getLeft(), is(10));
        assertThat(
                "left 1",
                factory.getActive().get(1).getLeft(),
                is(10 + LetterDisplayTest.LETTER_WIDTH));
    }
    
    @Test
    public void setTop() {
        wordDisplay.setWord("TO");
        
        wordDisplay.setTop(10);
        
        assertThat("top 0", factory.getActive().get(0).getTop(), is(10));
        assertThat("top 1", factory.getActive().get(1).getTop(), is(10));
    }
    
    @Test
    public void setRotation() {
        wordDisplay.setWord("TOTAL");
        wordDisplay.setLeft(0);
        wordDisplay.setTop(50);
        
        wordDisplay.setRotation((float) (Math.PI/2));
        
        assertThat(
                "centre x 0",
                factory.getActive().get(0).getCentreX(),
                is((int)(LetterDisplayTest.LETTER_WIDTH * 2.5)));
        assertThat(
                "centre y 2",
                factory.getActive().get(2).getCentreY(),
                is(50 + LetterDisplayTest.LETTER_HEIGHT/2));
        assertThat(
                "centre y 1",
                factory.getActive().get(1).getCentreY(),
                is(50 + LetterDisplayTest.LETTER_HEIGHT/2 - LetterDisplayTest.LETTER_WIDTH));
        assertThat("rotation", wordDisplay.getRotation(), closeTo(Math.PI/2, 0.0001));
    }
    
    @Test
    public void halfRotation() {
        wordDisplay.setWord("TOTAL");
        wordDisplay.setLeft(0);
        wordDisplay.setTop(50);
        
        wordDisplay.setRotation((float) (Math.PI));
        
        assertThat(
                "centre x 0",
                factory.getActive().get(0).getCentreX(),
                is((int)(LetterDisplayTest.LETTER_WIDTH * 4.5)));
        assertThat(
                "centre y 1",
                factory.getActive().get(1).getCentreY(),
                is(50 + LetterDisplayTest.LETTER_HEIGHT/2));
        assertThat(
                "centre y 2",
                factory.getActive().get(2).getCentreY(),
                is(50 + LetterDisplayTest.LETTER_HEIGHT/2));
        assertThat(
                "centre y 3",
                factory.getActive().get(3).getCentreY(),
                is(50 + LetterDisplayTest.LETTER_HEIGHT/2));
    }
    
    @Test
    public void getWord() {
        String expectedWord = "WORD";
        
        wordDisplay.setWord(expectedWord);
        String actualWord = wordDisplay.getWord();
        
        assertThat("word", actualWord, is(expectedWord));
    }
    
    @Test
    public void testToString() {
        wordDisplay.setWord("CONTENT");
        String string = wordDisplay.toString();
        
        assertThat("string representation", string, is("WordDisplay(\"CONTENT\")"));
    }
    
    @Test
    public void click() {
        wordDisplay.setWord("TO");
        factory.getActive().get(0).click();
        
        assertThat("clicked words", clickedWords, is(Arrays.asList(wordDisplay)));
    }
}
