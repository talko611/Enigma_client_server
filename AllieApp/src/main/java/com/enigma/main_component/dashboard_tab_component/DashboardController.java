package com.enigma.main_component.dashboard_tab_component;

import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.main_component.tasks.GetBattlefieldsTask;
import com.enigma.main_component.tasks.GetMyAgentTask;
import com.enigma.utiles.AppUtils;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;
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
    private Consumer<List<String>> updateAgents;
    private SimpleBooleanProperty isInActiveGame;
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
                                                        value.getGameStatus(),
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
       for(Map.Entry<UUID, GameDetailsObject> object : battlefieldMap.entrySet()){
           if(object.getValue().getBattlefieldName().equals(observableList.get(0).battlefieldName.get())){
               chosenBattlefield = object.getKey();
               battlefieldNameTb.setText(object.getValue().getBattlefieldName());
           }
       }
    }
    @FXML
    void joinButtonClicked(ActionEvent event) {
        if(validateJoinRequest()){
            joinButton.disableProperty().set(true);
            launchRequest();
        }
    }

    @FXML
    void readyButtonClicked(ActionEvent event) {

    }

    public void setIsInActiveGame(SimpleBooleanProperty isInActiveGame) {
        this.isInActiveGame = isInActiveGame;
        bindComponent();
    }

    private void bindComponent(){
        new Thread(new GetBattlefieldsTask(updateBattlefieldTable, isInActiveGame)).start();
        new Thread(new GetMyAgentTask(updateAgents, isInActiveGame)).start();
        isInActiveGame.addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                new Thread(new GetBattlefieldsTask(updateBattlefieldTable, isInActiveGame)).start();
                new Thread(new GetMyAgentTask(updateAgents, isInActiveGame)).start();
            }
        });
    }

    private boolean validateJoinRequest(){
        boolean answer = true;
        try{
            if(chosenBattlefield == null){
                userMessage.setText("Please choose battlefield first");
                answer = false;
            } else if (Long.parseLong(taskSizeTb.getText()) <= 0) {
                answer = false;
                userMessage.setText("Please enter only positive number in task size box");
            }
        }catch (NumberFormatException e){
            answer = false;
            userMessage.setText("Please enter only positive number in task size box");
        }
        return answer;
    }

    private void launchRequest(){
        HttpUrl.Builder urlBuilder  = HttpUrl.parse(AppUtils.APP_URL + AppUtils.JOIN_BATTLEFIELD_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString())
                .addQueryParameter("battlefield", chosenBattlefield.toString())
                .addQueryParameter("taskSize", String.valueOf(Long.parseLong(taskSizeTb.getText())));
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(e!=null){
                    System.out.println(e.getMessage());
                    System.out.println(e.getCause());
                    System.out.println(e.getStackTrace());
                }
                Platform.runLater(()->{
                    userMessage.setText("Cannot fulfill request please try again ");
                    joinButton.disableProperty().set(false);
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                RequestServerAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), RequestServerAnswer.class);
                Platform.runLater(()->{
                    userMessage.setText(answer.getMessage());
                    joinButton.disableProperty().set(false);
                    //Todo -  update property of is join
                });
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
