package com.p8499.pacu.controller;

import com.p8499.pacu.model.*;
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
public class ReferenceNewController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    private Reference result;
    @FXML
    private TextField mAbbreviation;
    @FXML
    private ListView<String> mDomesticCandidates;
    @FXML
    private ListView<String> mDomestics;
    @FXML
    private ComboBox<String> mForeignModule;
    @FXML
    private ListView<String> mForeignCandidates;
    @FXML
    private ListView<String> mForeigns;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mDomesticCandidates.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mDomestics.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mForeignCandidates.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mForeigns.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //when right list view is changed, set left list view and set abbreviation
        mDomestics.getItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            initDomesticCandidates();
            setAbbreviation();
        });
        //when combo box is changed
        mForeignModule.valueProperty().addListener((observable, oldValue, newValue) -> {
            mForeigns.getItems().removeAll();
            initForeignCandidates();
            setAbbreviation();
        });
        //when right list view is changed, set left list view and set abbreviation
        mForeigns.getItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            initForeignCandidates();
            setAbbreviation();
        });
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        initDomesticCandidates();
        initForeignModule();
    }

    public void onSaveClick() throws IOException {
        result = new Reference();
        result.mDomestics.set(mDomestics.getItems());
        result.mForeignModule.set(mForeignModule.getSelectionModel().getSelectedItem());
        result.mForeigns.set(mForeigns.getItems());
        mStage.close();
    }

    public void onDomesticSelectClick() {
        mDomestics.getItems().addAll(mDomesticCandidates.getSelectionModel().getSelectedItems());
    }

    public void onDomesticDeselectClick() {
        mDomestics.getItems().removeAll(mDomestics.getSelectionModel().getSelectedItems());
    }

    public void onForeignSelectClick() {
        mForeigns.getItems().addAll(mForeignCandidates.getSelectionModel().getSelectedItems());
    }

    public void onForeignDeselectClick() {
        mForeigns.getItems().removeAll(mForeigns.getSelectionModel().getSelectedItems());
    }

    public Reference getResult() {
        return result;
    }

    private void initDomesticCandidates() {
        mDomesticCandidates.getItems().clear();
        ((References) mScene.getUserData()).mParent.mFields.get().mFields.forEach(field -> {
            if ("table".equals(field.mSource.get())) {
                String databaseColumn = field.mDatabaseColumn.get();
                if (!mDomestics.getItems().contains(databaseColumn))
                    mDomesticCandidates.getItems().add(databaseColumn);
            }
        });
    }

    private void initForeignModule() {
        mForeignModule.getItems().clear();
        Module domesticModule = ((References) mScene.getUserData()).mParent;
        domesticModule.mParent.mModules.forEach(module -> {
            if (!module.mId.get().equals(domesticModule.mId.get()))
                mForeignModule.getItems().add(module.mId.get());
        });
    }

    private void initForeignCandidates() {
        mForeignCandidates.getItems().clear();
        ((References) mScene.getUserData()).mParent.mParent.mModules.get().filtered(module -> module.mId.get().equals(mForeignModule.getSelectionModel().getSelectedItem())).get(0).mFields.get().mFields.forEach(field -> {
            if ("table".equals(field.mSource.get())) {
                String databaseColumn = field.mDatabaseColumn.get();
                if (!mForeigns.getItems().contains(databaseColumn))
                    mForeignCandidates.getItems().add(databaseColumn);
            }
        });
    }

    private void setAbbreviation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(StringUtils.join(mDomestics.getItems(), ", "));
        stringBuilder.append("] references [");
        stringBuilder.append(StringUtils.join(mForeigns.getItems(), ", "));
        stringBuilder.append("]");
        mAbbreviation.setText(stringBuilder.toString());
    }

    public static Reference invoke(ApplicationController applicationController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/referenceNew.fxml"));
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
        ReferenceNewController dialogController = fxmlLoader.getController();
        dialogController.setEnvironment(dialogStage, dialogScene, applicationController);
        dialogStage.showAndWait();
        return dialogController.getResult();
    }
}
