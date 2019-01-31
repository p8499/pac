package com.p8499.pacu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.p8499.pacu.ProjectProvider;
import com.p8499.pacu.model.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 6/15/2018.
 */
public class ApplicationController implements Initializable, EnvironmentContainer {
    public Stage mStage;
    public Scene mScene;
    public BooleanProperty isModified;
    public ObjectProperty<File> mProjectFile;
    public ObjectProperty<File> mDatabaseFolder;
    public ObjectProperty<File> mJteeFolder;
    public ObjectProperty<File> mAndroidFolder;
    @FXML
    private BorderPane mBorder;
    @FXML
    private TreeView mTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isModified = new SimpleBooleanProperty();
        isModified.addListener(((observable, oldValue, newValue) -> setStageTitle()));
        mProjectFile = new SimpleObjectProperty<>();
        mProjectFile.addListener(((observable, oldValue, newValue) -> setStageTitle()));
        mDatabaseFolder = new SimpleObjectProperty<>();
        mJteeFolder = new SimpleObjectProperty<>();
        mAndroidFolder = new SimpleObjectProperty<>();
        mTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof TreeItem)
                showContent((TreeItemContainer) ((TreeItem) newValue).getValue());
        });
    }

    @Override
    public void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController) {
        mStage = stage;
        mScene = scene;
        try {
            onEmptyClick();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectTreeItem(TreeItem treeItem) {
        if (!treeItem.getParent().isExpanded())
            treeItem.getParent().setExpanded(true);
        mTree.getSelectionModel().select(mTree.getRow(treeItem));
    }

    public void onEmptyClick() throws IOException {
        boolean save = false, stop = false;
        if (isModified.get()) {
            Boolean confirm = ConfirmController.invoke(this, "Save your project?");
            save = confirm != null && confirm.booleanValue();
            stop = confirm == null;
        }
        if (save)
            save(mProjectFile.get() == null ? getOutputSourceFile() : mProjectFile.get());
        if (!stop)
            create(ProjectProvider.empty());
    }

    public void onSampleClick() throws IOException {
        boolean save = false, stop = false;
        if (isModified.get()) {
            Boolean confirm = ConfirmController.invoke(this, "Save your project?");
            save = confirm != null && confirm.booleanValue();
            stop = confirm == null;
        }
        if (save)
            save(mProjectFile.get() == null ? getOutputSourceFile() : mProjectFile.get());
        if (!stop)
            create(ProjectProvider.sample());
    }

    public void onOpenClick() throws IOException {
        boolean save = false, stop = false;
        if (isModified.get()) {
            Boolean confirm = ConfirmController.invoke(this, "Save your project?");
            save = confirm != null && confirm.booleanValue();
            stop = confirm == null;
        }
        if (save)
            save(mProjectFile.get() == null ? getOutputSourceFile() : mProjectFile.get());
        if (!stop)
            open(getInputSourceFile());
    }

    public void onSaveClick() throws IOException {
        if (mProjectFile.get() == null)
            save(getOutputSourceFile());
        else
            save(mProjectFile.get());
    }

    public void onSaveAsClick() throws IOException {
        save(getOutputSourceFile());
    }

    public void onDatabaseClick() throws Exception {
        generateDatabase(getOutputFolder());
    }

    public void onJteeClick() throws Exception {
        generateJtee(getOutputFolder());
    }

    public void onAndroidClick() throws Exception {
        generateAndroid(getOutputFolder());
    }

    public void onTestClick() throws Exception {
        generateTest(getOutputFolder());
    }

    private File getInputSourceFile() {
        //return a source file to read
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(mProjectFile.get() != null ? mProjectFile.get().getParentFile() : null);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All types", "*.*"),
                new FileChooser.ExtensionFilter("JSON file", "*.json"),
                new FileChooser.ExtensionFilter("Normal text file", "*.txt"));
        return fileChooser.showOpenDialog(mStage);
    }

    private File getOutputSourceFile() {
        //return a target file to write
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(mProjectFile.get() != null ? mProjectFile.get().getParentFile() : null);
        fileChooser.setInitialFileName(mProjectFile.get() != null ? mProjectFile.get().getName() : ((Project) mStage.getUserData()).mName.get());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON file", "*.json"),
                new FileChooser.ExtensionFilter("Normal text file", "*.txt"),
                new FileChooser.ExtensionFilter("All types", "*.*"));
        return fileChooser.showSaveDialog(mStage);
    }

    private File getOutputFolder() {
        //return a target file to write
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setInitialDirectory(mJteeFolder.get() != null ? mJteeFolder.get().getParentFile() : null);
        return fileChooser.showDialog(mStage);
    }

    private void create(Project project) {
        //create a project and update the application
        if (project != null) {
            mProjectFile.set(null);
            mStage.setUserData(project);
            mTree.setRoot(((TreeItemContainer) mStage.getUserData()).getTreeItem());
            mBorder.setCenter(null);
            isModified.set(false);
            setStageTitle();
        }
    }

    private void open(File file) throws IOException {
        //open a project and update the application
        if (file != null) {
            mProjectFile.set(file);
            mStage.setUserData(ProjectProvider.load(mProjectFile.get()));
            mTree.setRoot(((TreeItemContainer) mStage.getUserData()).getTreeItem());
            mBorder.setCenter(null);
            isModified.set(false);
            setStageTitle();
        }
    }

    private void save(File file) throws IOException {
        //save project to that file and update the application
        if (file != null) {
            mProjectFile.set(file);
            new ObjectMapper().writeValue(mProjectFile.get(), mStage.getUserData());
            isModified.set(false);
        }
    }

    private void generateDatabase(File folder) throws Exception {
        if (folder != null) {
            Project project = (Project) mStage.getUserData();
            com.p8499.paca.Main.generateDatabase(
                    (Map) Configuration.defaultConfiguration().jsonProvider().parse(new ObjectMapper().writeValueAsString(project)),
                    new File(folder, String.format("%s_scripts", project.mName.get())));
        }
    }

    private void generateJtee(File folder) throws Exception {
        if (folder != null) {
            Project project = (Project) mStage.getUserData();
            EnvJtee envJtee = project.mEnvJtee.get();
            com.p8499.paca.Main.generateJtee(
                    (Map) Configuration.defaultConfiguration().jsonProvider().parse(new ObjectMapper().writeValueAsString(project)),
                    new File(folder, envJtee.mApp.get()));
        }
    }

    private void generateAndroid(File folder) throws Exception {
        if (folder != null) {
            Project project = (Project) mStage.getUserData();
            com.p8499.paca.Main.generateAndroid(
                    (Map) Configuration.defaultConfiguration().jsonProvider().parse(new ObjectMapper().writeValueAsString(project)),
                    new File(folder, String.format("gen")));
        }
    }

    private void generateTest(File folder) throws Exception {
        if (folder != null) {
            Project project = (Project) mStage.getUserData();
            com.p8499.paca.Main.generateTest(
                    (Map) Configuration.defaultConfiguration().jsonProvider().parse(new ObjectMapper().writeValueAsString(project)),
                    new File(folder, String.format("%s_test", project.mName.get())));
        }
    }

    private void setStageTitle() {
        mStage.setTitle(String.format("%s%s - PAC Workbench", isModified.get() ? "*" : "", mProjectFile.get() == null ? ((Project) mStage.getUserData()).mName.get() : mProjectFile.get().getName()));
    }

    public void showContent(TreeItemContainer object) {
        String location = null;
        if (object instanceof Project)
            location = "com/p8499/pacu/view/project.fxml";
        else if (object instanceof EnvJtee)
            location = "com/p8499/pacu/view/envJtee.fxml";
        else if (object instanceof Datasources)
            location = "com/p8499/pacu/view/datasources.fxml";
        else if (object instanceof Datasource)
            location = "com/p8499/pacu/view/datasource.fxml";
        else if (object instanceof EnvAndroid)
            location = "com/p8499/pacu/view/envAndroid.fxml";
        else if (object instanceof Modules)
            location = "com/p8499/pacu/view/modules.fxml";
        else if (object instanceof Module)
            location = "com/p8499/pacu/view/module.fxml";
        else if (object instanceof Fields)
            location = "com/p8499/pacu/view/fields.fxml";
        else if (object instanceof Field)
            location = "com/p8499/pacu/view/field.fxml";
        else if (object instanceof Values)
            location = "com/p8499/pacu/view/values.fxml";
        else if (object instanceof Value)
            location = "com/p8499/pacu/view/value.fxml";
        else if (object instanceof Uniques)
            location = "com/p8499/pacu/view/uniques.fxml";
        else if (object instanceof Unique)
            location = "com/p8499/pacu/view/unique.fxml";
        else if (object instanceof References)
            location = "com/p8499/pacu/view/references.fxml";
        else if (object instanceof Reference)
            location = "com/p8499/pacu/view/reference.fxml";
        if (location != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource(location));
            mScene.setUserData(object);
            Node node = null;
            try {
                node = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fxmlLoader.<EnvironmentContainer>getController().setEnvironment(mStage, mScene, this);
            mBorder.setCenter(node);
        }
    }
}
