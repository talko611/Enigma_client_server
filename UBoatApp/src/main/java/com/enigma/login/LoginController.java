package com.enigma.login;

import com.enigma.Utils.UBoatAppUtils;
import com.enigma.dtos.ServletAnswers.LogInAnswer;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML private TextField nameField;
    @FXML private Label message;
    @FXML Button loginBt;
    private SimpleBooleanProperty isLoginSuccessful;

    @FXML
    void loginClicked(ActionEvent event) {
        if(!nameField.textProperty().isEmpty().get()){
            HttpUrl.Builder urlBuilder = HttpUrl.parse(UBoatAppUtils.APP_URL + UBoatAppUtils.LOGIN_RESOURCE).newBuilder();
            urlBuilder.addQueryParameter("name", nameField.getText());
            Request request = new Request.Builder().url(urlBuilder.build()).build();
            Call call = UBoatAppUtils.HTTP_CLIENT.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    message.setText("An error has occurred");
                    message.setVisible(true);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Gson gson = new Gson();
                    LogInAnswer answer = gson.fromJson(response.body().charStream(),LogInAnswer.class);
                    Platform.runLater(()->loginBt.disableProperty().set(false));
                    if(answer.isSuccess()){
                        Platform.runLater(() -> {
                            isLoginSuccessful.set(true);
                            UBoatAppUtils.setClientId(answer.getId());
                            message.visibleProperty().set(false);
                        });
                    }else {
                        Platform.runLater(()->{
                            message.visibleProperty().set(true);
                            message.setText(answer.getMessage());
                        });
                    }
                }
            });
        } else{
            message.setText("Please enter name first");
            message.visibleProperty().set(true);
        }
    }


    public void setIsLoginSuccessful(SimpleBooleanProperty isLoginSuccessful) {
        this.isLoginSuccessful = isLoginSuccessful;
    }
}
