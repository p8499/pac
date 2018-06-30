package com.p8499.pacu.controller;

import com.p8499.pacu.model.Field;
import com.p8499.pacu.model.Fields;
import com.p8499.pacu.model.TreeItemContainer;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/15/2018.
 */
public class FieldsController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TableView<Field> mTable;
    @FXML
    private TableColumn<Field, String> mColumnSource;
    @FXML
    private TableColumn<Field, String> mColumnDatabaseColumn;
    @FXML
    private TableColumn<Field, String> mColumnDescription;
    @FXML
    private TableColumn<Field, String> mColumnJavaType;
    @FXML
    private TableColumn<Field, String> mColumnNotnull;
    @FXML
    private TableColumn<Field, String> mColumnDefaultValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mTable.setRowFactory(tableView -> {
            TableRow<Field> row = new TableRow<>();
            //double click
            row.setOnMouseClicked(clickEvent -> {
                if ((clickEvent.getClickCount() > 1) && (!row.isEmpty()))
                    mApplicationController.selectTreeItem(row.getItem().getTreeItem());
            });
            //drag and drop
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.put(DataFormat.PLAIN_TEXT, row.getIndex());
                    Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);
                    dragboard.setDragView(row.snapshot(null, null));
                    dragboard.setDragViewOffsetX(event.getX());
                    dragboard.setDragViewOffsetY(event.getY());
                    dragboard.setContent(clipboardContent);
                    event.consume();
                }
            });
            row.setOnDragOver(event -> {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasContent(DataFormat.PLAIN_TEXT)) {
                    if (row.getIndex() != ((Integer) dragboard.getContent(DataFormat.PLAIN_TEXT)).intValue()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                        event.consume();
                    }
                }
            });
            row.setOnDragDropped(event -> {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasContent(DataFormat.PLAIN_TEXT)) {
                    int draggedIndex = (Integer) dragboard.getContent(DataFormat.PLAIN_TEXT);
                    Field field = tableView.getItems().remove(draggedIndex);
                    int dropIndex = row.isEmpty() ? tableView.getItems().size() : row.getIndex();
                    tableView.getItems().add(dropIndex, field);
                    event.setDropCompleted(true);
                    tableView.getSelectionModel().clearAndSelect(dropIndex);
                    ((TreeItemContainer) mScene.getUserData()).refreshTreeItem();
                    event.consume();
                }
            });
            return row;
        });
        mColumnSource.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mSource);
        mColumnDatabaseColumn.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDatabaseColumn);
        mColumnDescription.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDescription);
        mColumnJavaType.setCellValueFactory(cellDataFeatures -> {
            Field field = cellDataFeatures.getValue();
            if ("Integer".equals(field.mJavaType.get()))
                return new SimpleStringProperty("table".equals(field.mSource.get()) ? String.format("Integer (%d)", field.mIntegerLength.get()) : "Integer");
            if ("Double".equals(field.mJavaType.get()))
                return new SimpleStringProperty("table".equals(field.mSource.get()) ? String.format("Double (%d,%d)", field.mIntegerLength.get(), field.mFractionLength.get()) : "Double");
            if ("String".equals(field.mJavaType.get()))
                return new SimpleStringProperty("table".equals(field.mSource.get()) ? String.format("String (%d)", field.mStringLength.get()) : "String");
            if ("java.util.Date".equals(field.mJavaType.get()))
                return new SimpleStringProperty("java.util.Date");
            else
                return new SimpleStringProperty();
        });
        mColumnNotnull.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().mNotnull.get() ? "Not Null" : ""));
        mColumnDefaultValue.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDefaultValue);
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        Fields model = (Fields) mScene.getUserData();
        mTable.setItems(model.mFields.get());
    }

    public void onDeleteClick() throws IOException {
        List<Field> selected = mTable.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty())
            if (ConfirmController.invoke(mApplicationController, String.format("Delete %d %s?", selected.size(), selected.size() > 1 ? "items" : "item"))) {
                Fields model = (Fields) mScene.getUserData();
                model.mFields.removeAll(selected);
                mApplicationController.isModified.set(true);
                model.refreshTreeItem();
            }
    }

    public void onAddClick() throws IOException {
        Field field = FieldNewController.invoke(mApplicationController);
        if (field != null) {
            Fields model = (Fields) mScene.getUserData();
            field.mParent = model;
            model.mFields.add(field);
            mApplicationController.isModified.set(true);
            model.refreshTreeItem();
        }
    }
}
