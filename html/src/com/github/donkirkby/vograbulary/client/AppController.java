package com.github.donkirkby.vograbulary.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController
implements ValueChangeHandler<String>, VograbularyPresenter.NavigationListener {
    private HasWidgets container;
    private GwtPreferences preferences = new GwtPreferences();
    
    public AppController() {
        History.addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        VograbularyPresenter presenter;
        if (event.getValue().equals(RussianDollsPresenter.HISTORY_TOKEN)) {
            presenter = new RussianDollsPresenter();
        }
        else if (event.getValue().equals(UltraghostPresenter.HISTORY_TOKEN)) {
            presenter = new StudentChooserPresenter(preferences);
        }
        else if (event.getValue().equals(UltraghostPresenter.HISTORY_TOKEN_HYPER)) {
            presenter = new StudentChooserPresenter(preferences).setHyperghost(true);
        }
        else {
            presenter = new MainPresenter(preferences);
        }
        showPresenter(presenter);
    }

    public void go(HasWidgets container) {
        this.container = container;
        History.newItem(MainPresenter.HISTORY_TOKEN);
    }

    @Override
    public void showPresenter(VograbularyPresenter presenter) {
        presenter.setNavigationListener(this);
        this.container.clear();
        this.container.add(presenter);
    }

    @Override
    public void showPresenter(
            VograbularyPresenter presenter,
            String historyToken) {
        boolean issueEvent = false;
        History.newItem(historyToken, issueEvent);
        showPresenter(presenter);
    }
}
