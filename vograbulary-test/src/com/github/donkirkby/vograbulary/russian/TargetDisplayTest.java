package com.github.donkirkby.vograbulary.russian;

import static com.github.donkirkby.vograbulary.russian.LetterDisplayTest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TargetDisplayTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private class DummyTargetDisplay extends TargetDisplay {
        private int dragX;
        private boolean isDragVisible = true;
        private boolean isInsertVisible = true;
        
        public DummyTargetDisplay(
                LetterDisplayFactory factory) {
            super(factory);
        }
        
        @Override
        public int getDragX() {
            return dragX;
        }
        
        @Override
        public void setDragX(int dragX) {
            this.dragX = dragX;
        }
        
        @Override
        public boolean isDragVisible() {
            return isDragVisible;
        }

        @Override
        public void setDragVisible(boolean isDragVisible) {
            this.isDragVisible = isDragVisible;
        }
        
        @Override
        public boolean isInsertVisible() {
            return isInsertVisible;
        }
        
        @Override
        public void setInsertVisible(boolean isInsertVisible) {
            this.isInsertVisible = isInsertVisible;
        }
    }

    private class DummyLetterDisplayFactory extends LetterDisplayFactory {
        private List<DummyLetterDisplay> active = new ArrayList<>();
        
        @Override
        public LetterDisplay create(String letter) {
            LetterDisplayTest.DummyLetterDisplay display =
                    new LetterDisplayTest.DummyLetterDisplay(letter);
            active.add(display);
            return display;
        }

        @Override
        public void destroy(LetterDisplay letter) {
            active.remove(letter);
        }
        
        public List<DummyLetterDisplay> getActive() {
            return active;
        }
    }
    private TargetDisplay leftDisplay;
    private TargetDisplay rightDisplay;
    private List<LetterDisplay> leftLetters;
    private List<LetterDisplay> rightLetters;
    private DummyLetterDisplayFactory factory;
    private Puzzle puzzle;
    
    @Before
    public void setUp() {
        // Starting layout:
        // 0   20      45      65   90
        // LEFT                RIGHT
        factory = new DummyLetterDisplayFactory();
        leftDisplay = new DummyTargetDisplay(factory);
        rightDisplay = new DummyTargetDisplay(factory);
        leftDisplay.setOther(rightDisplay);
        puzzle = new Puzzle("LEFT RIGHT");
        leftDisplay.setPuzzle(puzzle);
        leftDisplay.setScreenWidth(90);
        leftDisplay.layout();
        leftLetters = leftDisplay.getLetters();
        rightLetters = rightDisplay.getLetters();
    }
    
    @Test
    public void leftLetterPositions() {
        assertThat("letter count", leftLetters.size(), is(4));
        assertThat("first letter", leftLetters.get(0).getLetter(), is("L"));
        assertThat("first x", leftLetters.get(0).getLeft(), is(0));
        assertThat("second letter", leftLetters.get(1).getLetter(), is("E"));
        assertThat("second x", leftLetters.get(1).getLeft(), is(LETTER_WIDTH));
    }
    
    @Test
    public void rightLetterPositions() {
        assertThat("first x", rightLetters.get(0).getLeft(), is(65));
        assertThat("right width", rightDisplay.getLettersWidth(), is(25));
    }
    
    @Test
    public void setPuzzle() {
        leftDisplay.setDragVisible(true);
        rightDisplay.setDragVisible(true);
        leftDisplay.setInsertVisible(false);

        leftDisplay.setPuzzle(new Puzzle("SINISTER SOUND"));
        
        assertThat("left drag visible", leftDisplay.isDragVisible(), is(false));
        assertThat("right drag visible", rightDisplay.isDragVisible(), is(false));
        assertThat("insert visible", leftDisplay.isInsertVisible(), is(true));
        assertThat("letter count", leftLetters.size(), is(8));
        assertThat(
                "active count",
                factory.getActive().size(),
                is(leftLetters.size() + rightLetters.size()));
    }
    
    @Test
    public void lettersReadOnly() {
        thrown.expect(UnsupportedOperationException.class);
        leftLetters.clear();
    }
    
    @Test
    public void layout() {
        int screenWidth = 200;
        int expectedLeft = screenWidth/2 - 25 - 20;
        int expectedRight = screenWidth/2 + 20 + 25;
        
        leftDisplay.setScreenWidth(screenWidth);
        rightDisplay.setScreenWidth(screenWidth);
        leftDisplay.layout(); // lays out both
        
        assertThat("left", leftDisplay.getLettersLeft(), is(expectedLeft));
        assertThat("right", rightDisplay.getLettersRight(), is(expectedRight));
    }
    
    @Test
    public void drag() {
        int startX = 10;
        int dragX = 25;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("dragButton.x", leftDisplay.getDragX(), is(dragX - startX));
        assertThat(
                "first letter x",
                leftLetters.get(0).getLeft(),
                is(dragX - startX));
        assertThat(
                "second letter x",
                leftLetters.get(1).getLeft(),
                is(dragX - startX + LETTER_WIDTH));
        assertThat(
                "right word letter x",
                rightLetters.get(0).getLeft(),
                is(65));
        assertThat("insert is visible", leftDisplay.isInsertVisible(), is(false));
    }
    
    @Test
    public void dragRight() {
        int startX = 100;
        int dragX = 75;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "left word letter x",
                leftLetters.get(0).getLeft(),
                is(0));
    }
    
    @Test
    public void dragTwice() {
        // The drag position is always the pointer position relative to the
        // text field.
        int startX = 10;
        int dragX1 = 25;
        int dragX2 = 23;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX1);
        leftDisplay.drag(dragX2);
        
        assertThat(
                "target.x",
                leftLetters.get(0).getLeft(),
                is(dragX1 - startX + dragX2 - startX));
    }
    
    @Test
    public void dragPast() {
        // The drag position is always the pointer position relative to the
        // text field.
        int startX = 0;
        int dragX1 = 110;
        int dragX2 = 40;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX1);
        leftDisplay.dragStop();
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX2);
        
        assertThat(
                "right drag visible",
                rightDisplay.isDragVisible(),
                is(false));
    }
    
    @Test
    public void hideRight() {
        // Visible boundary should be right at zero.
        int startX = 0;
        int dragX = 1;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat(rightDisplay.isDragVisible(), is(false));
    }
    
    @Test
    public void showRight() {
        // Visible boundary should be right at zero.
        int startX = 0;
        int dragX = -1;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat(rightDisplay.isDragVisible(), is(true));
    }
    
    @Test
    public void hideLeft() {
        // Visible boundary should be right at 65.
        int startX = 65;
        int dragX = 64;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(leftDisplay.isDragVisible(), is(false));
    }
    
    @Test
    public void hideThenShow() {
        int startX = 0;
        int drag1X = 1;
        int drag2X = -1;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(drag1X);
        assertThat(rightDisplay.isDragVisible(), is(false));

        leftDisplay.dragStart(drag1X);
        leftDisplay.drag(drag2X);
        assertThat(rightDisplay.isDragVisible(), is(true));
    }
    
    @Test
    public void pushRight() {
        int startX = 0;
        int dragX = 69-20; // starts to push label, but doesn't split yet
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat("target position", rightLetters.get(0).getLeft(), is(69));
        assertThat("target set", puzzle.isTargetSet(), is(false));
    }
    
    @Test
    public void pushLeft() {
        int startX = 65;
        // starts to push label, but doesn't split yet
        int dragX = -4+20;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "target position",
                leftLetters.get(3).getLeft(),
                is(dragX - LETTER_WIDTH));
    }
    
    @Test
    public void dragLeftPushLeft() {
        int startX = 65;
        // starts to push label, but doesn't split yet
        int dragX = -4+20;
        
        leftDisplay.dragStart(0);
        leftDisplay.drag(-1);
        leftDisplay.dragStop();
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "target position",
                leftLetters.get(3).getLeft(),
                is(dragX - LETTER_WIDTH));
    }
    
    @Test
    public void dragLeftResetPushLeft() {
        int startX = 65;
        // starts to push label, but doesn't split yet
        int dragX = -4+20;
        
        leftDisplay.dragStart(0);
        leftDisplay.drag(1);
        leftDisplay.dragStop();
        leftDisplay.setPuzzle(new Puzzle("LENT SIGHT"));
        leftDisplay.layout();
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "target position",
                leftLetters.get(3).getLeft(),
                is(dragX - LETTER_WIDTH));
    }
    
    @Test
    public void splitRight() {
        int startX = 0;
        int dragX = 70-20; // split first letter
        int expectedRightTailX = 70; // start of right word
        int expectedRightHeadX = dragX - LETTER_WIDTH;
        int expectedDragX = dragX; // drag button position
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX);
        
        assertThat(
                "right tail position",
                rightLetters.get(1).getLeft(),
                is(expectedRightTailX));
        assertThat(
                "right head position",
                rightLetters.get(0).getLeft(),
                is(expectedRightHeadX));
        assertThat("drag position", leftDisplay.getDragX(), is(expectedDragX));
        assertThat("target character", puzzle.getTargetCharacter(), is(1));
        assertThat("target word", puzzle.getTargetWord(), is(1));
    }

    @Test
    public void splitLeft() {
        int startX = 65;
        int dragX = 15; // split first letter
        int expectedDragX = dragX; // drag button position
        int expectedLeftTailX = dragX;
        int expectedLeftHeadX = dragX + 25;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX);
        
        assertThat(
                "left tail position",
                leftLetters.get(2).getRight(),
                is(expectedLeftTailX));
        assertThat(
                "left head position",
                leftLetters.get(3).getLeft(),
                is(expectedLeftHeadX));
        assertThat("drag position", rightDisplay.getDragX(), is(expectedDragX));
        assertThat("target character", puzzle.getTargetCharacter(), is(3));
        assertThat("target word", puzzle.getTargetWord(), is(0));
    }
    
    @Test
    public void splitRightInTwoSteps() {
        int startX = 0;
        int dragX1 = 69-20; // push
        int dragX2 = 1; // split first letter
        int expectedRightTailX = 70; // start of right word
        int expectedRightHeadX = dragX1 + dragX2 - LETTER_WIDTH;
        
        leftDisplay.dragStart(startX);
        leftDisplay.drag(dragX1);
        leftDisplay.drag(dragX2);
        
        assertThat(
                "right tail position",
                rightLetters.get(1).getLeft(),
                is(expectedRightTailX));
        assertThat(
                "right head position",
                rightLetters.get(0).getLeft(),
                is(expectedRightHeadX));
    }

    @Test
    public void splitLeftInTwoSteps() {
        int startX = 65;
        int dragX1 = 16; // push
        int dragX2 = 64; // split first letter
        int expectedLeftTailX = 15;
        int expectedLeftHeadX = 15 + 25;
        
        rightDisplay.dragStart(startX);
        rightDisplay.drag(dragX1);
        rightDisplay.drag(dragX2);
        
        assertThat(
                "left tail position",
                leftLetters.get(2).getRight(),
                is(expectedLeftTailX));
        assertThat(
                "left head position",
                leftLetters.get(3).getLeft(),
                is(expectedLeftHeadX));
    }
    
    @Test
    public void getText() {
        String text = rightDisplay.getText();
        
        assertThat("text", text, is("RIGHT"));
    }
}
