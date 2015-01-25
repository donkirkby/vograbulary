package com.github.donkirkby.vograbulary.client;

import com.google.gwt.user.client.ui.Composite;

public abstract class VograbularyPresenter extends Composite {
    private NavigationListener navigationListener;
    
    public interface NavigationListener {
        /**
         * Switch to a new presenter without adding an entry in the history.
         */
        void showPresenter(VograbularyPresenter presenter);
        
        /**
         * Switch to a new presenter and add an entry in the history.
         */
        void showPresenter(VograbularyPresenter presenter, String historyToken);
    }
    
    public NavigationListener getNavigationListener() {
        return navigationListener;
    }
    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }
}
