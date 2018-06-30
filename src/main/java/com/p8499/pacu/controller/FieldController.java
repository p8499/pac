package com.p8499.pacu.controller;

import com.p8499.pacu.model.Field;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/20/2018.
 */
public class FieldController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
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
        onResetClick();
    }

    public void onValuesClick() {
        mApplicationController.selectTreeItem(((Field) mScene.getUserData()).mValues.get().getTreeItem());
    }

    public void onSaveClick() throws IOException {
        Field model = (Field) mScene.getUserData();
        model.mSource.set(mSource.getSelectionModel().getSelectedItem());
        model.mDatabaseColumn.set(mDatabaseColumn.getText());
        model.mDescription.set(mDescription.getText());
        model.mNotnull.set(mNotnull.isSelected());
        model.mJavaType.set(mJavaType.getSelectionModel().getSelectedItem());
        model.mStringLength.set(Integer.valueOf(mStringLength.getText()));
        model.mIntegerLength.set(Integer.valueOf(mIntegerLength.getText()));
        model.mFractionLength.set(Integer.valueOf(mFractionLength.getText()));
        model.mDefaultValue.set(mDefaultValue.getText());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        Field model = (Field) mScene.getUserData();
        mSource.getSelectionModel().select(model.mSource.get());
        mDatabaseColumn.setText(model.mDatabaseColumn.get());
        mDescription.setText(model.mDescription.get());
        mNotnull.setSelected(model.mNotnull.get());
        mJavaType.getSelectionModel().select(model.mJavaType.get());
        mStringLength.setText(String.valueOf(model.mStringLength.get()));
        mIntegerLength.setText(String.valueOf(model.mIntegerLength.get()));
        mFractionLength.setText(String.valueOf(model.mFractionLength.get()));
        mDefaultValue.setText(model.mDefaultValue.get());
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
}
