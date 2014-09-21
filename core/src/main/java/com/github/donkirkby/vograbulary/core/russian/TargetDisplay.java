package com.github.donkirkby.vograbulary.core.russian;

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
    private Canvas canvas;
    private Image icon;
    private ImageLayer layer;
    private TargetDisplay leftSide = this;
    private TargetDisplay rightSide = this;
    private Point leftPoint = new Point();
    private Point rightPoint = new Point();

    public TargetDisplay() {
        this.text = "";
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
    
    public void setText(String text) {
        this.text = text;
        drawText();
    }
    
    @Override
    public void onPointerStart(Event event) {
        super.onPointerStart(event);
        
        drawText();
    }
    
    @Override
    public void onPointerDrag(Event event) {
        super.onPointerDrag(event);
        rightPoint.set(0, 0);
        Layers.transform(rightPoint, rightSide.layer, leftSide.layer, leftPoint);
        boolean isOverlapping = leftPoint.x < leftSide.layer.width();
        TargetDisplay opposite = leftSide == this ? rightSide : leftSide;
        opposite.layer.setVisible( ! isOverlapping);
    }

    private void drawText() {
        canvas.clear();
        boolean isAntialiased = true;
        TextLayout textLayout = PlayN.graphics().layoutText(
                text, 
                new TextFormat(VograbularyScreen.TITLE_FONT, isAntialiased));
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
}
