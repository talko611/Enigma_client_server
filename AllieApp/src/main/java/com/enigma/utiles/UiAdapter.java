package com.enigma.utiles;

import javafx.beans.property.SimpleBooleanProperty;

public class UiAdapter {
    private SimpleBooleanProperty isJoinToGame;
    private SimpleBooleanProperty isTaskSet;
    private SimpleBooleanProperty isReady;
    private SimpleBooleanProperty isInActiveGame;

    public UiAdapter(){
        this.isJoinToGame = new SimpleBooleanProperty();
        this.isTaskSet = new SimpleBooleanProperty();
        this.isReady = new SimpleBooleanProperty();
        this.isInActiveGame = new SimpleBooleanProperty();
    }

    public void setIsJoinToGame(boolean isJoinToGame) {
        this.isJoinToGame.set(isJoinToGame);
    }

    public void setIsTaskSet(boolean isTaskSet) {
        this.isTaskSet.set(isTaskSet);
    }

    public void setIsReady(boolean isReady) {
        this.isReady.set(isReady);
    }

    public void setIsInActiveGame(boolean isInActiveGame) {
        this.isInActiveGame.set(isInActiveGame);
    }

    public boolean isIsJoinToGame() {
        return isJoinToGame.get();
    }

    public SimpleBooleanProperty isJoinToGameProperty() {
        return isJoinToGame;
    }

    public boolean isIsTaskSet() {
        return isTaskSet.get();
    }

    public SimpleBooleanProperty isTaskSetProperty() {
        return isTaskSet;
    }

    public boolean isIsReady() {
        return isReady.get();
    }

    public SimpleBooleanProperty isReadyProperty() {
        return isReady;
    }

    public boolean isIsInActiveGame() {
        return isInActiveGame.get();
    }

    public SimpleBooleanProperty isInActiveGameProperty() {
        return isInActiveGame;
    }
}
