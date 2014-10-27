package com.github.donkirkby.vograbulary.core.russian;

import java.util.ArrayList;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer.Event;
import playn.core.TextFormat;
import playn.core.TextLayout;
import playn.core.util.Callback;
import pythagoras.f.Point;
import tripleplay.util.Layers;

import com.github.donkirkby.vograbulary.core.VograbularyScreen;

public class TargetDisplay extends DragAdapter {
    private String text;
    private String originalText;
    private Puzzle puzzle;
    private Canvas canvas;
    private Image icon;
    private ImageLayer layer;
    private TargetDisplay opposite;
    private boolean isLeft;
    private Point point = new Point();
    private ArrayList<Float> otherLetterPositions = new ArrayList<Float>();
    private int currentTarget;
    private int wordIndex;
    private static final TextFormat TEXT_FORMAT =
            new TextFormat(VograbularyScreen.TITLE_FONT, true);
    private CanvasImage image;
    
    public TargetDisplay() {
        this.text = "";
        createLayer();
    }

    protected void createLayer() {
        icon = PlayN.assets().getImage("images/drag.png");
        image = PlayN.graphics().createImage(250, 80);
        canvas = image.canvas();
        layer = PlayN.graphics().createImageLayer(image);
        layer.addListener(this);
    }

    public void setOpposite(TargetDisplay opposite) {
        this.opposite = opposite;
        opposite.opposite = this;
        wordIndex = isLeft() ? 0 : 1;
        opposite.wordIndex = 1 - wordIndex;
    }
    public TargetDisplay getOpposite() {
        return opposite;
    }
    
    /**
     * Is this target to the left of the opposite target?
     */
    private boolean isLeft() {
        return calculateDifferenceBetweenTargetPositions() < 0;
    }

    public ImageLayer getLayer() {
        return layer;
    }
    
    public String getText() {
        return text;
    }
    private void setText(String text) {
        this.originalText = this.text = text;
        drawText();
    }
    
    @Override
    public void onPointerStart(Event event) {
        super.onPointerStart(event);

        if (opposite.isVisible()) {
            otherLetterPositions.clear();
            String text = opposite.originalText + originalText;
            isLeft = isLeft();
            float startPosition = 
                    isLeft
                    ? -calculateTextWidth(originalText)
                    : 0;
            for (int i=0; i <= opposite.originalText.length(); i++) {
                String portion = text.substring(0, i);
                float width = calculateTextWidth(portion);
                otherLetterPositions.add(startPosition + width);
            }
            otherLetterPositions.add(Float.MAX_VALUE);
            currentTarget =
                    isLeft
                    ? 0
                    : otherLetterPositions.size() - 1;
        }
    }
    
    @Override
    public void onPointerDrag(Event event) {
        // TODO: drag either word off either end, so code can be the same.
        // undragged word actually moves when it is detached
        super.onPointerDrag(event);
        float differenceBetweenTargetPositions = calculateDifferenceBetweenTargetPositions();
        float wordPosition = differenceBetweenTargetPositions - getShiftX();
        for (int target = 0; target < otherLetterPositions.size(); target++) {
            Float targetLeft = otherLetterPositions.get(target);
            if (wordPosition <= targetLeft) {
                if (target != currentTarget) {
                    if (target == otherLetterPositions.size() - 1) {
                        opposite.setVisible(true);
                        shiftX(otherLetterPositions.get(currentTarget) -
                                otherLetterPositions.get(0));
                        text = originalText;
                    }
                    else {
                        int targetOffset = isLeft ? -1 : 0;
                        if (currentTarget == otherLetterPositions.size() - 1) {
                            opposite.setVisible(false);
                            shiftX(otherLetterPositions.get(0) - 
                                    otherLetterPositions.get(target+targetOffset));
                        }
                        else {
                            if (currentTarget == 0) {
                                opposite.setVisible(false);
                            }
                            float currentBoundary = otherLetterPositions.get(
                                    Math.max(currentTarget + targetOffset, 0));
                            float newBoundary = otherLetterPositions.get(
                                    Math.max(target + targetOffset, 0));
                            shiftX(currentBoundary - newBoundary);
                        }
                        int splitIndex = target - (isLeft ? 1 : 0);
                        text = opposite.originalText.substring(0, splitIndex) +
                                originalText +
                                opposite.originalText.substring(splitIndex);
                        puzzle.setTargetCharacter(target);
                    }
                    currentTarget = target;
                    drawText();
                }
                break;
            }
        }
    }

    public void setVisible(boolean isVisible) {
        layer.setVisible(isVisible);
    }
    
    public boolean isVisible() {
        return layer.visible();
    }

    protected float calculateDifferenceBetweenTargetPositions() {
        point.set(0, 0);
        Layers.transform(point, layer, opposite.layer, opposite.point);
        return opposite.point.x;
    }

    protected void drawText() {
        canvas.clear();
        final TextLayout textLayout = PlayN.graphics().layoutText(
                text,
                TEXT_FORMAT);
        canvas.fillText(textLayout, 0, 0);
        if ( ! icon.isReady()) {
            layer.setImage(image.subImage(
                    0, 
                    0, 
                    textLayout.width(), 
                    textLayout.height()));
        }
        
        icon.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                canvas.drawImage(
                        icon, 
                        (textLayout.width() - icon.width())/2, 
                        textLayout.height());
                layer.setImage(image.subImage(
                        0,
                        0, 
                        textLayout.width(), 
                        textLayout.height() + icon.height()));
            }

            @Override
            public void onFailure(Throwable cause) {
                // Too bad, no icon for you.
            }
        });
    }
    
    protected float calculateTextWidth(String text) {
        return PlayN.graphics().layoutText(text, TEXT_FORMAT).width();
    }
    
    public Puzzle getPuzzle() {
        return puzzle;
    }
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        if (getShiftX() != 0) {
            shiftX(-getShiftX());
        }
        setText(puzzle.getTarget(wordIndex));
    }
}
