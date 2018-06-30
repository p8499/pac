package com.p8499.pacu.controller;

import com.p8499.pacu.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
public class ValuesController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TableView<Value> mTable;
    @FXML
    private TableColumn<Value, String> mColumnValue;
    @FXML
    private TableColumn<Value, String> mColumnCode;
    @FXML
    private TableColumn<Value, String> mColumnLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mTable.setRowFactory(tableView -> {
            TableRow<Value> row = new TableRow<>();
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
                    Value value = tableView.getItems().remove(draggedIndex);
                    int dropIndex = row.isEmpty() ? tableView.getItems().size() : row.getIndex();
                    tableView.getItems().add(dropIndex, value);
                    event.setDropCompleted(true);
                    tableView.getSelectionModel().clearAndSelect(dropIndex);
                    ((TreeItemContainer) mScene.getUserData()).refreshTreeItem();
                    event.consume();
                }
            });
            return row;
        });
        mColumnValue.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mValue);
        mColumnCode.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mCode);
        mColumnLabel.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mLabel);
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        Values model = (Values) mScene.getUserData();
        mTable.setItems(model.mValues.get());
    }

    public void onDeleteClick() throws IOException {
        List<Value> selected = mTable.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty())
            if (ConfirmController.invoke(mApplicationController, String.format("Delete %d %s?", selected.size(), selected.size() > 1 ? "items" : "item"))) {
                Values model = (Values) mScene.getUserData();
                model.mValues.removeAll(selected);
                mApplicationController.isModified.set(true);
                model.refreshTreeItem();
            }
    }

    public void onAddClick() throws IOException {
        Value value = ValueNewController.invoke(mApplicationController);
        if (value != null) {
            Values model = (Values) mScene.getUserData();
            value.mParent = model;
            model.mValues.add(value);
            mApplicationController.isModified.set(true);
            model.refreshTreeItem();
        }
    }
}
