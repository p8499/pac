package com.p8499.pacu.controller;

import com.p8499.pacu.model.Datasource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/13/2018.
 */
public class DatasourceController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TextField mId;
    @FXML
    private ComboBox<String> mDatabaseType;
    @FXML
    private TextField mUrl;
    @FXML
    private TextField mUsername;
    @FXML
    private TextField mPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        onResetClick();
    }

    public void onSaveClick() throws IOException {
        Datasource model = (Datasource) mScene.getUserData();
        model.mId.set(mId.getText());
        model.mDatabaseType.set(mDatabaseType.getSelectionModel().getSelectedItem());
        model.mUrl.set(mUrl.getText());
        model.mUsername.set(mUsername.getText());
        model.mPassword.set(mPassword.getText());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        Datasource model = (Datasource) mScene.getUserData();
        mId.setText(model.mId.get());
        mDatabaseType.getSelectionModel().select(model.mDatabaseType.get());
        mUrl.setText(model.mUrl.get());
        mUsername.setText(model.mUsername.get());
        mPassword.setText(model.mPassword.get());
    }
}
