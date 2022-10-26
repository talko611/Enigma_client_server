package com.enigma;

import com.enigma.login.LoginController;
import com.enigma.main.MainController;
import com.enigma.utils.UiAdapter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class FrameController {
    private BorderPane frameComponent;
    private UiAdapter uiAdapter;
    @FXML private VBox loginComponent;
    @FXML private LoginController loginComponentController;
    @FXML private HBox headerContainer;

    @FXML
    void initialize(){
        this.uiAdapter = new UiAdapter();
        loginComponentController.setUiAdapter(this.uiAdapter);
        uiAdapter.isLoggedInProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                FXMLLoader loader = new FXMLLoader();
                URL url = getClass().getResource("/main/main_component_layout.fxml");
                loader.setLocation(url);
                try {
                    Parent parent = loader.load();
                    frameComponent.setCenter(parent);
                    MainController controller = loader.getController();
                    controller.setUiAdapter(uiAdapter);
                    controller.setNumOfWorkers(loginComponentController.getThreadsNum());
                    controller.setTeamNameLb(loginComponentController.getTeamName());
                    controller.setAgentNameLb(loginComponentController.getAgentName());
                } catch (IOException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        });
    }

    public void setFrameComponent(BorderPane frameComponent) {
        this.frameComponent = frameComponent;
    }

}
