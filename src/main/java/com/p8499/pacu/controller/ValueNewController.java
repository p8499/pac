package com.p8499.pacu.controller;

import com.p8499.pacu.model.Value;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/13/2018.
 */
public class ValueNewController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    private Value result;
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
    }

    public void onSaveClick() throws IOException {
        result = new Value();
        result.mValue.set(mValue.getText());
        result.mCode.set(mCode.getText());
        result.mLabel.set(mLabel.getText());
        mStage.close();
    }

    public Value getResult() {
        return result;
    }

    public static Value invoke(ApplicationController applicationController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/valueNew.fxml"));
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
        ValueNewController dialogController = fxmlLoader.getController();
        dialogController.setEnvironment(dialogStage, dialogScene, applicationController);
        dialogStage.showAndWait();
        return dialogController.getResult();
    }
}
