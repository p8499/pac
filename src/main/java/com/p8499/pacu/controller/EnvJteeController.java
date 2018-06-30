package com.p8499.pacu.controller;

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
public class EnvJteeController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TextField mApp;
    @FXML
    private TextField mBaseUrl;
    @FXML
    private TextField mPackageBase;
    @FXML
    private TextField mPackageBean;
    @FXML
    private TextField mPackageMask;
    @FXML
    private TextField mPackageMapper;
    @FXML
    private TextField mPackageService;
    @FXML
    private TextField mPackageControllerBase;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mApp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z0-9_]+"))
                mApp.setText(oldValue);
        });
        mBaseUrl.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !"/".equals(newValue.substring(newValue.length() - 1)))
                mBaseUrl.setText(newValue + "/");
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
        mPackageMapper.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageMapper.setText(oldValue);
        });
        mPackageService.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageService.setText(oldValue);
        });
        mPackageControllerBase.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("([a-z_]{1}[a-z0-9_]*(\\.[a-z_]{1}[a-z0-9_]*)*)"))
                mPackageControllerBase.setText(oldValue);
        });
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        onResetClick();
    }

    public void onDatasourcesClick() {
        mApplicationController.selectTreeItem(((EnvJtee) mScene.getUserData()).mDatasources.get().getTreeItem());
    }

    public void onSaveClick() throws IOException {
        EnvJtee model = (EnvJtee) mScene.getUserData();
        model.mApp.set(mApp.getText());
        model.mBaseUrl.set(mBaseUrl.getText());
        model.mPackageBase.set(mPackageBase.getText());
        model.mPackageBean.set(mPackageBean.getText());
        model.mPackageMask.set(mPackageMask.getText());
        model.mPackageMapper.set(mPackageMapper.getText());
        model.mPackageService.set(mPackageService.getText());
        model.mPackageControllerBase.set(mPackageControllerBase.getText());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        EnvJtee model = (EnvJtee) mScene.getUserData();
        mApp.setText(model.mApp.get());
        mBaseUrl.setText(model.mBaseUrl.get());
        mPackageBase.setText(model.mPackageBase.get());
        mPackageBean.setText(model.mPackageBean.get());
        mPackageMask.setText(model.mPackageMask.get());
        mPackageMapper.setText(model.mPackageMapper.get());
        mPackageService.setText(model.mPackageService.get());
        mPackageControllerBase.setText(model.mPackageControllerBase.get());
    }
}
