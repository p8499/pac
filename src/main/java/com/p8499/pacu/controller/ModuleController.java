package com.p8499.pacu.controller;

import com.p8499.pacu.model.Datasource;
import com.p8499.pacu.model.Module;
import com.p8499.pacu.model.Project;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/20/2018.
 */
public class ModuleController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    @FXML
    private TextField mId;
    @FXML
    private TextField mDescription;
    @FXML
    private TextArea mComment;
    @FXML
    private ComboBox<String> mDatasource;
    @FXML
    private TextField mDatabaseTable;
    @FXML
    private TextField mDatabaseView;
    @FXML
    private TextField mJteeBeanAlias;
    @FXML
    private TextField mJteeMaskAlias;
    @FXML
    private TextField mJteeMapperAlias;
    @FXML
    private TextField mJteeServiceAlias;
    @FXML
    private TextField mJteeControllerBaseAlias;
    @FXML
    private TextField mJteeControllerPath;
    @FXML
    private TextField mJteeControllerAttachmentPath;
    @FXML
    private TextField mAndroidBeanAlias;
    @FXML
    private TextField mAndroidMaskAlias;
    @FXML
    private TextField mAndroidStubAlias;
    @FXML
    private TextField mAndroidViewAlias;
    @FXML
    private TextField mAndroidListViewAlias;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mId.setText(oldValue);
        });
        mJteeBeanAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mJteeBeanAlias.setText(oldValue);
        });
        mJteeMaskAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mJteeMaskAlias.setText(oldValue);
        });
        mJteeMapperAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mJteeMapperAlias.setText(oldValue);
        });
        mJteeServiceAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mJteeServiceAlias.setText(oldValue);
        });
        mJteeControllerBaseAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mJteeControllerBaseAlias.setText(oldValue);
        });
        mAndroidBeanAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mAndroidBeanAlias.setText(oldValue);
        });
        mAndroidMaskAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mAndroidMaskAlias.setText(oldValue);
        });
        mAndroidStubAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mAndroidStubAlias.setText(oldValue);
        });
        mAndroidViewAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mAndroidViewAlias.setText(oldValue);
        });
        mAndroidListViewAlias.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mAndroidListViewAlias.setText(oldValue);
        });
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        mApplicationController = applicationController;
        //combo box
        List<String> datasourceIdList = new ArrayList<>();
        ((Project) mStage.getUserData()).mEnvJtee.get().mDatasources.get().mDatasources.get().forEach(datasource -> datasourceIdList.add(datasource.mId.get()));
        mDatasource.setItems(FXCollections.observableList(datasourceIdList));
        onResetClick();
    }

    public void onFieldsClick() {
        mApplicationController.selectTreeItem(((Module) mScene.getUserData()).mFields.get().getTreeItem());
    }

    public void onUniquesClick() {
        mApplicationController.selectTreeItem(((Module) mScene.getUserData()).mUniques.get().getTreeItem());
    }

    public void onReferencesClick() {
        mApplicationController.selectTreeItem(((Module) mScene.getUserData()).mReferences.get().getTreeItem());
    }

    public void onSaveClick() throws IOException {
        Module model = (Module) mScene.getUserData();
        model.mId.set(mId.getText());
        model.mDescription.set(mDescription.getText());
        model.mComment.set(mComment.getText());
        model.mDatasource.set(mDatasource.getSelectionModel().getSelectedItem());
        model.mDatabaseTable.set(mDatabaseTable.getText());
        model.mDatabaseView.set(mDatabaseView.getText());
        model.mJteeBeanAlias.set(mJteeBeanAlias.getText());
        model.mJteeMaskAlias.set(mJteeMaskAlias.getText());
        model.mJteeMapperAlias.set(mJteeMapperAlias.getText());
        model.mJteeServiceAlias.set(mJteeServiceAlias.getText());
        model.mJteeControllerBaseAlias.set(mJteeControllerBaseAlias.getText());
        model.mJteeControllerPath.set(mJteeControllerPath.getText());
        model.mJteeControllerAttachmentPath.set(mJteeControllerAttachmentPath.getText());
        model.mAndroidBeanAlias.set(mAndroidBeanAlias.getText());
        model.mAndroidMaskAlias.set(mAndroidMaskAlias.getText());
        model.mAndroidStubAlias.set(mAndroidStubAlias.getText());
        model.mAndroidViewAlias.set(mAndroidViewAlias.getText());
        model.mAndroidListViewAlias.set(mAndroidListViewAlias.getText());
        mApplicationController.isModified.set(true);
        model.refreshTreeItem();
    }

    public void onResetClick() {
        Module model = (Module) mScene.getUserData();
        mId.setText(model.mId.get());
        mDescription.setText(model.mDescription.get());
        mComment.setText(model.mComment.get());
        mDatasource.getSelectionModel().select(model.mDatasource.get());
        mDatabaseTable.setText(model.mDatabaseTable.get());
        mDatabaseView.setText(model.mDatabaseView.get());
        mJteeBeanAlias.setText(model.mJteeBeanAlias.get());
        mJteeMaskAlias.setText(model.mJteeMaskAlias.get());
        mJteeMapperAlias.setText(model.mJteeMapperAlias.get());
        mJteeServiceAlias.setText(model.mJteeServiceAlias.get());
        mJteeControllerBaseAlias.setText(model.mJteeControllerBaseAlias.get());
        mJteeControllerPath.setText(model.mJteeControllerPath.get());
        mJteeControllerAttachmentPath.setText(model.mJteeControllerAttachmentPath.get());
        mAndroidBeanAlias.setText(model.mAndroidBeanAlias.get());
        mAndroidMaskAlias.setText(model.mAndroidMaskAlias.get());
        mAndroidStubAlias.setText(model.mAndroidStubAlias.get());
        mAndroidViewAlias.setText(model.mAndroidViewAlias.get());
        mAndroidListViewAlias.setText(model.mAndroidListViewAlias.get());
    }
}
