package com.enigma.login_screen;

import com.enigma.Utils.AppUtils;
import com.enigma.dtos.ServletAnswers.LogInAnswer;
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
            HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.LOGIN_RESOURCE).newBuilder();
            urlBuilder.addQueryParameter("name", nameField.getText());
            Request request = new Request.Builder().url(urlBuilder.build()).build();
            Call call = AppUtils.HTTP_CLIENT.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Platform.runLater(()->{
                        message.setText("An error has occurred");
                        message.setVisible(true);
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    LogInAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(),LogInAnswer.class);
                    Platform.runLater(()->loginBt.disableProperty().set(false));
                    if(answer.isSuccess()){
                        Platform.runLater(() -> {
                            isLoginSuccessful.set(true);
                            AppUtils.CLIENT_ID = answer.getId();
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
    public String getName(){
        return nameField.getText();
    }
}
