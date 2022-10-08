package com.enigma.main_screen;

import com.enigma.Utils.UBoatAppUtils;
import com.enigma.Utils.UiAdapter;
import com.enigma.dtos.ServletAnswers.LoadFileAnswer;
import com.enigma.main_screen.machine_component.configuration_component.ConfigurationController;
import com.enigma.main_screen.machine_component.machine_details_component.MachineDetailsController;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    @FXML private ScrollPane machineDetailsComponent;
    @FXML private MachineDetailsController machineDetailsComponentController;
    @FXML private ScrollPane configurationComponent;
    @FXML private ConfigurationController configurationComponentController;

    private Stage primaryStage;
    private UiAdapter uiAdapter;

    @FXML
    void initialize(){
        this.uiAdapter = new UiAdapter();
        machineTab.disableProperty().bind(uiAdapter.isLoadedProperty().not());
        contestTab.disableProperty().bind(uiAdapter.isConfigureProperty().not());

        machineDetailsComponentController.bindToUiAdapter(uiAdapter);
        configurationComponentController.setUiAdapter(this.uiAdapter);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void loadFileClicked(ActionEvent event) {
        uiAdapter.setIsLoaded(false);
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
                    uiAdapter.setIsLoaded(answer.isSuccess());
                    loadFileAnswerLabel.setText(answer.getMessage());
                    uiAdapter.setIsConfigure(false);
                });
            }
        });
    }

    @FXML
    void configTabClicked(){
        if(uiAdapter.isLoadedProperty().get()){
            configurationComponentController.initComponent(true);
        }
    }
}
