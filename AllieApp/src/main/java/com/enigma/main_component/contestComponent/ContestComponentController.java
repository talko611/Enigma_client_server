package com.enigma.main_component.contestComponent;

import com.enigma.dtos.dataObjects.AllieData;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.main_component.contestComponent.agentDetailsComponent.AgentDetailsController;
import com.enigma.main_component.contestComponent.tasks.GetGameStatus;
import com.enigma.main_component.contestComponent.tasks.GetParticipantsTask;
import com.enigma.main_component.dashboard_tab_component.DashboardController;
import com.enigma.utiles.AppUtils;
import com.enigma.utiles.UiAdapter;
import com.squareup.okhttp.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ContestComponentController {
    @FXML private Label battlefieldNameLb;
    @FXML private Label uBoatNameLb;
    @FXML private Label gameStatLb;
    @FXML private Label teamStatLb;
    @FXML private Label difficultyLb;
    @FXML private Label encryptedMessageLb;
    @FXML private Label winnerLb;
    @FXML private Button doneBt;
    @FXML private TableView<UiAllie> participantsTable;
    @FXML private TableColumn<?, ?> teamNameCol;
    @FXML private TableColumn<?, ?> agentNumCol;
    @FXML private TableColumn<?, ?> taskSizeCol;
    @FXML private VBox agentProgressContainer;
    @FXML private TableView<?> candidatesTable;
    @FXML private TableColumn<?, ?> decryptionCol;
    @FXML private TableColumn<?, ?> configurationCol;

    private UiAdapter uiAdapter;
    private Consumer<GameDetailsObject> updateGameStatus;
    private Consumer<List<AllieData>> updateParticipantsList;
    private ObservableList<UiAllie> participants;
    private DashboardController dashboardController;
    private Map<String, AgentDetailsController> agentNameToComponentController;

    @FXML
    void initialize(){
        this.teamNameCol.setCellValueFactory( new PropertyValueFactory<>("teamName"));
        this.agentNumCol.setCellValueFactory(new PropertyValueFactory<>("agentNum"));
        this.taskSizeCol.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        this.participants = FXCollections.observableArrayList();
        this.updateGameStatus = (gameDetailsObject)->{
            switch (gameDetailsObject.getGameStatus()){
                case AWAITING:
                    updateGameStatusFields(gameDetailsObject);
                    break;
                case RUNNING:
                    updateGameStatusFields(gameDetailsObject);
                    uiAdapter.setIsInActiveGame(true);
                    break;
                case ENDING:
                    uiAdapter.setIsGameEnded(true);
                    uiAdapter.setIsInActiveGame(false);
                    //Todo - get winner + revel done button when get

            }
        };
        this.updateParticipantsList = (alliDataList)->{
          participants.clear();
          alliDataList.forEach(allieData -> {
              participants.add(new UiAllie(allieData.getAlliName(), allieData.getNumOfAgents(), allieData.getTaskSize()));
          });
          this.participantsTable.setItems(participants);
        };
        this.agentNameToComponentController = new HashMap<>();
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

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void bindAdapterToComponent(){
        uiAdapter.isReadyProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                getGameStatus();
                launchGetParticipantsTask();
                loadAgentDetailsComponents();
            }
        });
        uiAdapter.isInActiveGameProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                launchStartProducingTasksRequest();
            }
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

    private void launchGetParticipantsTask(){
        new Thread(new GetParticipantsTask(uiAdapter.isInActiveGameProperty(), this.updateParticipantsList)).start();
    }

    private void loadAgentDetailsComponents(){
        List<String> agentsNames = this.dashboardController.getAgentsNames();
        URL componentResource = getClass().getResource("/main/contest/agent_details_component/agent_details_component_layout.fxml");
        String cssLayout = "-fx-border-color: red;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 2;\n";
        agentsNames.forEach(name->{
            FXMLLoader loader = new FXMLLoader(componentResource);
            try {
                VBox component = loader.load();
                component.setStyle(cssLayout);
                AgentDetailsController controller = loader.getController();
                controller.setAgentName(name);
                this.agentNameToComponentController.put(name, controller);
                agentProgressContainer.getChildren().add(component);
                VBox.setMargin(component, new Insets(0,0,10,0));
            } catch (IOException e) {
                System.out.println("Couldn't load Agent " + name + " component");
            }
        });
    }

    private void launchStartProducingTasksRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.START_PRODUCING_TASKS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Allie app: start producing request has failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() != 200){
                    System.out.println("Allie app : start producing request has been denied");
                }
            }
        });
    }

    public static class UiAllie{
        private SimpleStringProperty teamName;
        private SimpleIntegerProperty agentNum;
        private SimpleLongProperty taskSize;

        public UiAllie(String teamName, int agentNum, long taskSize) {
            this.teamName = new SimpleStringProperty(teamName);
            this.agentNum = new SimpleIntegerProperty(agentNum);
            this.taskSize = new SimpleLongProperty(taskSize);
        }

        public void setTeamName(String teamName) {
            this.teamName.set(teamName);
        }

        public void setAgentNum(int agentNum) {
            this.agentNum.set(agentNum);
        }

        public void setTaskSize(long taskSize) {
            this.taskSize.set(taskSize);
        }

        public String getTeamName() {
            return teamName.get();
        }

        public SimpleStringProperty teamNameProperty() {
            return teamName;
        }

        public int getAgentNum() {
            return agentNum.get();
        }

        public SimpleIntegerProperty agentNumProperty() {
            return agentNum;
        }

        public long getTaskSize() {
            return taskSize.get();
        }

        public SimpleLongProperty taskSizeProperty() {
            return taskSize;
        }
    }

}
