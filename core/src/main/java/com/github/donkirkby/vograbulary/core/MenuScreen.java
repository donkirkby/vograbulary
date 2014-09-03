package com.github.donkirkby.vograbulary.core;

import com.github.donkirkby.vograbulary.core.russian.RussianDollsScreen;

import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

public class MenuScreen extends VograbularyScreen
{
    private ScreenStack screenStack;
    
    public MenuScreen(ScreenStack screenStack) {
        this.screenStack = screenStack;
    }

    @Override
    protected Group createBody() {
        final ChallengeScreen[] screens = new ChallengeScreen[] {
                new RussianDollsScreen()
        };
        final Button ultraghost = new Button("Ultraghost");
        return new Group(AxisLayout.vertical()).add(
                new Label("Main Menu").addStyles(Style.FONT.is(TITLE_FONT)),
                ultraghost.onClick(new UnitSlot() { 
                    public void onEmit () {
                        ultraghost.text.update("Clicked");
                    }
                }),
                new Button("Russian Dolls").onClick(new UnitSlot() { 
                    public void onEmit () {
                        screenStack.push(screens[0]);
                        screens[0].getMenuButton().clicked().connect(new UnitSlot() {
                            @Override
                            public void onEmit() {
                                screenStack.remove(screens[0]);
                            }
                        });
                    }
                }));
    }
}