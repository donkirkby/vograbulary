package com.github.donkirkby.vograbulary.core;

import static playn.core.PlayN.graphics;
import playn.core.Font;
import tripleplay.game.UIScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Group;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

public abstract class VograbularyScreen extends UIScreen {
    public static final Font TITLE_FONT = 
            graphics().createFont("Helvetica", Font.Style.PLAIN, 24);

    private Root root;

    @Override public void wasAdded() {
        // Create the root object that holds all the objects for the screen.
        super.wasAdded();
        root = iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(),
                layer);
        root.addStyles(Style.BACKGROUND.is(
                Background.bordered(0xFFCCCCCC, 0xFF99CCFF, 5).inset(5, 10)));
        root.setSize(width(), height());

        Group body = createBody();
        root.add(body);
    }

    /**
     * Create the main body of the screen that will be added to the root.
     */
    protected abstract Group createBody();
    
    @Override public void wasRemoved() {
        super.wasRemoved();
        iface.destroyRoot(root);
    }

}
