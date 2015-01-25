package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class MainPresenter extends VograbularyPresenter {
    
    public static final String HISTORY_TOKEN = "main";

    private static MainPresenterUiBinder uiBinder = GWT
            .create(MainPresenterUiBinder.class);

    interface MainPresenterUiBinder extends UiBinder<Widget, MainPresenter> {
    }
    
    public MainPresenter(GwtPreferences preferences) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
