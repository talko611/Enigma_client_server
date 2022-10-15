package com.enigma.main_screen.machine_component.machine_details_component;

import com.enigma.Utils.AppUtils;
import com.enigma.Utils.UiAdapter;
import com.enigma.dtos.ServletAnswers.MachineDetailsAnswer;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class MachineDetailsController {

    @FXML private Label usedVsAvailAns;
    @FXML private Label reflectorNumAns;
    @FXML private Label initConfigurationAns;
    @FXML private Label currConfigurationAns;
    @FXML private Label userMessageLb;

    private UiAdapter uiAdapter;

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindToUiAdapter();
    }

    private void bindToUiAdapter(){
        uiAdapter.isLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                getMachineDetails();
            }
        });
        uiAdapter.isConfigureProperty().addListener((observable, oldValue, newValue) -> {
            getMachineDetails();
        });
        currConfigurationAns.textProperty().bind(uiAdapter.currentConfigProperty());
    }

    private void getMachineDetails(){
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(AppUtils.APP_URL + AppUtils.MACHINE_DETAILS_RESOURCE)
                .newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->{
                    userMessageLb.setVisible(true);
                    userMessageLb.setText("Something went wrong please try again");
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                MachineDetailsAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), MachineDetailsAnswer.class);
                Platform.runLater(()->{
                    if(!answer.isConfig()){
                        userMessageLb.setText("Machine is not configured yet");
                        userMessageLb.setVisible(true);
                    }
                    else{
                        userMessageLb.setVisible(false);
                    }
                    usedVsAvailAns.setText(answer.getUsedVsAvailRotors());
                    reflectorNumAns.setText(answer.getReflectorsNum());
                    initConfigurationAns.setText(answer.getInitialConfig());
                    uiAdapter.setCurrentConfig(answer.getCurrentConfig());
                });
            }
        });

    }

}
