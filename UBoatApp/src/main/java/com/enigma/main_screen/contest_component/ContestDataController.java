package com.enigma.main_screen.contest_component;

import com.enigma.Utils.AppUtils;
import com.enigma.Utils.UiAdapter;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.enigma.dtos.dataObjects.Candidate;
import com.enigma.dtos.dataObjects.EncryptMessageData;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.main_screen.contest_component.tasks.GetCandidatesTask;
import com.enigma.main_screen.contest_component.tasks.GetGameStatusTask;
import com.enigma.main_screen.contest_component.tasks.GetParticipantsTask;
import com.enigma.main_screen.contest_component.trie_data_structure.Trie;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ContestDataController {
    @FXML private Label configurationLb;
    @FXML private TextField srcMessageTb;
    @FXML private TextField encryptedMessage;
    @FXML private Label userMessage;
    @FXML private TextField dictionaryTb;
    @FXML private ListView<String> wordList;
    @FXML private Button processBt;
    @FXML private Button resetMachineBt;
    @FXML private Button readyBt;
    @FXML private Button clearBt;
    @FXML private Button logOutBt;
    @FXML private TableView<UiAllie> teamsTable;
    @FXML private TableColumn<?, ?> teamNameParCol;
    @FXML private TableColumn<?, ?> agentNumCol;
    @FXML private TableColumn<?, ?> taskSizeCol;
    @FXML private Label battlefieldNameLb;
    @FXML private Label gameStatusLb;
    @FXML private TableView<UiCandidate> candidatesTb;
    @FXML private TableColumn<?, ?> decryptionCol;
    @FXML private TableColumn<?, ?> teamNameCanCol;
    @FXML private TableColumn<?, ?> configurationCol;
    private ObservableList<UiAllie> uiAllies;
    private ObservableList<UiCandidate> uiCandidates;
    private UiAdapter uiAdapter;
    private Trie dictionary;
    private Consumer<GameDetailsObject> updateGameDetails;
    private Consumer<List<Candidate>> updateCandidates;
    private String lastInitialConfiguration;
    @FXML
    void initialize(){
        this.teamNameParCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        this.agentNumCol.setCellValueFactory(new PropertyValueFactory<>("agentNum"));
        this.taskSizeCol.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        this.uiAllies = FXCollections.observableArrayList();
        this.decryptionCol.setCellValueFactory(new PropertyValueFactory<>("decryption"));
        this.teamNameCanCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        this.configurationCol.setCellValueFactory(new PropertyValueFactory<>("configuration"));
        this.uiCandidates = FXCollections.observableArrayList();
        this.candidatesTb.setItems(uiCandidates);
        dictionaryTb.textProperty().addListener((observable, oldValue, newValue) -> {
            wordList.getItems().clear();
            wordList.getItems().addAll(dictionary.getAllChildren(newValue.toLowerCase()));
        });
        wordList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                String value = srcMessageTb.getText() + " " + newValue;
                srcMessageTb.setText(value.trim());
            }

        });
        gameStatusLb.setText("Awaiting");
        this.updateGameDetails = gameDetailsObject -> {
            if(gameDetailsObject.getGameStatus() != uiAdapter.getGameStatus()){
                gameStatusLb.setText(gameDetailsObject.getGameStatus().toString());
                uiAdapter.setGameStatus(gameDetailsObject.getGameStatus());
                switch (gameDetailsObject.getGameStatus()){
                    case AWAITING:
                        break;
                    case RUNNING:
                        uiAdapter.setIsInActiveGame(true);
                        break;
                    case ENDING:
                        uiAdapter.setIsGameEnded(true);
                        uiAdapter.setIsInActiveGame(false);
                        userMessage.setText("Team " + gameDetailsObject.getWinningTeamName() + " has won!");
                        break;
                }
            }
        };
        this.updateCandidates = candidateList -> candidateList.forEach(candidate ->
                uiCandidates.add(new UiCandidate(candidate.getDecryption(), candidate.getTeamName(), candidate.getConfiguration())));
    }

    @FXML
    void processButtonClicked(ActionEvent event) {
        lastInitialConfiguration = uiAdapter.getCurrentConfig();
        launchEncryptRequest();
    }

    @FXML
    void readyButtonClicked(ActionEvent event) {
        launchSetReadyRequest();
    }

    @FXML
    void resetMachineClicked(ActionEvent event) {
        launchResetMachineRequest();
    }

    @FXML
    void clearButtonClicked(ActionEvent event){
        launchResetGameRequest();
    }

    @FXML
    void logOutButtonClicked(ActionEvent event){
        launchLogOutRequest();
    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindComponentToUiAdapter();
    }

    private void launchResetMachineRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.RESET_MACHINE_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->userMessage.setText("Something happened machine cannot be reset"));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() == 200){
                    RequestServerAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), RequestServerAnswer.class);
                    Platform.runLater(()->{
                        userMessage.setText("Machine is reset");
                        uiAdapter.setCurrentConfig(answer.getMessage());
                    });
                }
            }
        });
    }

    private void bindComponentToUiAdapter(){
        uiAdapter.isLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                launchGetDictionaryRequest();
                launchGetParticipantsTask();
            }
        });
        uiAdapter.isReadyProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                launchGetGameStatusTask();
                this.readyBt.disableProperty().set(true);
            }
            else{
                this.processBt.disableProperty().set(false);
                this.resetMachineBt.disableProperty().set(false);
                this.clearBt.disableProperty().set(true);
                this.logOutBt.disableProperty().set(true);
                this.logOutBt.visibleProperty().set(false);
                launchGetParticipantsTask();
            }

        });
        uiAdapter.isInActiveGameProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                launchGetCandidatesRequest();
                this.readyBt.disableProperty().set(true);
            }

        });
        uiAdapter.isEncryptedMessageProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.processBt.disableProperty().set(true);
                this.resetMachineBt.disableProperty().set(true);
                this.readyBt.disableProperty().set(false);
            }
        });
        uiAdapter.isGameEndedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.clearBt.disableProperty().set(false);
                this.logOutBt.disableProperty().set(false);
                this.logOutBt.visibleProperty().set(true);
            }
        });
        configurationLb.textProperty().bind(uiAdapter.currentConfigProperty());
        battlefieldNameLb.textProperty().bind(uiAdapter.battlefieldNameProperty());
    }

    private void launchGetGameStatusTask(){
        Thread thread = new Thread(new GetGameStatusTask(uiAdapter.isGameEndedProperty(), updateGameDetails));
        thread.setName("Get game status Task");
        thread.start();
    }

    private void launchGetParticipantsTask(){
        Thread thread = new Thread(new GetParticipantsTask((data)->{
            uiAllies.clear();
            data.forEach(allieData -> this.uiAllies.add(new
                    UiAllie(allieData.getAlliName(),allieData.getNumOfAgents(),allieData.getTaskSize())));
            teamsTable.setItems(uiAllies);
        },
                uiAdapter.isInActiveGameProperty()));
        thread.setName("Get participants Task");
        thread.start();
    }

    private void setDictionary(Set<String> words){
        this.dictionary = new Trie();
        words.forEach(word -> dictionary.addWord(word.toLowerCase()));
        wordList.getItems().addAll(dictionary.getAllChildren(""));
    }

    private void launchEncryptRequest(){
        EncryptMessageData data = new EncryptMessageData();
        data.setSource(srcMessageTb.getText());
        uiAdapter.setSrcMessage(srcMessageTb.getText());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.ENCRYPT_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), AppUtils.GSON_SERVICE.toJson(data));
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .method("POST", body).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //Todo - handle failure
            }

            @Override
            public void onResponse(Response response) throws IOException {
                EncryptMessageData answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), EncryptMessageData.class);
                if(response.code() == 200){
                    Platform.runLater(()->{
                        uiAdapter.setIsEncryptedMessage(true);
                        encryptedMessage.setText(answer.getEncrypted());
                        uiAdapter.setCurrentConfig(answer.getCurrentMachineConfiguration());
                        userMessage.setText(answer.getMessage());
                    });
                }else{
                    Platform.runLater(()-> userMessage.setText(answer.getMessage()));
                }
                response.body().close();
            }
        });
    }

    private void launchGetDictionaryRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.DICTIONARY_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder()
                .url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("UBoat app(Get dictionary request) -> could not fulfill request");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Set<String> dictionary = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(Set.class, String.class).getType());
                Platform.runLater(()->setDictionary(dictionary));
            }
        });
    }

    private void launchSetReadyRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.SET_READY_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->userMessage.setText("Request has failed"));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                RequestServerAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), RequestServerAnswer.class);
                Platform.runLater(()->{
                    uiAdapter.setIsReady(answer.isSuccess());
                    userMessage.setText(answer.getMessage());
                });
            }
        });
    }

    private void launchGetCandidatesRequest(){
        Thread thread = new Thread(new GetCandidatesTask(uiAdapter.isGameEndedProperty(), this.updateCandidates));
        thread.setName("Update candidates Task");
        thread.start();
    }

    private void launchResetGameRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.RESET_GAME_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->userMessage.setText("Rest request has failed"));
            }

            @Override
            public void onResponse(Response response) {
                if(response.code() == 200){
                    Platform.runLater(()->{
                        uiAdapter.setIsReady(false);
                        uiAdapter.setIsGameEnded(false);
                        uiAdapter.setIsEncryptedMessage(false);
                        srcMessageTb.setText("");
                        encryptedMessage.setText("");
                        userMessage.setText("Game has been reset");
                        uiCandidates.clear();
                    });
                }
            }
        });
    }

    private void launchLogOutRequest(){
        HttpUrl.Builder  urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.LOG_OUR_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->userMessage.setText("Something got wrong cannot succeed to log out\nPlease Try again"));
            }

            @Override
            public void onResponse(Response response) {
                if(response.code() == 200){
                    Platform.runLater(()->uiAdapter.setIsLoggedIn(false));
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

    public static class UiCandidate{
        private SimpleStringProperty decryption;
        private SimpleStringProperty teamName;
        private SimpleStringProperty configuration;

        public UiCandidate(String decryption, String teamName, String configuration) {
            this.decryption = new SimpleStringProperty(decryption);
            this.teamName = new SimpleStringProperty(teamName);
            this.configuration = new SimpleStringProperty(configuration);
        }

        public void setDecryption(String decryption) {
            this.decryption.set(decryption);
        }

        public void setTeamName(String teamName) {
            this.teamName.set(teamName);
        }

        public void setConfiguration(String configuration) {
            this.configuration.set(configuration);
        }

        public String getDecryption() {
            return decryption.get();
        }

        public SimpleStringProperty decryptionProperty() {
            return decryption;
        }

        public String getTeamName() {
            return teamName.get();
        }

        public SimpleStringProperty teamNameProperty() {
            return teamName;
        }

        public String getConfiguration() {
            return configuration.get();
        }

        public SimpleStringProperty configurationProperty() {
            return configuration;
        }
    }

}
