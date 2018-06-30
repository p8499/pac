package com.p8499.pacu.controller;

import com.p8499.pacu.model.*;
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
public class ModulesController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TableView<Module> mTable;
    @FXML
    private TableColumn<Module, String> mColumnId;
    @FXML
    private TableColumn<Module, String> mColumnDescription;
    @FXML
    private TableColumn<Module, String> mColumnDatabaseTable;
    @FXML
    private TableColumn<Module, String> mColumnDatabaseView;
    @FXML
    private TableColumn<Module, String> mColumnDatasource;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mTable.setRowFactory(tableView -> {
            TableRow<Module> row = new TableRow<>();
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
                    Module module = tableView.getItems().remove(draggedIndex);
                    int dropIndex = row.isEmpty() ? tableView.getItems().size() : row.getIndex();
                    tableView.getItems().add(dropIndex, module);
                    event.setDropCompleted(true);
                    tableView.getSelectionModel().clearAndSelect(dropIndex);
                    ((TreeItemContainer) mScene.getUserData()).refreshTreeItem();
                    event.consume();
                }
            });
            return row;
        });
        mColumnId.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mId);
        mColumnDescription.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDescription);
        mColumnDatabaseTable.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDatabaseTable);
        mColumnDatabaseView.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDatabaseView);
        mColumnDatasource.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDatasource);
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        Modules model = (Modules) mScene.getUserData();
        mTable.setItems(model.mModules.get());
    }

    public void onDeleteClick() throws IOException {
        List<Module> selected = mTable.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty())
            if (ConfirmController.invoke(mApplicationController, String.format("Delete %d %s?", selected.size(), selected.size() > 1 ? "items" : "item"))) {
                Modules model = (Modules) mScene.getUserData();
                model.mModules.removeAll(selected);
                mApplicationController.isModified.set(true);
                model.refreshTreeItem();
            }
    }

    public void onAddClick() throws IOException {
        Module module = ModuleNewController.invoke(mApplicationController);
        if (module != null) {
            Modules model = (Modules) mScene.getUserData();
            module.mParent = model;
            model.mModules.add(module);
            mApplicationController.isModified.set(true);
            model.refreshTreeItem();
        }
    }
}
