package com.enigma.main_component.dashboard_tab_component;

import com.enigma.dtos.ServletAnswers.GameDetailsObject;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.main_component.tasks.GetMyAgentsTask;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class DashboardController {
    @FXML private TableView<UiBattlefield> battlefieldTb;
    @FXML private TableColumn<?, ?> nameCol;
    @FXML private TableColumn<?, ?> uBoatCol;
    @FXML private TableColumn<?, ?> gameStatCol;
    @FXML private TableColumn<?, ?> participantsStatCol;
    @FXML private TableColumn<?,?> decryptionLevelCol;
    @FXML private ListView<String> agentsList;
    @FXML private TextField battlefieldNameTb;
    @FXML private TextField taskSizeTb;
    @FXML private Button joinButton;
    @FXML private Label userMessage;
    @FXML private Button readyButtonClicked;

    private Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable;
    private SimpleBooleanProperty isInActiveGame;
    private Map<UUID, GameDetailsObject> battlefieldMap;
    private ObservableList<UiBattlefield> uiBattlefieldList;
    @FXML void initialize(){
        //Battlefields table set up
        nameCol.setCellValueFactory(new PropertyValueFactory<>("battlefieldName"));
        uBoatCol.setCellValueFactory(new PropertyValueFactory<>("uBoatName"));
        gameStatCol.setCellValueFactory(new PropertyValueFactory<>("gameStatus"));
        participantsStatCol.setCellValueFactory(new PropertyValueFactory<>("participantStatus"));
        decryptionLevelCol.setCellValueFactory(new PropertyValueFactory<>("decryptionLevel"));
        uiBattlefieldList = FXCollections.observableArrayList();
        battlefieldTb.setItems(uiBattlefieldList);

        updateBattlefieldTable = (data) ->{
            uiBattlefieldList.clear();
            battlefieldMap = (Map<UUID, GameDetailsObject>) data.getListOfUsers();
            battlefieldMap.forEach((key,value)->{
                uiBattlefieldList.add(new UiBattlefield(value.getBattlefieldName(),
                                                        value.getuBoatName(),
                                                        value.getGameStatus(),
                                                        value.getParticipantsStatus(),
                                                        value.getDecryptionLevel()));
            });
            battlefieldTb.setItems(uiBattlefieldList);
        };
    }
    @FXML
    void joinButtonClicked(ActionEvent event) {

    }

    @FXML
    void readyButtonClicked(ActionEvent event) {

    }

    public void setIsInActiveGame(SimpleBooleanProperty isInActiveGame) {
        this.isInActiveGame = isInActiveGame;
        bindComponent();
    }

    private void bindComponent(){
        new Thread(new GetMyAgentsTask(updateBattlefieldTable, isInActiveGame)).start();
        isInActiveGame.addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                new Thread(new GetMyAgentsTask(updateBattlefieldTable, isInActiveGame)).start();
            }
        });
    }

    public static class UiBattlefield{
        private SimpleStringProperty battlefieldName;
        private SimpleStringProperty uBoatName;
        private SimpleStringProperty gameStatus;
        private SimpleStringProperty participantStatus;
        private SimpleStringProperty decryptionLevel;

        public UiBattlefield(String battlefieldName, String uBoatName, String gameStatus, String participantsStatus, String decryptionLevel){
            this.battlefieldName = new SimpleStringProperty(battlefieldName);
            this.uBoatName = new SimpleStringProperty(uBoatName);
            this.gameStatus = new SimpleStringProperty(gameStatus);
            this.participantStatus = new SimpleStringProperty(participantsStatus);
            this.decryptionLevel = new SimpleStringProperty(decryptionLevel);
        }

        public void setDecryptionLevel(String decryptionLevel) {
            this.decryptionLevel.set(decryptionLevel);
        }

        public String getDecryptionLevel() {
            return decryptionLevel.get();
        }

        public SimpleStringProperty decryptionLevelProperty() {
            return decryptionLevel;
        }

        public String getBattlefieldName() {
            return battlefieldName.get();
        }

        public SimpleStringProperty battlefieldNameProperty() {
            return battlefieldName;
        }

        public String getuBoatName() {
            return uBoatName.get();
        }

        public SimpleStringProperty uBoatNameProperty() {
            return uBoatName;
        }

        public String getGameStatus() {
            return gameStatus.get();
        }

        public SimpleStringProperty gameStatusProperty() {
            return gameStatus;
        }

        public String getParticipantStatus() {
            return participantStatus.get();
        }

        public SimpleStringProperty participantStatusProperty() {
            return participantStatus;
        }

        public void setBattlefieldName(String battlefieldName) {
            this.battlefieldName.set(battlefieldName);
        }

        public void setuBoatName(String uBoatName) {
            this.uBoatName.set(uBoatName);
        }

        public void setGameStatus(String gameStatus) {
            this.gameStatus.set(gameStatus);
        }

        public void setParticipantStatus(String participantStatus) {
            this.participantStatus.set(participantStatus);
        }
    }

}
