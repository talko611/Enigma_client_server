package com.enigma.main;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.enigma.dtos.dataObjects.Candidate;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.main.tasks.*;
import com.enigma.utils.AppUtils;
import com.enigma.utils.UiAdapter;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    @FXML private TableView<UiCandidate> candidatesTable;
    @FXML private TableColumn<?, ?> decryptedCol;
    @FXML private TableColumn<?, ?> configurationCol;
    @FXML private ProgressBar taskProgressBar;
    private UiAdapter uiAdapter;
    private MachineParts machineParts;
    private Set<String> dictionary;
    private Consumer<EnigmaParts> updateEnigmaParts;
    private Consumer<GameDetailsObject> updateGameStatus;
    private Consumer<Long> updateProgress;
    private Consumer<Candidate> reportCandidateFound;
    private SimpleLongProperty tasksAccepted;
    private SimpleLongProperty tasksPreformed;
    private ObservableList<UiCandidate> uiCandidatesList;
    private int numOfThreads;
    private ExecutorService executorService;

    @FXML
    void initialize(){
        this.decryptedCol.setCellValueFactory(new PropertyValueFactory<>("decryption"));
        this.configurationCol.setCellValueFactory(new PropertyValueFactory<>("configuration"));
        this.uiCandidatesList = FXCollections.observableArrayList();
        this.candidatesTable.setItems(uiCandidatesList);
        this.tasksPreformed = new SimpleLongProperty();
        this.tasksPreformedLb.textProperty().bind(tasksPreformed.asString());
        this.tasksAccepted = new SimpleLongProperty();
        this.tasksAcceptedLb.textProperty().bind(tasksAccepted.asString());
        this.updateEnigmaParts = (enigmaParts -> {
            this.machineParts = enigmaParts.getMachineParts();
            this.dictionary = enigmaParts.getDmParts().getDictionary();
        });
        this.updateGameStatus = (gameDetailsObject -> {
            switch (gameDetailsObject.getGameStatus()){
                case ENDING:
                    this.executorService.shutdownNow();
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
        this.updateProgress = (tasksPreformed)->{
            double progress = (double) Math.round(this.tasksPreformed.get()/ (double) this.tasksAccepted.get() * 1000) / 1000;
            this.taskProgressBar.setProgress(progress);
            this.percentageLb.setText(progress * 100 + "%");
        };
        this.reportCandidateFound = (candidate) ->{
            uiCandidatesList.add(new UiCandidate(candidate.getDecryption(), candidate.getConfiguration()));
        };
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

        uiAdapter.isInActiveGameProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                getTasks();
        });
    }

    private void getTasks(){
        this.executorService = Executors.newFixedThreadPool(this.numOfThreads);
        new Thread(new GetDecryptionTasks(uiAdapter.isGameEndedProperty(),
                this.tasksPreformed,
                this.tasksAccepted,
                this.updateProgress,
                this.reportCandidateFound,
                this.teamNameLb.getText(),
                this.dictionary,
                this.machineParts,
                this.executorService)).start();
    }

    public void setTeamNameLb(String teamName) {
        this.teamNameLb.setText(teamName);
    }

    public void setNumOfWorkers(int numOfWorkers) {
        this.numOfThreads = numOfWorkers;
    }

    public static class UiCandidate{
        private SimpleStringProperty decryption;
        private SimpleStringProperty configuration;

        public UiCandidate(String decryption, String configuration) {
            this.decryption = new SimpleStringProperty(decryption);
            this.configuration = new SimpleStringProperty(configuration);
        }

        public String getDecryption() {
            return decryption.get();
        }

        public SimpleStringProperty decryptionProperty() {
            return decryption;
        }

        public String getConfiguration() {
            return configuration.get();
        }

        public SimpleStringProperty configurationProperty() {
            return configuration;
        }

        public void setDecryption(String decryption) {
            this.decryption.set(decryption);
        }

        public void setConfiguration(String configuration) {
            this.configuration.set(configuration);
        }
    }
}
