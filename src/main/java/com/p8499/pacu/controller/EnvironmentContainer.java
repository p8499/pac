package com.p8499.pacu.controller;

import com.p8499.pacu.controller.ApplicationController;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Administrator on 6/15/2018.
 */
public interface EnvironmentContainer {
    void setEnvironment(Stage stage, Scene scene, ApplicationController applicationController);
}
