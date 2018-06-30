package com.p8499.pacu.controller;

import com.p8499.pacu.model.Unique;
import com.p8499.pacu.model.Uniques;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/13/2018.
 */
public class UniqueNewController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    private Unique result;
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
    }

    public void onSaveClick() throws IOException {
        result = new Unique();
        result.mKey.set(mKey.isSelected());
        result.mSerial.set(mSerial.isSelected());
        result.mItems.set(mItems.getItems());
        mStage.close();
    }

    public void onItemSelectClick() {
        mItems.getItems().addAll(mItemCandidates.getSelectionModel().getSelectedItems());
    }

    public void onItemDeselectClick() {
        mItems.getItems().removeAll(mItems.getSelectionModel().getSelectedItems());
    }

    public Unique getResult() {
        return result;
    }

    private void initItemCandidates() {
        mItemCandidates.getItems().clear();
        Uniques uniques = (Uniques) mScene.getUserData();
        uniques.mParent.mFields.get().mFields.forEach(field -> {
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

    public static Unique invoke(ApplicationController applicationController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/uniqueNew.fxml"));
        //scene
        Scene dialogScene = new Scene(fxmlLoader.load());
        dialogScene.setUserData(applicationController.mScene.getUserData());
        //stage
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(applicationController.mStage);
        dialogStage.setScene(dialogScene);
        dialogStage.setUserData(applicationController.mStage.getUserData());
        //controller
        UniqueNewController dialogController = fxmlLoader.getController();
        dialogController.setEnvironment(dialogStage, dialogScene, applicationController);
        dialogStage.showAndWait();
        return dialogController.getResult();
    }
}
