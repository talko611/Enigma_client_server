package com.enigma.main_component.dashboard_tab_component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class DashboardController {

    @FXML private ListView<?> agentsList;
    @FXML private TextField battlefieldNameTb;
    @FXML private TextField taskSizeTb;
    @FXML private Button joinButton;
    @FXML private Label userMessage;
    @FXML private Button readyButtonClicked;

    @FXML
    void joinButtonClicked(ActionEvent event) {

    }

    @FXML
    void readyButtonClicked(ActionEvent event) {

    }

}
