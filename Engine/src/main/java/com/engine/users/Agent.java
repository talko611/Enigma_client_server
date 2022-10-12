package com.engine.users;

import java.util.UUID;

public class Agent extends User{
    private UUID AllieId;
    public Agent(String name){
        super(name);
    }

    public void setAllieId(UUID allieId) {
        AllieId = allieId;
    }

    public UUID getAllieId() {
        return AllieId;
    }
}
