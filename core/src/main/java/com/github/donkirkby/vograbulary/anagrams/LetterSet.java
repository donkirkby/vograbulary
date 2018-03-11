package com.github.donkirkby.vograbulary.anagrams;

public class LetterSet {
    private char[] mDeck;
    private int mNextTileToShow;
    private int mNextTileToHide;
    private int mNextTileToReserve;

    public LetterSet(String letters) {
        mDeck = letters.toCharArray();
    }

    /**
     * Show the next letter from the set.
     * @return the letter
     */
    public char showNextLetter() {
        return mDeck[mNextTileToShow++];
    }

    /**
     * Get the list of currently visible letters. Letters are added to this
     * list by calling showLetter() and removed by calling removeLetter().
     * @return the letters
     */
    public String getVisibleLetters() {
        return new String(
                mDeck, 
                mNextTileToHide, 
                mNextTileToShow-mNextTileToHide);
    }

    /**
     * Reserve a letter to be hidden by a call to hideReservedLetters().
     * @param letter The letter to reserve.
     * @return true if the letter was successfully reserved, false if none
     * of the unreserved, visible letters matched letter.
     */
    public boolean reserveLetter(char letter) {
        boolean isFound = false;
        for (int j = mNextTileToReserve; 
                j < mNextTileToShow && ! isFound; 
                j++) {
            isFound = letter == mDeck[j];
            if (isFound) {
                mDeck[j] = mDeck[mNextTileToReserve];
                mDeck[mNextTileToReserve++] = letter;
            }
        }
        return isFound;
    }

    /**
     * Hide all letters from the set that were previously reserved.
     */
    public void hideReservedLetters() {
        mNextTileToHide = mNextTileToReserve;
    }

    /**
     * Release any reserved letters so that they can be reserved again.
     */
    public void releaseReservedLetters() {
        mNextTileToReserve = mNextTileToHide;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mDeck.length; i++) {
            if (i == 0 && 0 != mNextTileToHide)
            {
                builder.append('[');
            }
            if (i == mNextTileToHide) {
                if (0 != mNextTileToHide) {
                    builder.append(']');
                }
                if (mNextTileToHide != mNextTileToReserve) {
                    builder.append('(');
                }
            }
            if (i == mNextTileToReserve && mNextTileToHide != mNextTileToReserve) {
                builder.append(')');
            }
            if (i == mNextTileToShow) {
                builder.append('{');
            }
            builder.append(mDeck[i]);
        }
        if (mNextTileToShow != mDeck.length) {
            builder.append('}');
        }
        return builder.toString();
    }

    /**
     * Show any letters that have not been shown yet.
     */
    public void showAllRemaining() {
        mNextTileToShow = mDeck.length;
    }

    /**
     * Count how many letters have not been shown yet.
     * @return the count.
     */
    public int getRemainingCount() {
        return mDeck.length - mNextTileToShow;
    }
}