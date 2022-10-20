package com.enigma.main_component.dashboard_tab_component;

import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.main_component.dashboard_tab_component.tasks.GetBattlefieldsTask;
import com.enigma.main_component.dashboard_tab_component.tasks.GetMyAgentsTask;
import com.enigma.utiles.AppUtils;
import com.enigma.utiles.UiAdapter;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.*;
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
    @FXML private Button readyButton;
    @FXML private Button setButton;
    @FXML private Label userMessage;


    private Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable;
    private Consumer<List<String>> updateAgents;
    private UiAdapter uiAdapter;
    private Map<UUID, GameDetailsObject> battlefieldMap;
    private ObservableList<UiBattlefield> uiBattlefieldList;
    private UUID chosenBattlefield;
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
            battlefieldMap = (Map<UUID, GameDetailsObject>) data.getData();
            battlefieldMap.forEach((key,value)->{
                uiBattlefieldList.add(new UiBattlefield(value.getBattlefieldName(),
                                                        value.getuBoatName(),
                                                        value.getGameStatus().toString(),
                                                        value.getParticipantsStatus(),
                                                        value.getDecryptionLevel()));
            });
            battlefieldTb.setItems(uiBattlefieldList);
        };
        updateAgents = (agents)->{
            agentsList.getItems().clear();
            agentsList.getItems().addAll(agents);
        };
    }

    @FXML
    void clickOnTable(MouseEvent event) {
       ObservableList<UiBattlefield> observableList = battlefieldTb.getSelectionModel().getSelectedItems();
       if(!observableList.isEmpty()){
           for(Map.Entry<UUID, GameDetailsObject> object : battlefieldMap.entrySet()){
               if(object.getValue().getBattlefieldName().equals(observableList.get(0).battlefieldName.get())){
                   chosenBattlefield = object.getKey();
                   battlefieldNameTb.setText(object.getValue().getBattlefieldName());
               }
           }
       }
    }
    @FXML
    void joinButtonClicked(ActionEvent event) {
        if(chosenBattlefield != null){
            launchJoinRequest();
        }else{
            userMessage.setText("Please choose battlefield first");
        }
    }

    @FXML
    void readyButtonClicked(ActionEvent event) {
        launchSetReadyRequest();
    }

    private void launchSetReadyRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.SET_READY_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->{
                    userMessage.setText("Could not preform request please try again");
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 200){
                    Platform.runLater(()->{
                        uiAdapter.setIsReady(true);
                    });
                }else{
                    Platform.runLater(()->{
                        userMessage.setText("Cannot fulfill request please check if you sign to battlefield and set task size");
                    });
                }
            }
        });
    }

    @FXML
    void setTaskSizeClicked(ActionEvent event) {
        try {
            long taskSize = Long.parseLong(taskSizeTb.getText());
            if(taskSize > 0){
                launchSetTaskSizeRequest(taskSize);
            }
            userMessage.setText("Please enter only positive whole number");
        }catch (NumberFormatException e){
            userMessage.setText("Please enter only positive whole number");
        }

    }

    private void launchSetTaskSizeRequest(long taskSize){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.SET_TASK_SIZE_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString()).addQueryParameter("taskSize", String.valueOf(taskSize));
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->{
                    userMessage.setText("Couldn't complete request" );
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                RequestServerAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), RequestServerAnswer.class);
                Platform.runLater(()->{
                    uiAdapter.setIsTaskSet(answer.isSuccess());
                    userMessage.setText(answer.getMessage());
                });
            }
        });
    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindComponent();
    }

    private void bindComponent(){
        launchGetMyAgentsTask();
        launchGetBattlefieldsTask();
        uiAdapter.isReadyProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                launchGetBattlefieldsTask();
                launchGetMyAgentsTask();
            }
        });
        readyButton.disableProperty().bind(uiAdapter.isJoinToGameProperty().not().or(uiAdapter.isTaskSetProperty().not()));
        joinButton.disableProperty().bind(uiAdapter.isJoinToGameProperty());
        setButton.disableProperty().bind(uiAdapter.isTaskSetProperty().or(uiAdapter.isJoinToGameProperty().not()));
    }

    private void launchJoinRequest(){
        HttpUrl.Builder urlBuilder  = HttpUrl.parse(AppUtils.APP_URL + AppUtils.JOIN_BATTLEFIELD_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString())
                .addQueryParameter("battlefield", chosenBattlefield.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->{
                    userMessage.setText("Cannot fulfill request please try again ");
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                RequestServerAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), RequestServerAnswer.class);
                Platform.runLater(()->{
                    userMessage.setText(answer.getMessage());
                    uiAdapter.setIsJoinToGame(answer.isSuccess());
                });
            }
        });
    }

    private void launchGetBattlefieldsTask(){
        new Thread(new GetBattlefieldsTask(updateBattlefieldTable, uiAdapter.isReadyProperty())).start();
    }

    private void launchGetMyAgentsTask(){
        new Thread(new GetMyAgentsTask(updateAgents, uiAdapter.isReadyProperty())).start();
    }

    public List<String> getAgentsNames(){
        return new ArrayList<>(agentsList.getItems());
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
