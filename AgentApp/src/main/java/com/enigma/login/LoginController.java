package com.enigma.login;

import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.dtos.ServletAnswers.LogInAnswer;
import com.enigma.login.tasks.GetActiveAllies;
import com.enigma.utils.AppUtils;
import com.enigma.utils.UiAdapter;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class LoginController {
    @FXML private TextField nameField;
    @FXML private Button loginBt;
    @FXML private ComboBox<String> teamsCb;
    @FXML private ComboBox<Integer> workersCb;
    @FXML private Label message;
    @FXML private TextField numberOfTasksTf;

    private UiAdapter uiAdapter;
    private Map<String, UUID> allies;
    private int numOfTasks;

    @FXML
    void initialize(){
        workersCb.getItems().addAll(Arrays.asList(1,2,3,4));
        allies = new HashMap<>();
    }

    @FXML
    void loginClicked(ActionEvent event) {
        if(teamsCb.getItems().isEmpty()){
            message.setText("No allie online cannot login until an allie is login");
        }else if(numberOfTasksTf.getText().isEmpty()){
            message.setText("Please enter number of tasks");
        }
        else{
            if(validateLoginRequest()){
                loginBt.disableProperty().set(true);
                logIn();
            }
        }
    }

    private boolean validateLoginRequest(){
        if(nameField.getText().isEmpty()){
            message.setText("Username is missing");
            return false;
        }
        if(workersCb.getValue() == null){
            message.setText("Num of workers is not chosen");
            return false;
        }
        if(teamsCb.getValue() == null){
            message.setText("No Allie team was chosen");
            return false;
        }
        try {
            this.numOfTasks = Integer.parseInt(this.numberOfTasksTf.getText());
        }catch (NumberFormatException e){
            message.setText("Please enter only positive whole number");
            return false;
        }
        return true;
    }

    private void logIn(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.LOGIN_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("name", nameField.getText())
                .addQueryParameter("allie", allies.get(teamsCb.getValue()).toString())
                .addQueryParameter("numOfTasks", String.valueOf(numOfTasks));
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->{
                    message.setText("Cannot log in please try again");
                    loginBt.disableProperty().set(false);
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogInAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), LogInAnswer.class);
                Platform.runLater(()->{
                    AppUtils.CLIENT_ID = answer.getId();
                    uiAdapter.setIsLoggedIn(answer.isSuccess());
                    message.setText(answer.getMessage());
                    loginBt.disableProperty().set(false);
                    if(response.code() == 200)
                        Platform.runLater(()->uiAdapter.setIsActive(true));
                    if(response.code() == 206)
                        Platform.runLater(()->uiAdapter.setIsActive(false));
                });
                response.body().close();
            }
        });

    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindComponent();
    }

    private void bindComponent(){
        Consumer<GetMapOfData<String>> updateTeams = (answer)->{
            allies.clear();
            String value = teamsCb.getValue();
            teamsCb.getItems().clear();
            answer.getData().forEach((id, name) -> allies.put((String) name,id));
            teamsCb.getItems().addAll(allies.keySet());
            teamsCb.setValue(value);
        };
        Thread thread = new Thread(new GetActiveAllies(updateTeams, uiAdapter.isLoggedInProperty()));
        thread.setName("Get allies name thread");
        thread.start();
    }

    public String getTeamName(){
        return teamsCb.getValue();
    }

    public int getThreadsNum(){
        return workersCb.getValue();
    }

    public String getAgentName(){
        return nameField.getText();
    }
}
