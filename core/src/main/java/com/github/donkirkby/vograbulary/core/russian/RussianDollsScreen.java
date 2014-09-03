package com.github.donkirkby.vograbulary.core.russian;

import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

import com.github.donkirkby.vograbulary.core.ChallengeScreen;

public class RussianDollsScreen extends ChallengeScreen {
    private Button menuButton;

    @Override
    protected Group createBody() {
        menuButton = new Button("Menu");
        return new Group(AxisLayout.vertical()).add(
                new Label("Russian Dolls").addStyles(Style.FONT.is(TITLE_FONT)),
                menuButton);
    }

    @Override
    protected Button getMenuButton() {
        return menuButton;
    }

}
