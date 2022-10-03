package com.engine.users;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private boolean isLoggedIn;
    private boolean isInActiveGame;
    private boolean isReadyToPlay;
    private boolean isBelongToBattlefield;

    public User(String name){
        this.id = UUID.randomUUID();
        this.name = name;
        this.isLoggedIn = true;
        this.isInActiveGame = false;
        this.isBelongToBattlefield = false;
        this.isReadyToPlay = false;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setInActiveGame(boolean inActiveGame) {
        isInActiveGame = inActiveGame;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        isReadyToPlay = readyToPlay;
    }

    public void setBelongToBattlefield(boolean belongToBattlefield) {
        isBelongToBattlefield = belongToBattlefield;
    }

    public boolean isInActiveGame() {
        return isInActiveGame;
    }

    public boolean isReadyToPlay() {
        return isReadyToPlay;
    }

    public boolean isBelongToBattlefield() {
        return isBelongToBattlefield;
    }
}
