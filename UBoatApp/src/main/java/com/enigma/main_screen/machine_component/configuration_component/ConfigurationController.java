package com.enigma.main_screen.machine_component.configuration_component;

import com.enigma.Utils.AppUtils;
import com.enigma.Utils.UiAdapter;
import com.enigma.dtos.dataObjects.ManualConfigStrings;
import com.enigma.dtos.ServletAnswers.MachinePartsAnswer;
import com.enigma.dtos.ServletAnswers.RequestServerAnswer;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;

public class ConfigurationController {

    @FXML private ChoiceBox<Integer> rotorIdCb;
    @FXML private Label userMessageLb;
    @FXML private ChoiceBox<String> rotorOffsetCb;
    @FXML private ChoiceBox<Integer> reflectorCb;
    @FXML private Label rotorCountLb;
    @FXML private Label rotorStatusLb;

    private MachinePartsAnswer currentMachineParts;
    private static final String MIN_ROTOR_COUNT = "Minimum number of Rotors:  ";
    private static final String ROTOR_COUNT_STATUS = "Rotors chosen: ";
    private StringBuilder rotorConfigLine;
    private StringBuilder offsetConfigLine;
    private int rotorAddNum;
    private UiAdapter uiAdapter;


    public ConfigurationController(){
        this.rotorConfigLine = new StringBuilder();
        this.offsetConfigLine = new StringBuilder();
    }

    @FXML
    void addRotor(ActionEvent event) {
        if(rotorIdCb.getValue() != null && rotorOffsetCb.getValue() != null){
            this.rotorConfigLine.append(rotorIdCb.getValue()).append(" ");
            this.offsetConfigLine.append(rotorOffsetCb.getValue());
            rotorIdCb.getItems().remove(rotorIdCb.getValue());
            ++rotorAddNum;
            rotorStatusLb.setText(ROTOR_COUNT_STATUS+ rotorAddNum + "/" + currentMachineParts.getRotorIds().size());
            userMessageLb.setText("");
        }else{
            userMessageLb.setText("Cannot add rotor without an offset");
        }
        rotorOffsetCb.setValue(null);
        rotorIdCb.setValue(null);
    }

    @FXML
    void launchAutoConfigRequest(ActionEvent event) {
        uiAdapter.setIsConfigure(false);
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(AppUtils.APP_URL + AppUtils.CONFIGURATION_RESOURCE)
                .newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()-> userMessageLb.setText("Something went wrong please try again"));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                RequestServerAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), RequestServerAnswer.class);
                Platform.runLater(()->{
                    uiAdapter.setIsConfigure(answer.isSuccess());
                    userMessageLb.setText(answer.getMessage());
                    initComponent(false);
                });
            }
        });
    }

    @FXML
    void launchManualConfigRequest(ActionEvent event) {
        if(rotorAddNum < currentMachineParts.getRotorsCount()){
            userMessageLb.setText("Rotor number is less than the machine minimum");
            return;
        }
        if(reflectorCb.getValue() == null){
            userMessageLb.setText("Please choose reflector");
            return;
        }
        uiAdapter.setIsConfigure(false);
        ManualConfigStrings bodyData = createRequestBody();
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(AppUtils.APP_URL + AppUtils.CONFIGURATION_RESOURCE)
                .newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), AppUtils.GSON_SERVICE.toJson(bodyData));
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                userMessageLb.setText("Something went wrong.. Cannot get machine parts");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                RequestServerAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), RequestServerAnswer.class);
                Platform.runLater(() -> {
                    uiAdapter.setIsConfigure(answer.isSuccess());
                    userMessageLb.setText(answer.getMessage());
                    initComponent(false);
                });
            }
        });

    }

    private void launchGetMachinePartsRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.MACHINE_PARTS_DETAILS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                userMessageLb.setText("Something went wrong.. Cannot get machine parts");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                MachinePartsAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), MachinePartsAnswer.class);
                Platform.runLater(()-> {
                    setCurrentMachineParts(answer);
                    initComponent(false);
                });
            }
        });
    }

    private void setCurrentMachineParts(MachinePartsAnswer currentMachineParts){
        this.currentMachineParts = currentMachineParts;
    }

    public void initComponent(boolean deleteUserMessage){
        setRotorIdCb();
        setReflectorCb();
        setRotorOffsetCb();
        rotorCountLb.setText(MIN_ROTOR_COUNT + currentMachineParts.getRotorsCount());
        rotorStatusLb.setText(ROTOR_COUNT_STATUS + "0/" + currentMachineParts.getRotorIds().size());
        rotorAddNum = 0;
        rotorConfigLine = new StringBuilder();
        offsetConfigLine = new StringBuilder();
        if(deleteUserMessage)
            userMessageLb.setText("");
    }

    private void setRotorIdCb(){
        rotorIdCb.getItems().clear();
        rotorIdCb.getItems().addAll(currentMachineParts.getRotorIds());
    }

    private void setRotorOffsetCb(){
        rotorOffsetCb.getItems().clear();
        rotorOffsetCb.getItems().addAll(currentMachineParts.getKeyboardChars());
    }

    private void setReflectorCb(){
        reflectorCb.getItems().clear();
        reflectorCb.getItems().addAll(currentMachineParts.getReflectorIds());
    }

    private void bindToUiAdaptor(){
        uiAdapter.isLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                launchGetMachinePartsRequest();
            }
        });
    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindToUiAdaptor();
    }

    private ManualConfigStrings createRequestBody(){
        ManualConfigStrings configStrings = new ManualConfigStrings();
        configStrings.setRotorConfigLine(rotorConfigLine.toString());
        configStrings.setOffsetConfigLine(offsetConfigLine.toString());
        configStrings.setReflectorConfigLine(reflectorCb.getValue().toString());
        configStrings.setPlugBoardConfigLine("");
        return configStrings;
    }
}
