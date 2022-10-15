package com.enigma.Utils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UiAdapter {
    private SimpleBooleanProperty isLoaded;
    private SimpleBooleanProperty isConfigure;
    private SimpleStringProperty currentConfig;
    private SimpleBooleanProperty isInActiveGame;
    private SimpleStringProperty battlefieldName;
    private String srcMessage;

    public UiAdapter(){
        this.currentConfig = new SimpleStringProperty();
        this.isConfigure = new SimpleBooleanProperty(false);
        this.isLoaded = new SimpleBooleanProperty(false);
        this.isInActiveGame = new SimpleBooleanProperty(false);
        this.battlefieldName = new SimpleStringProperty();
    }

    public String getBattlefieldName() {
        return battlefieldName.get();
    }

    public void setBattlefieldName(String battlefieldName) {
        this.battlefieldName.set(battlefieldName);
    }

    public SimpleStringProperty battlefieldNameProperty() {
        return battlefieldName;
    }

    public boolean isIsLoaded() {
        return isLoaded.get();
    }

    public boolean isIsConfigure() {
        return isConfigure.get();
    }

    public String getCurrentConfig() {
        return currentConfig.get();
    }

    public SimpleBooleanProperty isLoadedProperty() {
        return isLoaded;
    }

    public SimpleBooleanProperty isConfigureProperty() {
        return isConfigure;
    }

    public SimpleStringProperty currentConfigProperty() {
        return currentConfig;
    }

    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded.set(isLoaded);
    }

    public void setIsConfigure(boolean isConfigure) {
        this.isConfigure.set(isConfigure);
    }

    public void setCurrentConfig(String currentConfig) {
        this.currentConfig.set(currentConfig);
    }

    public void setIsInActiveGame(boolean isInActiveGame) {
        this.isInActiveGame.set(isInActiveGame);
    }

    public boolean isIsInActiveGame() {
        return isInActiveGame.get();
    }

    public SimpleBooleanProperty isInActiveGameProperty() {
        return isInActiveGame;
    }

    public String getSrcMessage() {
        return srcMessage;
    }

    public void setSrcMessage(String srcMessage) {
        this.srcMessage = srcMessage;
    }
}
