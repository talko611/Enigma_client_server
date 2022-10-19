package com.enigma.Utils;

import com.enigma.dtos.Enums.GameStatus;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UiAdapter {
    private SimpleBooleanProperty isLoaded;
    private SimpleBooleanProperty isConfigure;
    private SimpleBooleanProperty isEncryptedMessage;
    private SimpleBooleanProperty isReady;
    private SimpleBooleanProperty isGameEnded;
    private SimpleStringProperty currentConfig;
    private SimpleBooleanProperty isInActiveGame;
    private SimpleStringProperty battlefieldName;
    private String srcMessage;
    private GameStatus gameStatus;

    public UiAdapter(){
        this.currentConfig = new SimpleStringProperty();
        this.isConfigure = new SimpleBooleanProperty();
        this.isLoaded = new SimpleBooleanProperty();
        this.isInActiveGame = new SimpleBooleanProperty();
        this.battlefieldName = new SimpleStringProperty();
        this.isEncryptedMessage = new SimpleBooleanProperty();
        this.isReady = new SimpleBooleanProperty();
        this.isGameEnded = new SimpleBooleanProperty();
        gameStatus = GameStatus.AWAITING;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setIsEncryptedMessage(boolean isEncryptedMessage) {
        this.isEncryptedMessage.set(isEncryptedMessage);
    }

    public void setIsReady(boolean isReady) {
        this.isReady.set(isReady);
    }

    public void setIsGameEnded(boolean isGameEnded) {
        this.isGameEnded.set(isGameEnded);
    }

    public boolean isIsEncryptedMessage() {
        return isEncryptedMessage.get();
    }

    public SimpleBooleanProperty isEncryptedMessageProperty() {
        return isEncryptedMessage;
    }

    public boolean isIsReady() {
        return isReady.get();
    }

    public SimpleBooleanProperty isReadyProperty() {
        return isReady;
    }

    public boolean isIsGameEnded() {
        return isGameEnded.get();
    }

    public SimpleBooleanProperty isGameEndedProperty() {
        return isGameEnded;
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
