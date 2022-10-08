package com.enigma.main_screen.machine_component.machine_details_component;

import com.enigma.Utils.UBoatAppUtils;
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

    public void bindToUiAdapter(UiAdapter uiAdapter){
        uiAdapter.isLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                getMachineDetails();
            }
        });
        uiAdapter.isConfigureProperty().addListener((observable, oldValue, newValue) -> {
            getMachineDetails();
        });
        uiAdapter.currentConfigProperty().bind(currConfigurationAns.textProperty());
    }

    private void getMachineDetails(){
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(UBoatAppUtils.APP_URL + UBoatAppUtils.MACHINE_DETAILS_RESOURCE)
                .newBuilder();
        urlBuilder.addQueryParameter("id", UBoatAppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();
        Call call = UBoatAppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                userMessageLb.setVisible(true);
                userMessageLb.setText("Something went wrong please try again");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Gson gson = new Gson();
                MachineDetailsAnswer answer = gson.fromJson(response.body().charStream(), MachineDetailsAnswer.class);
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
                    currConfigurationAns.setText(answer.getCurrentConfig());
                });

            }
        });

    }

}
