package com.enigma;

import com.enigma.login.LoginController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class FrameController {
    private BorderPane frameComponent;
    private SimpleBooleanProperty isLoggedIn;
    @FXML private VBox loginComponent;
    @FXML private LoginController loginComponentController;

    @FXML
    void initialize(){
        isLoggedIn = new SimpleBooleanProperty();
        loginComponentController.setIsLoggedIn(this.isLoggedIn);
        isLoggedIn.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                FXMLLoader loader = new FXMLLoader();
                URL url = getClass().getResource("/main/main_component_layout.fxml");
                loader.setLocation(url);
                try {
                    Parent parent = loader.load();
                    frameComponent.setCenter(parent);
                } catch (IOException e) {
                    System.out.println(e.getCause());
                    System.out.println(e.getStackTrace());
                }
            }
        });
    }

    public void setFrameComponent(BorderPane frameComponent) {
        this.frameComponent = frameComponent;
    }
}
