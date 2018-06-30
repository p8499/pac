package com.p8499.pacu.controller;

import com.p8499.pacu.model.Datasource;
import com.p8499.pacu.model.Datasources;
import com.p8499.pacu.model.TreeItemContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/15/2018.
 */
public class DatasourcesController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TableView<Datasource> mTable;
    @FXML
    private TableColumn<Datasource, String> mColumnId;
    @FXML
    private TableColumn<Datasource, String> mColumnDatabaseType;
    @FXML
    private TableColumn<Datasource, String> mColumnUrl;
    @FXML
    private TableColumn<Datasource, String> mColumnUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mTable.setRowFactory(tableView -> {
            TableRow<Datasource> row = new TableRow<>();
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
                    Datasource datasource = tableView.getItems().remove(draggedIndex);
                    int dropIndex = row.isEmpty() ? tableView.getItems().size() : row.getIndex();
                    tableView.getItems().add(dropIndex, datasource);
                    event.setDropCompleted(true);
                    tableView.getSelectionModel().clearAndSelect(dropIndex);
                    ((TreeItemContainer) mScene.getUserData()).refreshTreeItem();
                    event.consume();
                }
            });
            return row;
        });
        mColumnId.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mId);
        mColumnDatabaseType.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mDatabaseType);
        mColumnUrl.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mUrl);
        mColumnUsername.setCellValueFactory(cellDataFeatures -> cellDataFeatures.getValue().mUsername);
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        Datasources model = (Datasources) mScene.getUserData();
        mTable.setItems(model.mDatasources.get());
    }

    public void onDeleteClick() throws IOException {
        List<Datasource> selected = mTable.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty())
            if (ConfirmController.invoke(mApplicationController, String.format("Delete %d %s?", selected.size(), selected.size() > 1 ? "items" : "item"))) {
                Datasources model = (Datasources) mScene.getUserData();
                model.mDatasources.removeAll(selected);
                mApplicationController.isModified.set(true);
                model.refreshTreeItem();
            }
    }

    public void onAddClick() throws IOException {
        Datasource datasource = DatasourceNewController.invoke(mApplicationController);
        if (datasource != null) {
            Datasources model = (Datasources) mScene.getUserData();
            datasource.mParent = model;
            model.mDatasources.add(datasource);
            mApplicationController.isModified.set(true);
            model.refreshTreeItem();
        }
    }
}
