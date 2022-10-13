package com.enigma.main_component;

import com.enigma.main_component.dashboard_tab_component.DashboardController;
import com.enigma.utiles.UiAdapter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
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

    private BorderPane frameComponent;
    private UiAdapter uiAdapter;


    @FXML
    void initialize(){
        this.uiAdapter = new UiAdapter();
        dashboardComponentController.setUiAdapter(uiAdapter);
    }

    public void setFrameComponent(BorderPane frameComponent) {
        this.frameComponent = frameComponent;
    }
}
