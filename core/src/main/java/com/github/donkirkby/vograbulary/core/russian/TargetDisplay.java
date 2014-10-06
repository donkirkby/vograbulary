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
    private Canvas canvas;
    private Image icon;
    private ImageLayer layer;
    private TargetDisplay leftSide = this;
    private TargetDisplay rightSide = this;
    private Point leftPoint = new Point();
    private Point rightPoint = new Point();
    private ArrayList<Float> otherLetterPositions = new ArrayList<Float>();
    private int splitIndex;
    private static final TextFormat TEXT_FORMAT =
            new TextFormat(VograbularyScreen.TITLE_FONT, true);

    public TargetDisplay() {
        this.text = "";
        createLayer();
    }

    protected void createLayer() {
        icon = PlayN.assets().getImage("images/drag.png");
        CanvasImage image = PlayN.graphics().createImage(250, 80);
        canvas = image.canvas();
        layer = PlayN.graphics().createImageLayer(image);
        layer.addListener(this);
    }
    
    public TargetDisplay withLeftSide(TargetDisplay leftSide) {
        this.leftSide = leftSide;
        leftSide.rightSide = this;
        return this;
    }
    
    public ImageLayer getLayer() {
        return layer;
    }
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.originalText = this.text = text;
        drawText();
    }
    
    @Override
    public void onPointerStart(Event event) {
        super.onPointerStart(event);

        TargetDisplay opposite;
        if (this == rightSide) {
            opposite = leftSide;
            if (opposite.isVisible()) {
                String text = opposite.originalText;
                for (int i=0; i <= text.length(); i++) {
                    String portion = text.substring(i);
                    float width = calculateTextWidth(portion);
                    otherLetterPositions.add(getLeftSideWidth() - width);
                }
                splitIndex = opposite.originalText.length();
            }
        }
    }
    
    @Override
    public void onPointerDrag(Event event) {
        super.onPointerDrag(event);
        float differenceBetweenTargetPositions = calculateDifferenceBetweenTargetPositions();
        float wordPosition = differenceBetweenTargetPositions - getShiftX();
        boolean isOverlapping = wordPosition < getLeftSideWidth();
        TargetDisplay opposite = leftSide == this ? rightSide : leftSide;
        boolean shouldBeVisible = ! isOverlapping;
        if (shouldBeVisible != opposite.isVisible()) {
            opposite.setVisible(shouldBeVisible);
            if (shouldBeVisible) {
                text = originalText;
                shiftX(
                        leftSide == this
                        ? -calculateTextWidth(rightSide.originalText)
                        : calculateTextWidth(leftSide.originalText));
            }
            else {
                text = leftSide.originalText + rightSide.originalText;
                shiftX(
                        leftSide == this
                        ? calculateTextWidth(rightSide.originalText)
                        : -calculateTextWidth(leftSide.originalText));
            }
            drawText();
        }
        if ( ! shouldBeVisible) {
            for (int split = 0; split < otherLetterPositions.size(); split++) {
                Float splitPosition = otherLetterPositions.get(split);
                if (wordPosition <= splitPosition) {
                    if (split != splitIndex) {
                        text = opposite.originalText.substring(0, split) +
                                originalText + 
                                opposite.originalText.substring(split);
                        shiftX(otherLetterPositions.get(splitIndex) - splitPosition);
                        splitIndex = split;
                        drawText();
                    }
                    break;
                }
            }
        }
    }

    protected void setVisible(boolean isVisible) {
        layer.setVisible(isVisible);
    }
    
    public boolean isVisible() {
        return layer.visible();
    }

    protected float getLeftSideWidth() {
        return leftSide.layer.width();
    }

    protected float calculateDifferenceBetweenTargetPositions() {
        rightPoint.set(0, 0);
        Layers.transform(rightPoint, rightSide.layer, leftSide.layer, leftPoint);
        float differenceBetweenTargetPositions = leftPoint.x;
        return differenceBetweenTargetPositions;
    }

    protected void drawText() {
        canvas.clear();
        TextLayout textLayout = PlayN.graphics().layoutText(
                text,
                TEXT_FORMAT);
        float x =
                rightSide == this
                ? 0
                : canvas.width() - textLayout.width();
        canvas.fillText(textLayout, x, 32);
        
        icon.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                canvas.drawImage(icon, 50, 54);
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
    
    public TargetDisplay getLeftSide() {
        return leftSide;
    }
    
    public TargetDisplay getRightSide() {
        return rightSide;
    }
}
