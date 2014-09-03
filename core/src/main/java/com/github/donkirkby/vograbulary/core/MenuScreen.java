package com.github.donkirkby.vograbulary.core;

import static playn.core.PlayN.graphics;
import playn.core.Font;
import react.UnitSlot;
import tripleplay.game.UIScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

public class MenuScreen extends UIScreen
{
    public static final Font TITLE_FONT = 
            graphics().createFont("Helvetica", Font.Style.PLAIN, 24);

    private Root root;

    @Override public void wasShown () {
        // Create the root object that holds all the objects for the screen.
        super.wasShown();
        root = iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(),
                layer);
        root.addStyles(Style.BACKGROUND.is(
                Background.bordered(0xFFCCCCCC, 0xFF99CCFF, 5).inset(5, 10)));
        root.setSize(width(), height());

        // Create a label at the top of the screen with a title.
        root.add(new Label("Main Menu").addStyles(Style.FONT.is(TITLE_FONT)));

        // Create the button and tell it to change its name when you click it.
        final Button button = new Button("Click Me");
        root.add(button.onClick(new UnitSlot() { 
            public void onEmit () {
                button.text.update("Clicked");
            }
        }));
    }

    @Override public void wasHidden () {
        super.wasHidden();
        iface.destroyRoot(root);
    }
}