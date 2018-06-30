package com.p8499.pacu.controller;

import com.p8499.pacu.model.Datasource;
import com.p8499.pacu.model.Project;
import com.p8499.pacu.model.Unique;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/13/2018.
 */
public class UniqueController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TextField mAbbreviation;
    @FXML
    private ListView<String> mItemCandidates;
    @FXML
    private ListView<String> mItems;
    @FXML
    private CheckBox mKey;
    @FXML
    private CheckBox mSerial;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mItemCandidates.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mItems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        mItemCandidates.setCellFactory(listView -> {
//            ListCell<String> cell = new ListCell<>();
//            //double click
//            cell.setOnMouseClicked(clickEvent -> {
//                if ((clickEvent.getClickCount() > 1) && (!cell.isEmpty()))
//                    mItems.getItems().add(cell.getItem());
//            });
//            return cell;
//        });
//        mItems.setCellFactory(listView -> {
//            ListCell<String> cell = new ListCell<String>();
//            //double click
//            cell.setOnMouseClicked(clickEvent -> {
//                if ((clickEvent.getClickCount() > 1) && (!cell.isEmpty()))
//                    mItems.getItems().remove(cell.getItem());
//            });
//            return cell;
//        });
        //when right list view is changed, set left list view and set abbreviation
        mItems.getItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            initItemCandidates();
            setAbbreviation();
        });
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        initItemCandidates();
        onResetClick();
    }

    public void onSaveClick() throws IOException {
        Unique model = (Unique) mScene.getUserData();
        model.mKey.set(mKey.isSelected());
        model.mSerial.set(mSerial.isSelected());
        model.mItems.set(mItems.getItems());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        Unique model = (Unique) mScene.getUserData();
        mKey.setSelected(model.mKey.get());
        mSerial.setSelected(model.mSerial.get());
        model.mItems.forEach(item -> mItems.getItems().add(item));
    }

    public void onItemSelectClick() {
        mItems.getItems().addAll(mItemCandidates.getSelectionModel().getSelectedItems());
    }

    public void onItemDeselectClick() {
        mItems.getItems().removeAll(mItems.getSelectionModel().getSelectedItems());
    }

    private void initItemCandidates() {
        mItemCandidates.getItems().clear();
        ((Unique) mScene.getUserData()).mParent.mParent.mFields.get().mFields.forEach(field -> {
            if ("table".equals(field.mSource.get())) {
                String databaseColumn = field.mDatabaseColumn.get();
                if (!mItems.getItems().contains(databaseColumn))
                    mItemCandidates.getItems().add(databaseColumn);
            }
        });
    }

    private void setAbbreviation() {
        mAbbreviation.setText(StringUtils.join(mItems.getItems(), ", "));
    }
}
