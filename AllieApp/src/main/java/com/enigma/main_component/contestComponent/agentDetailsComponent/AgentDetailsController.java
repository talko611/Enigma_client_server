package com.enigma.main_component.contestComponent.agentDetailsComponent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.text.NumberFormat;

public class AgentDetailsController {
    @FXML private Label agentNameLb;
    @FXML private Label assignedTaskLb;
    @FXML private Label acceptedTasksLb;
    @FXML private ProgressBar agentProgressBar;
    @FXML private Label progressBarLb;
    @FXML private Label producedCandidatesLb;
    private NumberFormat nf;

    @FXML
    void initialize(){
        this.nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
    }

    public void setAgentName(String name){
        this.agentNameLb.setText(name);
    }

    public void setAssignedTaskLb(String assignedTasks){
        this.assignedTaskLb.setText(assignedTasks);
    }
    public void setProducedCandidatesLb(String producedCandidates){
        this.producedCandidatesLb.setText(producedCandidates);
    }

    public void setAcceptedTasksLb(String acceptedTasks) {
        this.acceptedTasksLb.setText(acceptedTasks);
    }

    public void setProgress(double progress){
        this.agentProgressBar.setProgress(progress);
        this.progressBarLb.setText(nf.format(progress * 100) + "%");
    }
}
