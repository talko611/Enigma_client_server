package com.enigma;

import com.enigma.login_component.LoginController;
import com.enigma.main_component.MainController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class FrameController {
    private BorderPane frameComponent;
    private SimpleBooleanProperty isLoginSuccessful;
    private Stage primaryStage;
    @FXML private VBox loginComponent;
    @FXML private LoginController loginComponentController;


    @FXML void initialize(){
        isLoginSuccessful = new SimpleBooleanProperty(false);
        loginComponentController.setIsLoginSuccessful(isLoginSuccessful);
        isLoginSuccessful.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                FXMLLoader loader = new FXMLLoader();
                URL url = getClass().getResource("/main/main_layout.fxml");
                loader.setLocation(url);
                try{
                    Parent parent = loader.load();
                    MainController controller = loader.getController();
                    controller.setMyName(loginComponentController.getName());
                    controller.setFrameComponent(frameComponent);
                    frameComponent.setCenter(parent);
                }catch (IOException e){
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
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
