package com.enigma.main;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.main.tasks.GetGameStatus;
import com.enigma.main.tasks.GetMachinePart;
import com.enigma.main.tasks.SetAvailableTask;
import com.enigma.utils.UiAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Set;
import java.util.function.Consumer;

public class MainController {
    @FXML private Label teamNameLb;
    @FXML private Label agentStatusLb;
    @FXML private Label battlefieldNameLb;
    @FXML private Label uBoatNameLb;
    @FXML private Label gameStatusLb;
    @FXML private Label participantsLb;
    @FXML private Label difficultyLb;
    @FXML private Label percentageLb;
    @FXML private Label tasksAcceptedLb;
    @FXML private Label tasksPreformedLb;
    @FXML private TableView<?> candidatesTable;
    @FXML private TableColumn<?, ?> decryptedCol;
    @FXML private TableColumn<?, ?> configurationCol;
    @FXML private ProgressBar taskProgressBar;
    private UiAdapter uiAdapter;
    private MachineParts machineParts;
    private Set<String> dictionary;
    private Consumer<EnigmaParts> updateEnigmaParts;
    private Consumer<GameDetailsObject> updateGameStatus;
    private int numOfWorkers;

    @FXML
    void initialize(){
        this.updateEnigmaParts = (enigmaParts -> {
            this.machineParts = enigmaParts.getMachineParts();
            this.dictionary = enigmaParts.getDmParts().getDictionary();
        });
        this.updateGameStatus = (gameDetailsObject -> {
            switch (gameDetailsObject.getGameStatus()){
                case ENDING:
                    //todo end all threads
                case RUNNING:
                    updateGameStatusFields(gameDetailsObject);
                    uiAdapter.setIsInActiveGame(true);
                    break;
                case AWAITING:
                    updateGameStatusFields(gameDetailsObject);
                    break;
            }
        });
    }
    private void initComponent(){
        this.uBoatNameLb.setText("");
        this.gameStatusLb.setText("waiting to join for new game");
        this.battlefieldNameLb.setText("");
        this.participantsLb.setText("");
        this.difficultyLb.setText("");
        this.percentageLb.setText("0%");
        this.tasksPreformedLb.setText("0");
        this.tasksAcceptedLb.setText("0");
        this.taskProgressBar.setProgress(0);
    }
    private void updateGameStatusFields(GameDetailsObject gameDetailsObject){
        this.uBoatNameLb.setText(gameDetailsObject.getuBoatName());
        this.battlefieldNameLb.setText(gameDetailsObject.getBattlefieldName());
        this.participantsLb.setText(gameDetailsObject.getParticipantsStatus());
        this.difficultyLb.setText(gameDetailsObject.getDecryptionLevel());
        this.gameStatusLb.setText(gameDetailsObject.getGameStatus().toString());
    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindComponent();
    }

    private void getParts(){
        new Thread(new GetMachinePart(updateEnigmaParts, uiAdapter.isReadyProperty()::set)).start();
    }
    private void getGameStatus(){
        new Thread(new GetGameStatus(updateGameStatus, uiAdapter.isReadyProperty())).start();
    }

    private void bindComponent(){
        //one time set this thread when join in the middle of a game;
        if(!uiAdapter.isIsActive()){
            new Thread(new SetAvailableTask(uiAdapter.isActiveProperty()::set, agentStatusLb::setText)).start();
        }
        uiAdapter.isActiveProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                getParts();
            }
        });

        uiAdapter.isReadyProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                getParts();
            }if(newValue){
                getGameStatus();
            }
        });
    }

    public void setTeamNameLb(String teamName) {
        this.teamNameLb.setText(teamName);
    }

    public void setNumOfWorkers(int numOfWorkers) {
        this.numOfWorkers = numOfWorkers;
    }
}
