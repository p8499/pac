package com.p8499.pacu.controller;

import com.p8499.pacu.model.Field;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/20/2018.
 */
public class FieldNewController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    private Field result;
    @FXML
    private ComboBox<String> mSource;
    @FXML
    private TextField mDatabaseColumn;
    @FXML
    private TextField mDescription;
    @FXML
    private CheckBox mNotnull;
    @FXML
    private ComboBox<String> mJavaType;
    @FXML
    private TextField mStringLength;
    @FXML
    private TextField mIntegerLength;
    @FXML
    private TextField mFractionLength;
    @FXML
    private TextField mDefaultValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //source
        mSource.valueProperty().addListener((observable, oldValue, newValue) -> {
            initNotnull();
            initStringLength();
            initIntegerLength();
            initFractionLength();
            initDefaultValue();
        });
        //when disable, clear it
        mNotnull.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) mNotnull.setSelected(false);
        });
        //javaType
        mJavaType.valueProperty().addListener((observable, oldValue, newValue) -> {
            initStringLength();
            initIntegerLength();
            initFractionLength();
            initDefaultValue();
        });
        //when disable, clear it
        mStringLength.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) mStringLength.setText("0");
        });
        //when disable, clear it
        mIntegerLength.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) mIntegerLength.setText("0");
        });
        //when disable, clear it
        mFractionLength.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) mFractionLength.setText("0");
        });
        //when disable, clear it
        mDefaultValue.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) mDefaultValue.clear();
        });
        mDatabaseColumn.textProperty().addListener((observable, oldValue, newValue) -> {
            mDatabaseColumn.setText(newValue.toLowerCase().trim());
        });
        mStringLength.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                mStringLength.setText(oldValue);
        });
        mIntegerLength.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                mIntegerLength.setText(oldValue);
        });
        mFractionLength.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                mFractionLength.setText(oldValue);
        });
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
    }

    public void onSaveClick() throws IOException {
        result = new Field();
        result.mSource.set(mSource.getSelectionModel().getSelectedItem());
        result.mDatabaseColumn.set(mDatabaseColumn.getText());
        result.mDescription.set(mDescription.getText());
        result.mNotnull.set(mNotnull.isSelected());
        result.mJavaType.set(mJavaType.getSelectionModel().getSelectedItem());
        result.mStringLength.set(Integer.valueOf(mStringLength.getText()));
        result.mIntegerLength.set(Integer.valueOf(mIntegerLength.getText()));
        result.mFractionLength.set(Integer.valueOf(mFractionLength.getText()));
        result.mDefaultValue.set(mDefaultValue.getText());
        mStage.close();
    }

    public Field getResult() {
        return result;
    }

    private void initNotnull() {
        if ("table".equals(mSource.getSelectionModel().getSelectedItem()))
            mNotnull.setDisable(false);
        else
            mNotnull.setDisable(true);
    }

    private void initStringLength() {
        if ("table".equals(mSource.getSelectionModel().getSelectedItem())
                && "String".equals(mJavaType.getSelectionModel().getSelectedItem()))
            mStringLength.setDisable(false);
        else
            mStringLength.setDisable(true);
    }

    private void initIntegerLength() {
        if ("table".equals(mSource.getSelectionModel().getSelectedItem())
                && ("Integer".equals(mJavaType.getSelectionModel().getSelectedItem()) || "Double".equals(mJavaType.getSelectionModel().getSelectedItem())))
            mIntegerLength.setDisable(false);
        else
            mIntegerLength.setDisable(true);
    }

    private void initFractionLength() {
        if ("table".equals(mSource.getSelectionModel().getSelectedItem())
                && "Double".equals(mJavaType.getSelectionModel().getSelectedItem()))
            mFractionLength.setDisable(false);
        else
            mFractionLength.setDisable(true);
    }

    private void initDefaultValue() {
        if ("table".equals(mSource.getSelectionModel().getSelectedItem())
                && ("Integer".equals(mJavaType.getSelectionModel().getSelectedItem()) || "Double".equals(mJavaType.getSelectionModel().getSelectedItem()) || "String".equals(mJavaType.getSelectionModel().getSelectedItem())))
            mDefaultValue.setDisable(false);
        else
            mDefaultValue.setDisable(true);
    }

    public static Field invoke(ApplicationController applicationController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/fieldNew.fxml"));
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
        FieldNewController dialogController = fxmlLoader.getController();
        dialogController.setEnvironment(dialogStage, dialogScene, applicationController);
        dialogStage.showAndWait();
        return dialogController.getResult();
    }
}
