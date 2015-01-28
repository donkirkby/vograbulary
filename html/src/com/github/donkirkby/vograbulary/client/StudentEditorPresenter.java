package com.github.donkirkby.vograbulary.client;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class StudentEditorPresenter extends VograbularyPresenter {
    public static final String HISTORY_TOKEN = "students";

    private static StudentEditorPresenterUiBinder uiBinder = GWT
            .create(StudentEditorPresenterUiBinder.class);

    interface StudentEditorPresenterUiBinder extends
            UiBinder<Widget, StudentEditorPresenter> {
    }
    
    @UiField
    HTMLPanel studentPanel;
    
    @UiField
    TextBox studentName;

    private CellList<String> studentDisplay;
    private GwtPreferences preferences;
    private String oldStudentName;
    private SingleSelectionModel<String> selectionModel;

    private ListDataProvider<String> studentList;

    public StudentEditorPresenter(final GwtPreferences preferences) {
        this.preferences = preferences;
        initWidget(uiBinder.createAndBindUi(this));
        TextCell textCell = new TextCell();
        studentDisplay = new CellList<String>(textCell);
        studentList = new ListDataProvider<>(preferences.getStudentNames());
        studentList.addDataDisplay(studentDisplay);
        selectionModel = new SingleSelectionModel<>();
        studentDisplay.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                updateSelectedStudent();
                oldStudentName = selectionModel.getSelectedObject();
                studentName.setText(oldStudentName);
            }
        });
        studentPanel.add(studentDisplay);
        selectionModel.setSelected(studentList.getList().get(0), true);
    }

    @UiHandler("addButton")
    void add(ClickEvent event) {
        updateSelectedStudent();
        selectionModel.setSelected(oldStudentName, false);
        oldStudentName = null;
        studentName.setText("");
        studentName.setFocus(true);
    }
    
    @UiHandler("deleteButton")
    void delete(ClickEvent event) {
        if (oldStudentName != null) {
            studentList.getList().remove(oldStudentName);
            preferences.setStudentNames(studentList.getList());
            oldStudentName = null;
            studentName.setText("");
            studentName.setFocus(true);
        }
    }

    private void updateSelectedStudent() {
        if (oldStudentName == null) {
            String newName = studentName.getText();
            if (newName.length() > 0) {
                studentList.getList().add(newName);
                preferences.setStudentNames(studentList.getList());
            }
        }
        else {
            int oldIndex = studentList.getList().indexOf(oldStudentName);
            studentList.getList().set(oldIndex, studentName.getText());
            preferences.setStudentNames(studentList.getList());
        }
    }
}
