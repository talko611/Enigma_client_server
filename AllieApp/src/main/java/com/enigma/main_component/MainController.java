package com.enigma.main_component;

import com.enigma.main_component.contestComponent.ContestComponentController;
import com.enigma.main_component.dashboard_tab_component.DashboardController;
import com.enigma.utiles.UiAdapter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML private TabPane mainTabPane;
    @FXML private Tab dashboardTab;
    @FXML private Tab contestTab;
    @FXML private VBox dashboardComponent;
    @FXML private DashboardController dashboardComponentController;
    @FXML private ScrollPane contestComponent;
    @FXML private ContestComponentController contestComponentController;

    private BorderPane frameComponent;
    private UiAdapter uiAdapter;


    @FXML
    void initialize(){
        this.uiAdapter = new UiAdapter();
        dashboardComponentController.setUiAdapter(uiAdapter);
        contestComponentController.setUiAdapter(uiAdapter);
        contestComponentController.setDashboardController(dashboardComponentController);
        uiAdapter.isReadyProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                contestTab.disableProperty().set(false);
                mainTabPane.getSelectionModel().select(contestTab);
                dashboardTab.disableProperty().set(true);

            }else{
                dashboardTab.disableProperty().set(false);
                contestTab.disableProperty().set(true);
                mainTabPane.getSelectionModel().select(dashboardTab);
            }
        });
    }

    public void setFrameComponent(BorderPane frameComponent) {
        this.frameComponent = frameComponent;
    }
}
