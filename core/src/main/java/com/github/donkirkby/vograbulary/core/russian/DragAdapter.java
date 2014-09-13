package com.github.donkirkby.vograbulary.core.russian;

import playn.core.Layer;
import playn.core.Pointer.Adapter;
import playn.core.Pointer.Event;

public class DragAdapter extends Adapter {
    private float startX;
    
    @Override
    public void onPointerStart(Event event) {
        // Can only be one drag event at a time, no multitouch.
        startX = event.localX();
    }
    
    @Override
    public void onPointerDrag(Event event) {
        Layer draggingLayer = event.hit();
        float tx = event.localX() - startX;
        draggingLayer.transform().translateX(tx);
    }
}
