package com.enigma.main_component.contestComponent;

import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.main_component.contestComponent.tasks.GetGameStatus;
import com.enigma.utiles.UiAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class ContestComponentController {
    @FXML private Label battlefieldNameLb;
    @FXML private Label uBoatNameLb;
    @FXML private Label gameStatLb;
    @FXML private Label teamStatLb;
    @FXML private Label difficultyLb;
    @FXML private Label encryptedMessageLb;
    @FXML private Button doneBt;
    @FXML private TableView<?> participantsTable;
    @FXML private TableColumn<?, ?> teamNameCol;
    @FXML private TableColumn<?, ?> agentNumCol;
    @FXML private TableColumn<?, ?> taskSizeCol;
    @FXML private VBox agentProgressContainer;
    @FXML private TableView<?> candidatesTable;
    @FXML private TableColumn<?, ?> decryptionCol;
    @FXML private TableColumn<?, ?> configurationCol;

    private UiAdapter uiAdapter;
    private Consumer<GameDetailsObject> updateGameStatus;

    @FXML
    void initialize(){
        this.updateGameStatus = (gameDetailsObject)->{
            switch (gameDetailsObject.getGameStatus()){
                case AWAITING:
                    updateGameStatusFields(gameDetailsObject);
                    break;
                case RUNNING:
                    uiAdapter.setIsInActiveGame(true);
                    break;
                case ENDING:
                    uiAdapter.setIsGameEnded(true);
                    uiAdapter.setIsInActiveGame(false);
                    //Todo - get winner + revel done button when get

            }
        };
    }

    @FXML
    void doneButtonClicked(ActionEvent event) {
        //Todo init component
        uiAdapter.setIsReady(false);
        uiAdapter.setIsJoinToGame(false);
        uiAdapter.setIsTaskSet(false);
        uiAdapter.setIsGameEnded(false);
    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindAdapterToComponent();
    }

    private void bindAdapterToComponent(){
        uiAdapter.isReadyProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                getGameStatus();
        });
    }

    private void getGameStatus(){
        new Thread(new GetGameStatus(updateGameStatus, uiAdapter.isReadyProperty(), uiAdapter.isGameEndedProperty())).start();
    }

    private void updateGameStatusFields(GameDetailsObject gameDetailsObject){
        this.battlefieldNameLb.setText(gameDetailsObject.getBattlefieldName());
        this.uBoatNameLb.setText(gameDetailsObject.getuBoatName());
        this.gameStatLb.setText(gameDetailsObject.getGameStatus().toString());
        this.teamStatLb.setText(gameDetailsObject.getParticipantsStatus());
        this.difficultyLb.setText(gameDetailsObject.getDecryptionLevel());
        this.encryptedMessageLb.setText(gameDetailsObject.getEncryptedMessage() == null ? "": gameDetailsObject.getEncryptedMessage());
    }

}
