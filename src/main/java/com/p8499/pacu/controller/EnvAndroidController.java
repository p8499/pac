package com.p8499.pacu.controller;

import com.p8499.pacu.model.EnvAndroid;
import com.p8499.pacu.model.EnvJtee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/13/2018.
 */
public class EnvAndroidController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TextField mApp;
    @FXML
    private TextField mPackageBase;
    @FXML
    private TextField mPackageBean;
    @FXML
    private TextField mPackageMask;
    @FXML
    private TextField mPackageStub;
    @FXML
    private TextField mPackageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mApp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z0-9_]+"))
                mApp.setText(oldValue);
        });
        mPackageBase.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageBase.setText(oldValue);
        });
        mPackageBean.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageBean.setText(oldValue);
        });
        mPackageMask.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageMask.setText(oldValue);
        });
        mPackageStub.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageStub.setText(oldValue);
        });
        mPackageView.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageView.setText(oldValue);
        });
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        onResetClick();
    }

    public void onSaveClick() throws IOException {
        EnvAndroid model = (EnvAndroid) mScene.getUserData();
        model.mApp.set(mApp.getText());
        model.mPackageBase.set(mPackageBase.getText());
        model.mPackageBean.set(mPackageBean.getText());
        model.mPackageMask.set(mPackageMask.getText());
        model.mPackageStub.set(mPackageStub.getText());
        model.mPackageView.set(mPackageView.getText());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        EnvAndroid model = (EnvAndroid) mScene.getUserData();
        mApp.setText(model.mApp.get());
        mPackageBase.setText(model.mPackageBase.get());
        mPackageBean.setText(model.mPackageBean.get());
        mPackageMask.setText(model.mPackageMask.get());
        mPackageStub.setText(model.mPackageStub.get());
        mPackageView.setText(model.mPackageView.get());
    }
}
