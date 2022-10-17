package com.enigma.main;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.enigma.main.tasks.GetMachinePart;
import com.enigma.main.tasks.SetAvailableTask;
import com.enigma.utils.UiAdapter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;

import java.util.Set;
import java.util.function.Consumer;

public class MainController {

    private UiAdapter uiAdapter;
    private MachineParts machineParts;
    private Set<String> dictionary;
    private Consumer<EnigmaParts> updateEnigmaParts;

    @FXML
    void initialize(){
        this.updateEnigmaParts = (enigmaParts -> {
            this.machineParts = enigmaParts.getMachineParts();
            this.dictionary = enigmaParts.getDmParts().getDictionary();
        });
    }

    public void setUiAdapter(UiAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        bindComponent();
    }

    private void getParts(){
        new Thread(new GetMachinePart(updateEnigmaParts, uiAdapter.isReadyProperty()::set)).start();
    }

    private void bindComponent(){
        //one time set this thread when join in the middle of a game;
        if(!uiAdapter.isIsActive()){
            new Thread(new SetAvailableTask(uiAdapter.isActiveProperty()::set)).start();
        }
        uiAdapter.isActiveProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                getParts();
            }
        });

        uiAdapter.isReadyProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                getParts();
            }
        });
    }
}
