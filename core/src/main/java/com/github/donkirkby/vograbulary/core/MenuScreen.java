package com.github.donkirkby.vograbulary.core;

import com.github.donkirkby.vograbulary.core.russian.RussianDollsScreen;
import com.github.donkirkby.vograbulary.core.ultraghost.UltraghostScreen;

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
                new RussianDollsScreen(),
                new UltraghostScreen()
        };
        Group group = new Group(AxisLayout.vertical()).add(
                new Label("Main Menu").addStyles(Style.FONT.is(TITLE_FONT)));
        for (final ChallengeScreen screen : screens) {
            group.add(new Button(screen.getName()).onClick(new UnitSlot() {
                public void onEmit () {
                    screenStack.push(screen);
                    screen.getMenuButton().clicked().connect(new UnitSlot() {
                        @Override
                        public void onEmit() {
                            screenStack.remove(screen);
                        }
                    });
                }
            }));
        }
        return group;
    }
}