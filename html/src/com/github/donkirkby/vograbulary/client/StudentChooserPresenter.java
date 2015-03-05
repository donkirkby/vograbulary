package com.github.donkirkby.vograbulary.client;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;

public class StudentChooserPresenter extends VograbularyPresenter {

    private static StudentChooserPresenterUiBinder uiBinder = GWT
            .create(StudentChooserPresenterUiBinder.class);

    interface StudentChooserPresenterUiBinder extends
            UiBinder<Widget, StudentChooserPresenter> {
    }
    
    @UiField
    HTMLPanel studentPanel;
    
    @UiField
    ListBox wordLength;
    
    @UiField
    ListBox vocabularySize;
    
    @UiField
    Button startButton;
    
    private CellList<String> studentList;
    private GwtPreferences preferences;
    private boolean isHyperghost;
    private MultiSelectionModel<String> selectionModel;

    public StudentChooserPresenter(GwtPreferences preferences) {
        this.preferences = preferences;
        initWidget(uiBinder.createAndBindUi(this));
        TextCell textCell = new TextCell();
        studentList = new CellList<String>(textCell);
        selectionModel = new MultiSelectionModel<>();
        studentList.addCellPreviewHandler(new Handler<String>() {
            @Override
            public void onCellPreview(CellPreviewEvent<String> event) {
                if (BrowserEvents.CLICK.equals(event.getNativeEvent().getType())) {
                    boolean isSelected = selectionModel.isSelected(event.getValue());
                    selectionModel.setSelected(event.getValue(), ! isSelected);
                    event.setCanceled(true);
                }
            }
        });
        DefaultSelectionEventManager<String> selectionManager =
                DefaultSelectionEventManager.createCheckboxManager();
        studentList.setSelectionModel(selectionModel, selectionManager);
        studentPanel.add(studentList);
        studentList.setRowData(preferences.getStudentNames());
        setListBoxValue(
                wordLength,
                preferences.getUltraghostMinimumWordLength());
        setListBoxValue(
                vocabularySize,
                preferences.getComputerStudentVocabularySize());
    }
    
    private void setListBoxValue(ListBox listBox, int value) {
        for (int i = 0; i < listBox.getItemCount(); i++) {
            if (Integer.parseInt(listBox.getItemText(i)) == value) {
                listBox.setSelectedIndex(i);
                return;
            }
        }
        listBox.setSelectedIndex(0);
    }
    
    private int getListBoxValue(ListBox listBox) {
        return Integer.parseInt(listBox.getItemText(listBox.getSelectedIndex()));
    }
    
    @UiHandler("startButton")
    void start(ClickEvent view) {
        preferences.setUltraghostMinimumWordLength(
                getListBoxValue(wordLength));
        preferences.setComputerStudentVocabularySize(
                getListBoxValue(vocabularySize));
        UltraghostPresenter presenter = new UltraghostPresenter(preferences);
        presenter.setStudents(selectionModel.getSelectedSet());
        presenter.setHyperghost(isHyperghost);
        getNavigationListener().showPresenter(presenter);
    }
    
    public StudentChooserPresenter setHyperghost(boolean isHyperghost) {
        this.isHyperghost = isHyperghost;
        return this;
    }
    public boolean isHyperghost() {
        return isHyperghost;
    }
}
