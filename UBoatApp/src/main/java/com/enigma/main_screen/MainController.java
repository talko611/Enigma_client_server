package com.enigma.main_screen;

import com.enigma.Utils.AppUtils;
import com.enigma.Utils.UiAdapter;
import com.enigma.dtos.ServletAnswers.LoadFileAnswer;
import com.enigma.main_screen.contest_component.ContestDataController;
import com.enigma.main_screen.machine_component.configuration_component.ConfigurationController;
import com.enigma.main_screen.machine_component.machine_details_component.MachineDetailsController;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
    @FXML private Button loadFileButton;
    @FXML private Label loadFileAnswerLabel;
    @FXML private Tab machineTab;
    @FXML private Tab machineDetailsTab;
    @FXML private Tab contestTab;
    @FXML private ScrollPane machineDetailsComponent;
    @FXML private MachineDetailsController machineDetailsComponentController;
    @FXML private ScrollPane configurationComponent;
    @FXML private ConfigurationController configurationComponentController;
    @FXML private ScrollPane contestDataComponent;
    @FXML private ContestDataController contestDataComponentController;
    @FXML private Label uBoatName;

    private Stage primaryStage;
    private UiAdapter uiAdapter;

    @FXML
    void initialize(){
        this.uiAdapter = new UiAdapter();
        machineTab.disableProperty().bind(uiAdapter.isLoadedProperty().not());
        contestTab.disableProperty().bind(uiAdapter.isConfigureProperty().not());

        machineDetailsComponentController.setUiAdapter(uiAdapter);
        configurationComponentController.setUiAdapter(uiAdapter);
        contestDataComponentController.setUiAdapter(uiAdapter);
        machineTab.disableProperty().bind(uiAdapter.isEncryptedMessageProperty());
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
        loadFileButton.disableProperty().set(true);
        RequestBody body = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("File",fileToUpload.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),fileToUpload))
                .build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.UPLOAD_FILE_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).method("POST",body).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Platform.runLater(()->{
                    loadFileAnswerLabel.setText("Something went wrong please try to load again");
                    loadFileButton.disableProperty().set(false);
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LoadFileAnswer answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), LoadFileAnswer.class);
                Platform.runLater(()->{
                    uiAdapter.setIsLoaded(answer.isSuccess());
                    loadFileButton.disableProperty().set(answer.isSuccess());
                    uiAdapter.setIsConfigure(false);
                    if(answer.isSuccess()){
                        uiAdapter.setBattlefieldName(answer.getMessage());
                        loadFileAnswerLabel.setText("Battlefield loaded");
                    }else{
                        loadFileAnswerLabel.setText(answer.getMessage());
                    }
                });
            }
        });
    }

    public void setName(String name){
        this.uBoatName.setText("Hello <" + name + ">" );
    }

    @FXML
    void configTabClicked(){
        if(uiAdapter.isIsLoaded()){
            configurationComponentController.initComponent(true);
        }
    }

    public void setLoggedInProperty(SimpleBooleanProperty loggedInProperty){
        this.uiAdapter.setIsLoggedInProperty(loggedInProperty);
    }
}
