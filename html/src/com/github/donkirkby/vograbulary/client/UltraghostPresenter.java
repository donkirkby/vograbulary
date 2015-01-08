package com.github.donkirkby.vograbulary.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UltraghostPresenter extends Composite {
    public static final String HISTORY_TOKEN = "ultraghost";

    private static UltraghostPresenterUiBinder uiBinder = GWT
            .create(UltraghostPresenterUiBinder.class);

    interface UltraghostPresenterUiBinder extends
            UiBinder<Widget, UltraghostPresenter> {
    }

    public UltraghostPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
