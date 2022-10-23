package com.enigma.utils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UiAdapter {
    private SimpleBooleanProperty isActive;
    private SimpleBooleanProperty isReady;
    private SimpleBooleanProperty isLoggedIn;
    private SimpleBooleanProperty isInActiveGame;
    private SimpleBooleanProperty isGameEnded;
    private SimpleStringProperty myName;

    public UiAdapter() {
        this.isActive = new SimpleBooleanProperty();
        this.isLoggedIn = new SimpleBooleanProperty();
        this.isInActiveGame = new SimpleBooleanProperty();
        this.isReady = new SimpleBooleanProperty();
        this.isGameEnded = new SimpleBooleanProperty();
        this.myName = new SimpleStringProperty("<Anonymous");
    }

    public void setMyName(String myName) {
        this.myName.set(myName);
    }

    public String getMyName() {
        return myName.get();
    }

    public SimpleStringProperty myNameProperty() {
        return myName;
    }

    public boolean isIsGameEnded() {
        return isGameEnded.get();
    }

    public void setIsGameEnded(boolean isGameEnded) {
        this.isGameEnded.set(isGameEnded);
    }

    public SimpleBooleanProperty isGameEndedProperty() {
        return isGameEnded;
    }

    public boolean isIsReady() {
        return isReady.get();
    }

    public void setIsReady(boolean isReady) {
        this.isReady.set(isReady);
    }

    public SimpleBooleanProperty isReadyProperty() {
        return isReady;
    }

    public boolean isIsActive() {
        return isActive.get();
    }

    public SimpleBooleanProperty isActiveProperty() {
        return isActive;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn.get();
    }

    public SimpleBooleanProperty isLoggedInProperty() {
        return isLoggedIn;
    }

    public boolean isIsInActiveGame() {
        return isInActiveGame.get();
    }

    public SimpleBooleanProperty isInActiveGameProperty() {
        return isInActiveGame;
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn.set(isLoggedIn);
    }

    public void setIsInActiveGame(boolean isInActiveGame) {
        this.isInActiveGame.set(isInActiveGame);
    }
}
