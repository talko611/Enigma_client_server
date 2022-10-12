package com.enigma.main_screen.contest_component;

import com.enigma.Utils.UBoatAppUtils;
import com.enigma.Utils.UiAdapter;
import com.enigma.dtos.dataObjects.EncryptMessageData;
import com.enigma.main_screen.contest_component.trie_data_structure.Trie;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Set;

public class ContestDataController {

    @FXML private Label configurationLb;
    @FXML private Label userMessage;
    @FXML private TextField srcMessageTb;
    @FXML private TextField encryptedMessage;
    @FXML private TextField dictionaryTb;
    @FXML private ListView<String> wordList;
    @FXML private TableView<?> teamsTable;
    @FXML private Button processBt;
    @FXML private Button resetMachineBt;
    @FXML private Button readyBt;
    @FXML private Button clearBt;

    private UiAdapter uiAdapter;
    private Trie dictionary;

    @FXML
    void initialize(){
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
            }
        });
        resetMachineBt.disableProperty().bind(uiAdapter.isInActiveGameProperty());
        clearBt.disableProperty().bind(uiAdapter.isInActiveGameProperty());
        configurationLb.textProperty().bind(uiAdapter.currentConfigProperty());
    }

    private void loadDictionary(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(UBoatAppUtils.APP_URL + UBoatAppUtils.DICTIONARY_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", UBoatAppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder()
                .url(urlBuilder.build()).build();
        Call call = UBoatAppUtils.HTTP_CLIENT.newCall(request);
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
        Gson gson = new Gson();
        EncryptMessageData data = new EncryptMessageData();
        data.setSource(srcMessageTb.getText());
        uiAdapter.setSrcMessage(srcMessageTb.getText());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(UBoatAppUtils.APP_URL + UBoatAppUtils.ENCRYPT_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", UBoatAppUtils.CLIENT_ID.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(data));
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .method("POST", body).build();
        Call call = UBoatAppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //Todo - handle failure
            }

            @Override
            public void onResponse(Response response) throws IOException {
                EncryptMessageData answer = gson.fromJson(response.body().charStream(), EncryptMessageData.class);
                if(response.code() == 200){
                    Platform.runLater(()->{
                        encryptedMessage.setText(answer.getEncrypted());
                        uiAdapter.setCurrentConfig(answer.getCurrentMachineConfiguration());
                        userMessage.setText(answer.getMessage());
                    });
                }else{
                    Platform.runLater(()->userMessage.setText(answer.getMessage()));
                }
            }
        });
    }
}
