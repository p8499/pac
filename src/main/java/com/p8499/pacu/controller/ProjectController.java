package com.p8499.pacu.controller;

import com.p8499.pacu.model.Modules;
import com.p8499.pacu.model.Project;
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
public class ProjectController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TextField mName;

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

    public void onEnvJteeClick() {
        mApplicationController.selectTreeItem(((Project) mScene.getUserData()).mEnvJtee.get().getTreeItem());
    }

    public void onEnvAndroidClick() {
        mApplicationController.selectTreeItem(((Project) mScene.getUserData()).mEnvAndroid.get().getTreeItem());
    }

    public void onModulesClick() {
        mApplicationController.selectTreeItem(((Project) mScene.getUserData()).mModules.get().getTreeItem());
    }

    public void onSaveClick() throws IOException {
        Project model = (Project) mScene.getUserData();
        model.mName.set(mName.getText());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        Project model = (Project) mScene.getUserData();
        mName.setText(model.mName.get());
    }
}
