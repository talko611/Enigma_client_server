package com.engine.users;

import com.engine.enums.AgentStatus;

import java.util.UUID;

public class Agent extends User{
    private UUID AllieId;
    private int numOfThreads;
    private AgentStatus status;
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

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }
}
