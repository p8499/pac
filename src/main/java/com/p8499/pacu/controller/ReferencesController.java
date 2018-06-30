package com.p8499.pacu.controller;

import com.p8499.pacu.model.TreeItemContainer;
import com.p8499.pacu.model.Reference;
import com.p8499.pacu.model.References;
import javafx.beans.property.SimpleStringProperty;
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
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/15/2018.
 */
public class ReferencesController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TableView<Reference> mTable;
    @FXML
    private TableColumn<Reference, String> mColumnDomestics;
    @FXML
    private TableColumn<Reference, String> mColumnForeignModule;
    @FXML
    private TableColumn<Reference, String> mColumnForeigns;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mTable.setRowFactory(tableView -> {
            TableRow<Reference> row = new TableRow<>();
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
                    Reference reference = tableView.getItems().remove(draggedIndex);
                    int dropIndex = row.isEmpty() ? tableView.getItems().size() : row.getIndex();
                    tableView.getItems().add(dropIndex, reference);
                    event.setDropCompleted(true);
                    tableView.getSelectionModel().clearAndSelect(dropIndex);
                    ((TreeItemContainer) mScene.getUserData()).refreshTreeItem();
                    event.consume();
                }
            });
            return row;
        });
        mColumnDomestics.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(StringUtils.join(cellDataFeatures.getValue().mDomestics, ", ")));
        mColumnForeignModule.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().mForeignModule.get()));
        mColumnForeigns.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(StringUtils.join(cellDataFeatures.getValue().mForeigns, ", ")));
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        References model = (References) mScene.getUserData();
        mTable.setItems(model.mReferences.get());
    }

    public void onDeleteClick() throws IOException {
        List<Reference> selected = mTable.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty())
            if (ConfirmController.invoke(mApplicationController, String.format("Delete %d %s?", selected.size(), selected.size() > 1 ? "items" : "item"))) {
                References model = (References) mScene.getUserData();
                model.mReferences.removeAll(selected);
                mApplicationController.isModified.set(true);
                model.refreshTreeItem();
            }
    }

    public void onAddClick() throws IOException {
        Reference reference = ReferenceNewController.invoke(mApplicationController);
        if (reference != null) {
            References model = (References) mScene.getUserData();
            reference.mParent = model;
            model.mReferences.add(reference);
            mApplicationController.isModified.set(true);
            model.refreshTreeItem();
        }
    }
}
