package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class AnagramsPresenter extends VograbularyPresenter {
    public static final String HISTORY_TOKEN = "anagrams";

    interface AnagramsPresenterUiBinder extends
    UiBinder<Widget, AnagramsPresenter> {
    }
    
    @UiField
    AbsolutePanel wordPanel;

    @UiField
    ParagraphElement stateText;
    
    @UiField
    Button nextButton;
    
    private static AnagramsPresenterUiBinder uiBinder = 
            GWT.create(AnagramsPresenterUiBinder.class);

    public AnagramsPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
