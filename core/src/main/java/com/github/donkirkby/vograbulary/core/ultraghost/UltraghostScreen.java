package com.github.donkirkby.vograbulary.core.ultraghost;

import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

import com.github.donkirkby.vograbulary.core.ChallengeScreen;

public class UltraghostScreen extends ChallengeScreen {

    private Button menuButton;

    @Override
    public Button getMenuButton() {
        return menuButton;
    }

    @Override
    public String getName() {
        return "Ultraghost";
    }

    @Override
    protected Group createBody() {
        menuButton = new Button("Menu");
        return new Group(AxisLayout.vertical()).add(
                new Label("Ultraghost").addStyles(Style.FONT.is(TITLE_FONT)),
                menuButton);
    }

}
