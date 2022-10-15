package com.enigma.main_screen.contest_component;

import com.enigma.Utils.AppUtils;
import com.enigma.Utils.UiAdapter;
import com.enigma.dtos.dataObjects.AllieData;
import com.enigma.dtos.dataObjects.EncryptMessageData;
import com.enigma.main_screen.contest_component.tasks.GetParticipants;
import com.enigma.main_screen.contest_component.trie_data_structure.Trie;
import com.google.gson.Gson;
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
    @FXML private TableView<UiAllie> teamsTable;
    @FXML private TableColumn<?, ?> teamNameParCol;
    @FXML private TableColumn<?, ?> agentNumCol;
    @FXML private TableColumn<?, ?> taskSizeCol;
    @FXML private Label battlefieldNameLb;
    @FXML private Label gameStatusLb;
    @FXML private TableView<?> candidatesTb;
    @FXML private TableColumn<?, ?> decryptionCol;
    @FXML private TableColumn<?, ?> teamNameCanCol;
    @FXML private TableColumn<?, ?> configurationCol;

    private ObservableList<UiAllie> uiAllies;
    private ObservableList<UiCandidate> uiCandidates;

    private UiAdapter uiAdapter;
    private Trie dictionary;

    @FXML
    void initialize(){
        this.teamNameParCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        this.agentNumCol.setCellValueFactory(new PropertyValueFactory<>("agentNum"));
        this.taskSizeCol.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        this.decryptionCol.setCellValueFactory(new PropertyValueFactory<>("decryption"));
        this.teamNameCanCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        this.configurationCol.setCellValueFactory(new PropertyValueFactory<>("configuration"));
        this.uiAllies = FXCollections.observableArrayList();
        this.uiCandidates = FXCollections.observableArrayList();
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
    }

    @FXML
    void processButtonClicked(ActionEvent event) {
        processBt.disableProperty().set(true);
        encrypt();
        readyBt.disableProperty().set(false);
    }

    @FXML
    void readyButtonClicked(ActionEvent event) {
        uiAdapter.setIsInActiveGame(true);
    }

    @FXML
    void resetMachineClicked(ActionEvent event) {

    }

    @FXML
    void clearButtonClicked(ActionEvent event){
        processBt.disableProperty().set(false);
    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindToUiAdapter();
    }

    private void bindToUiAdapter(){
        uiAdapter.isLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                loadDictionary();
                new Thread(new GetParticipants((data)->{
                    uiAllies.clear();
                    data.forEach(allieData -> this.uiAllies.add(new
                            UiAllie(allieData.getAlliName(),allieData.getNumOfAgents(),allieData.getTaskSize())));
                    teamsTable.setItems(uiAllies);
                },
                        uiAdapter.isInActiveGameProperty())).start();
            }
        });
        clearBt.disableProperty().bind(uiAdapter.isInActiveGameProperty());
        configurationLb.textProperty().bind(uiAdapter.currentConfigProperty());
        battlefieldNameLb.textProperty().bind(uiAdapter.battlefieldNameProperty());
    }

    private void loadDictionary(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.DICTIONARY_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder()
                .url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Something went wrong");
                //Todo - find out better way to handle this
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Gson gson = new Gson();
                Set<String> dictionary = gson.fromJson(response.body().charStream(), Set.class);
                Platform.runLater(()->setDictionary(dictionary));
            }
        });
    }

    private void setDictionary(Set<String> words){
        this.dictionary = new Trie();
        words.forEach(word -> dictionary.addWord(word.toLowerCase()));
        wordList.getItems().addAll(dictionary.getAllChildren(""));
    }

    private void encrypt(){
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
                        encryptedMessage.setText(answer.getEncrypted());
                        uiAdapter.setCurrentConfig(answer.getCurrentMachineConfiguration());
                        userMessage.setText(answer.getMessage());
                    });
                }else{
                    Platform.runLater(()->{
                        userMessage.setText(answer.getMessage());
                        processBt.disableProperty().set(false);
                    });
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

        public UiCandidate(SimpleStringProperty decryption, SimpleStringProperty teamName, SimpleStringProperty configuration) {
            this.decryption = decryption;
            this.teamName = teamName;
            this.configuration = configuration;
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
