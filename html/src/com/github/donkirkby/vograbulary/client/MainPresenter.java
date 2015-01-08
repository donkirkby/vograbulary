package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MainPresenter extends Composite {
    
    public static final String HISTORY_TOKEN = "main";

    private static MainPresenterUiBinder uiBinder = GWT
            .create(MainPresenterUiBinder.class);

    interface MainPresenterUiBinder extends UiBinder<Widget, MainPresenter> {
    }
    
    @UiField
    Button ultraghostButton;
    
    @UiField
    Button russianDollsButton;
    
    @UiHandler("russianDollsButton")
    void goRussianDolls(ClickEvent e) {
        History.newItem(RussianDollsPresenter.HISTORY_TOKEN);
    }
    
    @UiHandler("ultraghostButton")
    void goUltraghost(ClickEvent e) {
        History.newItem(UltraghostPresenter.HISTORY_TOKEN);
    }

    public MainPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
        
        
    }

}
