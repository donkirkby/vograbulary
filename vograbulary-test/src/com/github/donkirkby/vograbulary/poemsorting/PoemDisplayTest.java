package com.github.donkirkby.vograbulary.poemsorting;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class PoemDisplayTest {
    @Test
    public void display() {
        List<Poem> poems = Poem.load(
                "Guess I'm a poet,",
                "I didn't know it.");
        int width = 15;
        String expectedDisplay =
                "egssu i'm a  \n" +
                "    eopt,    \n" +
                "i ddin't know\n" +
                "    it.      \n" +
                "gudidnetmkaow\n" +
                "i esioit  n  \n" +
                "    pt       \n" +
                "    s        \n";
        PoemDisplay display = new PoemDisplay(poems.get(0), width);
        
        assertThat("display", buildTextDisplay(display), is(expectedDisplay));
    }
    
    @Test
    public void wrapTwice() {
        List<Poem> poems = Poem.load(
                "Guess I'm a poet,",
                "I didn't know it.");
        int width = 10;
        String expectedDisplay =
                "egssu i'm\n" +
                "    a    \n" +
                "    eopt,\n" +
                "i ddin't \n" +
                "    know \n" +
                "    it.  \n" +
                "gudianetm\n" +
                "i esdnit \n" +
                "    ioow \n" +
                "    kt   \n" +
                "    p    \n" +
                "    s    \n";
        PoemDisplay display = new PoemDisplay(poems.get(0), width);
        
        assertThat("display", buildTextDisplay(display), is(expectedDisplay));
    }
    
    @Test
    public void displayBlankColumn() {
        List<Poem> poems = Poem.load(
                "Guess I'm a poet,",
                "I did not know it.");
        int width = 20;
        String expectedDisplay =
                "egssu i'm a eopt, \n" +
                "i ddi not know it.\n" +
                "gudid iom anooeit \n" +
                "i ess n t k pw t  \n";
        PoemDisplay display = new PoemDisplay(poems.get(0), width);
        
        assertThat("display", buildTextDisplay(display), is(expectedDisplay));
    }
    
    private String buildTextDisplay(PoemDisplay display) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < display.getBodyLineCount(); i++) {
            for (int j = 0; j < display.getWidth(); j++) {
                builder.append(display.getBody(i, j));
            }
            builder.append('\n');
        }
        for (int i = 0; i < display.getClueLineCount(); i++) {
            for (int j = 0; j < display.getWidth(); j++) {
                builder.append(display.getClue(i, j));
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
