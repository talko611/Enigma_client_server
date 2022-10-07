package com.enigma.main;

import com.enigma.Utils.UBoatAppUtils;
import com.enigma.dtos.ServletAnswers.LoadFileAnswer;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainController {
    @FXML private Button LoadFileButton;
    @FXML private Label loadFileAnswerLabel;
    @FXML private Tab machineTab;
    @FXML private Tab machineDetailsTab;
    @FXML private Tab contestTab;
    @FXML private Tab encryptDecryptTab;

    private Stage primaryStage;

    private SimpleBooleanProperty isLoaded;
    private SimpleBooleanProperty isConfigure;

    @FXML
    void initialize(){
        isLoaded = new SimpleBooleanProperty(false);
        isConfigure = new SimpleBooleanProperty(false);
        machineTab.disableProperty().bind(isLoaded.not());
        contestTab.disableProperty().bind(isConfigure.not());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void loadFileClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("chooseFile to load..");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files","*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile == null)
            return;
        sendLoadFileRequest(selectedFile);
    }
    private void sendLoadFileRequest(File fileToUpload){
        RequestBody body = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("File",fileToUpload.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),fileToUpload))
                .build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(UBoatAppUtils.APP_URL + UBoatAppUtils.UPLOAD_FILE_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id",UBoatAppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).method("POST",body).build();
        Call call = UBoatAppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Something went wrong..");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Gson gson = new Gson();
                LoadFileAnswer answer = gson.fromJson(response.body().charStream(), LoadFileAnswer.class);
                Platform.runLater(()->{
                    isLoaded.set(answer.isSuccess());
                    isConfigure.set(false);
                    loadFileAnswerLabel.setText(answer.getMessage());
                });
            }
        });
    }
}
