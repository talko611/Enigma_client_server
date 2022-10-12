package com.engine.users;

import java.util.UUID;

public class Agent extends User{
    private UUID AllieId;
    private int numOfThreads;
    public Agent(String name){
        super(name);
    }

    public void setAllieId(UUID allieId) {
        AllieId = allieId;
    }

    public UUID getAllieId() {
        return AllieId;
    }

    public int getNumOfThreads() {
        return numOfThreads;
    }

    public void setNumOfThreads(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }
}
