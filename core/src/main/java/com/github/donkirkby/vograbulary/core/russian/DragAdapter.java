package com.github.donkirkby.vograbulary.core.russian;

import playn.core.Layer;
import playn.core.Pointer.Adapter;
import playn.core.Pointer.Event;

public class DragAdapter extends Adapter {
    private Layer draggingLayer;
    private float startX; // local x position of the pointer when drag started 
    private float shiftX; // shift of layer relative to pointer
    
    @Override
    public void onPointerStart(Event event) {
        // Can only be one drag event at a time, no multitouch.
        draggingLayer = event.hit();
        startX = event.localX() + shiftX;
    }
    
    @Override
    public void onPointerDrag(Event event) {
        float tx = event.localX() - startX + shiftX;
        translateX(tx);
    }

    /**
     * A wrapper around the layer's translateX() so this can be stubbed out
     * during unit tests.
     */
    protected void translateX(float tx) {
        draggingLayer.transform().translateX(tx);
    }
    
    /**
     * Add to the current shift amount.
     */
    protected void shiftX(float x) {
        shiftX += x;
        translateX(x);
    }
    
    public float getShiftX() {
        return shiftX;
    }
}
