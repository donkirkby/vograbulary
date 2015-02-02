package com.github.donkirkby.vograbulary.russian;

/** Track the positions of the two target words, and the insert button.
 * 
 * Calculate which word and character is the insertion target. Assumes
 * monospace font in the target word displays.
 *
 */
public class PuzzleDisplay {
    private Puzzle puzzle;
    
    private class TargetEntry {
        public int left;
        public int width;
    }
    private TargetEntry[] targetEntries = new TargetEntry[] {
            new TargetEntry(),
            new TargetEntry()
    };
    
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }
    public Puzzle getPuzzle() {
        return puzzle;
    }
    
    public void setTargetPositions(
            int word1Left,
            int word1Width,
            int word2Left,
            int word2Width) {
        targetEntries[0].left = word1Left;
        targetEntries[0].width = word1Width;
        targetEntries[1].left = word2Left;
        targetEntries[1].width = word2Width;
    }
    public void calculateInsertion(int insertX) {
        puzzle.clearTargets();
        calculateInsertionForWord(insertX, 0);
        calculateInsertionForWord(insertX, 1);
    }
    private void calculateInsertionForWord(int insertX, int wordIndex) {
        if (puzzle.getTargetWord() >= 0) {
            return; // already found target;
        }
        TargetEntry entry = targetEntries[wordIndex];
        if (insertX < entry.left || entry.left + entry.width < insertX) {
            return;
        }
        puzzle.setTargetWord(wordIndex);
        int wordPixel = insertX - entry.left;
        int wordLength = puzzle.getTarget(wordIndex).length();
        int wordWidth = entry.width;
        int targetCharacter = (wordPixel*wordLength + wordWidth/2) / wordWidth;
        if (puzzle.isValidTargetCharacter(targetCharacter)) {
            puzzle.setTargetCharacter(targetCharacter);
        }
    }
}
