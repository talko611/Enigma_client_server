package com.enigma.main;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.enigma.main.tasks.GetMachinePart;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;

import java.util.Set;
import java.util.function.Consumer;

public class MainController {

    private SimpleBooleanProperty isReady;
    private MachineParts machineParts;
    private Set<String> dictionary;
    private Consumer<EnigmaParts> updateEnigmaParts;

    @FXML
    void initialize(){
        this.isReady = new SimpleBooleanProperty();
        this.updateEnigmaParts = (enigmaParts -> {
            this.machineParts = enigmaParts.getMachineParts();
            this.dictionary = enigmaParts.getDmParts().getDictionary();
        });
        getParts();
    }

    private void getParts(){
        new Thread(new GetMachinePart(updateEnigmaParts, isReady::set)).start();
    }
}
