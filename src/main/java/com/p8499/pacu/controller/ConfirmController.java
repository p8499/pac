package com.p8499.pacu.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/19/2018.
 */
public class ConfirmController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    private Boolean result;
    @FXML
    private Label mMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        mMessage.setText(String.valueOf(scene.getUserData()));
    }

    public void onOkayClick() {
        result = true;
        mStage.close();
    }

    public void onCancelClick() {
        result = false;
        mStage.close();
    }

    public Boolean getResult() {
        return result;
    }

    public static Boolean invoke(ApplicationController applicationController, String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/confirm.fxml"));
        //scene
        Scene confirmScene = new Scene(fxmlLoader.load());
        confirmScene.setUserData(message);
        //stage
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.WINDOW_MODAL);
        confirmStage.initOwner(applicationController.mStage);
        confirmStage.setScene(confirmScene);
        //controller
        ConfirmController confirmController = fxmlLoader.getController();
        confirmController.setEnvironment(confirmStage, confirmScene, applicationController);
        confirmStage.showAndWait();
        return confirmController.getResult();
    }
}
