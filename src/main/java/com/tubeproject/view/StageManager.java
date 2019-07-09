package com.tubeproject.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {

    public static void changeStage(Node rootPane, String screen) {
        sceneChange(rootPane, screen, null);
    }

    public static void changeStage(Node rootPane, String screen, String stylesheet) {
        sceneChange(rootPane, screen, stylesheet);
    }

    private static void sceneChange(Node rootPane, String screen, String stylesheet) {
        AnchorPane homePage;
        try {
            homePage = FXMLLoader.load(StageManager.class.getResource(screen));

        } catch (IOException e) {
            System.out.println("Error while loading FXML");
            return;
        }
        Scene homeScene = new Scene(homePage);
        Stage homeStage = (Stage) rootPane.getScene().getWindow();
        if (stylesheet != null)
            homeScene.getStylesheets().add(StageManager.class.getResource(stylesheet).toExternalForm());
        homeStage.setScene(homeScene);
        homeStage.show();
    }
}
