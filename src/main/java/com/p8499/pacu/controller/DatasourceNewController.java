package com.p8499.pacu.controller;

import com.p8499.pacu.model.Datasource;
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
public class DatasourceNewController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    private Datasource result;
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
    }

    public void onSaveClick() throws IOException {
        result = new Datasource();
        result.mId.set(mId.getText());
        result.mDatabaseType.set(mDatabaseType.getSelectionModel().getSelectedItem());
        result.mUrl.set(mUrl.getText());
        result.mUsername.set(mUsername.getText());
        result.mPassword.set(mPassword.getText());
        mStage.close();
    }

    public Datasource getResult() {
        return result;
    }

    public static Datasource invoke(ApplicationController applicationController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/datasourceNew.fxml"));
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
        DatasourceNewController dialogController = fxmlLoader.getController();
        dialogController.setEnvironment(dialogStage, dialogScene, applicationController);
        dialogStage.showAndWait();
        return dialogController.getResult();
    }
}
