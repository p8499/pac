package com.p8499.pacu;

import com.p8499.pacu.controller.ApplicationController;
import com.p8499.pacu.controller.ProjectController;
import com.p8499.pacu.model.Project;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Administrator on 6/12/2018.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    //only 1 key for per module
    //serial unique has only one item
    //databaseTable/View can not be t[0-9]+
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("com/p8499/pacu/view/application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxmlLoader.<ApplicationController>getController().setEnvironment(primaryStage, scene, fxmlLoader.getController());

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
