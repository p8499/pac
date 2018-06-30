package com.p8499.pacu.controller;

import com.p8499.pacu.model.Module;
import com.p8499.pacu.model.Project;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/20/2018.
 */
public class ModuleNewController implements Initializable, EnvironmentContainer {
    private Stage mStage;
    private Scene mScene;
    private ApplicationController mApplicationController;
    private Module result;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[A-Za-z_$]+[a-zA-Z0-9_$]*"))
                mId.setText(oldValue);
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
    }

    public void onSaveClick() throws IOException {
        result = new Module();
        result.mId.set(mId.getText());
        result.mDescription.set(mDescription.getText());
        result.mComment.set(mComment.getText());
        result.mDatasource.set(mDatasource.getSelectionModel().getSelectedItem());
        result.mDatabaseTable.set(mDatabaseTable.getText());
        result.mDatabaseView.set(mDatabaseView.getText());
        result.mJteeBeanAlias.set(StringUtils.capitalize(mId.getText()));
        result.mJteeMaskAlias.set(result.mJteeBeanAlias.get() + "Mask");
        result.mJteeMapperAlias.set(result.mJteeBeanAlias.get() + "Mapper");
        result.mJteeServiceAlias.set(result.mJteeBeanAlias.get() + "Service");
        result.mJteeControllerBaseAlias.set(result.mJteeBeanAlias.get() + "ControllerBase");
        result.mJteeControllerPath.set(String.format("api/%s/", result.mId.get()));
        result.mJteeControllerAttachmentPath.set(String.format("api/%s_attachment/", result.mId.get()));
        result.mAndroidBeanAlias.set(StringUtils.capitalize(mId.getText()));
        result.mAndroidMaskAlias.set(result.mAndroidBeanAlias.get() + "Mask");
        result.mAndroidStubAlias.set(result.mAndroidBeanAlias.get() + "Stub");
        result.mAndroidViewAlias.set(result.mAndroidBeanAlias.get() + "View");
        result.mAndroidListViewAlias.set(result.mAndroidBeanAlias.get() + "ListView");
        mStage.close();
    }

    public Module getResult() {
        return result;
    }

    public static Module invoke(ApplicationController applicationController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/moduleNew.fxml"));
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
        ModuleNewController dialogController = fxmlLoader.getController();
        dialogController.setEnvironment(dialogStage, dialogScene, applicationController);
        dialogStage.showAndWait();
        return dialogController.getResult();
    }
}
