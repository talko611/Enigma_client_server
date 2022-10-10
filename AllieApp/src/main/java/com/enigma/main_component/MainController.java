package com.enigma.main_component;

import com.enigma.main_component.dashboard_tab_component.DashboardController;
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

    public void setFrameComponent(BorderPane frameComponent) {
        this.frameComponent = frameComponent;
    }
}
