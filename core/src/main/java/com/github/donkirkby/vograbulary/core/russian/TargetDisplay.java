package com.github.donkirkby.vograbulary.core.russian;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer.Event;
import playn.core.util.Callback;

public class TargetDisplay extends DragAdapter {
    private String text;
    private Canvas canvas;
    private Image icon;
    private ImageLayer layer;

    public TargetDisplay() {
        this.text = "";
        icon = PlayN.assets().getImage("images/drag.png");
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
        super.onPointerStart(event);
        
        text += "X";
        drawText();
    }

    private void drawText() {
        canvas.clear();
        canvas.drawText(text, 32, 32);
        icon.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                canvas.drawImage(icon, 50, 40);
            }

            @Override
            public void onFailure(Throwable cause) {
                // Too bad, no icon for you.
            }
        });
    }
}
