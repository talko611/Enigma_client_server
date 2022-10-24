package com.enigma;

import com.enigma.login_screen.LoginController;
import com.enigma.main_screen.MainController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class FrameController {

    private BorderPane frameComponent;
    private SimpleBooleanProperty isLoggedIn;
    private Stage primaryStage;
    @FXML private VBox loginComponent;
    @FXML private LoginController loginComponentController;


    @FXML void initialize(){
        this.isLoggedIn = new SimpleBooleanProperty(false);
        this.loginComponentController.setIsLoginSuccessful(isLoggedIn);
        isLoggedIn.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/mainApp/uBoat_app_Layout.fxml"));
                try {
                    Parent root = loader.load();
                    MainController mainController = loader.getController();
                    mainController.setName(loginComponentController.getName());
                    mainController.setLoggedInProperty(isLoggedIn);
                    mainController.setPrimaryStage(this.primaryStage);
                    frameComponent.setCenter(root);
                } catch (IOException e) {
                    System.out.println("Cannot load app");
                }
                System.out.println("load after successful login");
            }else {
                loginComponentController.resetComponent();
                frameComponent.setCenter(loginComponent);
            }
        });
    }

    public void setFrameComponent(BorderPane frameComponent) {
        this.frameComponent = frameComponent;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
