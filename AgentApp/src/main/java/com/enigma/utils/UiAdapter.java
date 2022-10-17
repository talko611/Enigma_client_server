package com.enigma.utils;

import javafx.beans.property.SimpleBooleanProperty;

public class UiAdapter {
    private SimpleBooleanProperty isActive;
    private SimpleBooleanProperty isReady;
    private SimpleBooleanProperty isLoggedIn;
    private SimpleBooleanProperty isInActiveGame;

    public UiAdapter() {
        this.isActive = new SimpleBooleanProperty();
        this.isLoggedIn = new SimpleBooleanProperty();
        this.isInActiveGame = new SimpleBooleanProperty();
        this.isReady = new SimpleBooleanProperty();
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
