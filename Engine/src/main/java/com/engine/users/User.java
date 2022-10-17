package com.engine.users;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private boolean isReadyToPlay;
    private boolean isInActiveGame;

    public User(String name){
        this.id = UUID.randomUUID();
        this.name = name;
        this.isReadyToPlay = false;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public synchronized void setReadyToPlay(boolean readyToPlay) {
        isReadyToPlay = readyToPlay;
    }

    public synchronized boolean isReadyToPlay() {
        return isReadyToPlay;
    }

    public synchronized boolean isInActiveGame() {
        return isInActiveGame;
    }

    public synchronized void setInActiveGame(boolean inActiveGame) {
        isInActiveGame = inActiveGame;
    }
}
