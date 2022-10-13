package com.engine.users;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private boolean isReadyToPlay;

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


    public void setReadyToPlay(boolean readyToPlay) {
        isReadyToPlay = readyToPlay;
    }

    public boolean isReadyToPlay() {
        return isReadyToPlay;
    }
}
