package com.p8499.pacu.controller;

import com.p8499.pacu.model.Unique;
import com.p8499.pacu.model.Uniques;
import com.p8499.pacu.model.TreeItemContainer;
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
public class UniquesController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TableView<Unique> mTable;
    @FXML
    private TableColumn<Unique, String> mColumnItems;
    @FXML
    private TableColumn<Unique, String> mColumnKey;
    @FXML
    private TableColumn<Unique, String> mColumnSerial;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mTable.setRowFactory(tableView -> {
            TableRow<Unique> row = new TableRow<>();
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
                    Unique unique = tableView.getItems().remove(draggedIndex);
                    int dropIndex = row.isEmpty() ? tableView.getItems().size() : row.getIndex();
                    tableView.getItems().add(dropIndex, unique);
                    event.setDropCompleted(true);
                    tableView.getSelectionModel().clearAndSelect(dropIndex);
                    ((TreeItemContainer) mScene.getUserData()).refreshTreeItem();
                    event.consume();
                }
            });
            return row;
        });
        mColumnItems.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(StringUtils.join(cellDataFeatures.getValue().mItems, ", ")));
        mColumnKey.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().mKey.get() ? "Key" : ""));
        mColumnSerial.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().mSerial.get() ? "Serial" : ""));
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        Uniques model = (Uniques) mScene.getUserData();
        mTable.setItems(model.mUniques.get());
    }

    public void onDeleteClick() throws IOException {
        List<Unique> selected = mTable.getSelectionModel().getSelectedItems();
        if (!selected.isEmpty())
            if (ConfirmController.invoke(mApplicationController, String.format("Delete %d %s?", selected.size(), selected.size() > 1 ? "items" : "item"))) {
                Uniques model = (Uniques) mScene.getUserData();
                model.mUniques.removeAll(selected);
                mApplicationController.isModified.set(true);
                model.refreshTreeItem();
            }
    }

    public void onAddClick() throws IOException {
        Unique unique = UniqueNewController.invoke(mApplicationController);
        if (unique != null) {
            Uniques model = (Uniques) mScene.getUserData();
            unique.mParent = model;
            model.mUniques.add(unique);
            mApplicationController.isModified.set(true);
            model.refreshTreeItem();
        }
    }
}
