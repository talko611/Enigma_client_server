package com.enigma.main_component.contestComponent.agentDetailsComponent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class AgentDetailsController {
    @FXML private Label agentNameLb;
    @FXML private Label assignedTaskLb;
    @FXML private Label acceptedTasksLb;
    @FXML private ProgressBar agentProgressBar;
    @FXML private Label progressBarLb;
    @FXML private Label producedCandidatesLb;

    public void setAgentName(String name){
        this.agentNameLb.setText(name);
    }

    public void setAssignedTaskLb(String assignedTasks){
        this.assignedTaskLb.setText(assignedTasks);
    }

    public void setAcceptedTasksLb(String acceptedTasks) {
        this.acceptedTasksLb.setText(acceptedTasks);
    }

    public void setProgress(double progress){
        this.agentProgressBar.setProgress(progress);
        this.progressBarLb.setText(progress * 100 +"%");
    }
}
