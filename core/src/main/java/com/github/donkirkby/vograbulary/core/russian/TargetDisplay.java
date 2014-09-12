package com.github.donkirkby.vograbulary.core.russian;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer.Adapter;
import playn.core.Pointer.Event;

public class TargetDisplay extends Adapter {
    private String text;
    private float startX;
    private Canvas canvas;
    private ImageLayer layer;

    public TargetDisplay() {
        this.text = "";
        CanvasImage image = PlayN.graphics().createImage(250, 64);
        canvas = image.canvas();
        layer = PlayN.graphics().createImageLayer(image);
        layer.addListener(this);
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
        text += "X";
        startX = event.localX();
        drawText();
    }

    private void drawText() {
        canvas.clear();
        canvas.drawText(text, 32, 32);
    }
    
    @Override
    public void onPointerDrag(Event event) {
        layer.transform().translateX(event.localX() - startX);
    }
}
