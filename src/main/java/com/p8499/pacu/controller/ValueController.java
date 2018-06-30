package com.p8499.pacu.controller;

import com.p8499.pacu.model.Datasource;
import com.p8499.pacu.model.Value;
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
public class ValueController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TextField mValue;
    @FXML
    private TextField mCode;
    @FXML
    private TextField mLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mCode.textProperty().addListener((observable, oldValue, newValue) -> {
            mCode.setText(newValue.toUpperCase().trim());
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
        Value model = (Value) mScene.getUserData();
        model.mValue.set(mValue.getText());
        model.mCode.set(mCode.getText());
        model.mLabel.set(mLabel.getText());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        Value model = (Value) mScene.getUserData();
        mValue.setText(model.mValue.get());
        mCode.setText(model.mCode.get());
        mLabel.setText(model.mLabel.get());
    }
}
