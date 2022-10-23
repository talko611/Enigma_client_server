package com.enigma.login_component;

import com.enigma.dtos.ServletAnswers.LogInAnswer;
import com.enigma.utiles.AppUtils;
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
    @FXML private Button loginBt;
    @FXML private Label message;
    private SimpleBooleanProperty isLoginSuccessful;

    @FXML
    void loginClicked(ActionEvent event) {
        message.setVisible(false);
        if(!nameField.textProperty().isEmpty().get()){
            HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.LOGIN_RESOURCE).newBuilder();
            urlBuilder.addQueryParameter("name", nameField.getText());
            Request request = new Request.Builder().url(urlBuilder.build()).build();
            Call call = AppUtils.CLIENT.newCall(request);
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
                    if(answer.isSuccess()){
                        Platform.runLater(() -> {
                            isLoginSuccessful.set(true);
                            AppUtils.setClientId(answer.getId());
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

    public String getName(){
        return nameField.getText();
    }
}
