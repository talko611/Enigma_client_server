package com.enigma;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class UBoatApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/uBoatApp_mainFrame.fxml");
        loader.setLocation(url);
        ScrollPane root = loader.load();
        FrameController controller = loader.getController();
        controller.setFrameComponent((BorderPane) root.getContent());
        controller.setPrimaryStage(primaryStage);
        Scene scene  = new Scene(root, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
