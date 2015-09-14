package com.github.donkirkby.vograbulary.anagrams;

import static com.github.donkirkby.vograbulary.Assert.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class LetterSetTest {
    @Test
    public void showNextLetter()
    {
        // SETUP
        LetterSet letters = new LetterSet("ABCD");
        
        // EXEC
        char letter1 = letters.showNextLetter();
        char letter2 = letters.showNextLetter();
        String visibleLetters = letters.getVisibleLetters();
        
        // VERIFY
        assertEquals("letter 1", 'A', letter1);
        assertEquals("letter 2", 'B', letter2);
        assertEquivalent("visible letters", "AB", visibleLetters);
    }
    
    public void testShowAll()
    {
        // SETUP
        LetterSet letters = new LetterSet("ABCD");
        
        // EXEC
        letters.showAllRemaining();
        String visibleLetters = letters.getVisibleLetters();
        
        // VERIFY
        assertEquivalent("visible letters", "ABCD", visibleLetters);
    }
    
    public void testHideReservedLetters()
    {
        // SETUP
        LetterSet letters = new LetterSet("ABCD");
        
        // EXEC
        letters.showNextLetter(); // A
        letters.showNextLetter(); // B
        boolean isSuccess = letters.reserveLetter('B');
        String visibleBeforeHide = letters.getVisibleLetters();
        letters.hideReservedLetters();
        String visibleAfterHide = letters.getVisibleLetters();
        
        // VERIFY
        assertTrue("success", isSuccess);
        assertEquivalent("visible before hide", "AB", visibleBeforeHide);
        assertEquivalent("visible after hide", "A", visibleAfterHide);
    }
    
    public void testReserveTwice()
    {
        // SETUP
        LetterSet letters = new LetterSet("ABCD");
        
        // EXEC
        letters.showNextLetter(); // A
        letters.showNextLetter(); // B
        letters.reserveLetter('B');
        boolean isSuccess = letters.reserveLetter('B');
        
        // VERIFY
        assertFalse("success", isSuccess);
    }
    
    public void testReleaseReservedLetters()
    {
        // SETUP
        LetterSet letters = new LetterSet("ABCD");
        
        // EXEC
        letters.showNextLetter(); // A
        letters.showNextLetter(); // B
        letters.reserveLetter('B');
        String visibleBeforeRelease = letters.getVisibleLetters();
        letters.releaseReservedLetters();
        String visibleAfterRelease = letters.getVisibleLetters();
        boolean isSuccess = letters.reserveLetter('B');
        
        // VERIFY
        assertTrue("success", isSuccess);
        assertEquivalent("visible before release", "AB", visibleBeforeRelease);
        assertEquivalent("visible after release", "AB", visibleAfterRelease);
    }
    
    public void testToString()
    {
        // SETUP
        LetterSet letters = new LetterSet("ABCD");
        
        // EXEC
        String start = letters.toString();
        letters.showNextLetter();
        String visible = letters.toString();
        letters.reserveLetter('A');
        String reserved = letters.toString();
        letters.hideReservedLetters();
        String hidden = letters.toString();
        letters.showNextLetter();
        letters.showNextLetter();
        letters.reserveLetter('B');
        String mixed = letters.toString();
        letters.showNextLetter();
        String noMore = letters.toString();
        
        // VERIFY
        assertEquals("start", "{ABCD}", start);
        assertEquals("visible", "A{BCD}", visible);
        assertEquals("reserved", "(A){BCD}", reserved);
        assertEquals("hidden", "[A]{BCD}", hidden);
        assertEquals("mixed", "[A](B)C{D}", mixed);
        assertEquals("no more", "[A](B)CD", noMore);
    }
}
