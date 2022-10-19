package com.engine.users;

import com.engine.enums.AgentStatus;

import java.util.UUID;

public class Agent extends User{
    private UUID AllieId;
    private int numOfThreads;
    private AgentStatus status;
    public Agent(String name){
        super(name);
        this.status = AgentStatus.AWAITING;
    }

    public void setAllieId(UUID allieId) {
        AllieId = allieId;
    }

    public UUID getAllieId() {
        return AllieId;
    }

    public synchronized int getNumOfThreads() {
        return numOfThreads;
    }

    public synchronized void setNumOfThreads(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }

    public synchronized AgentStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(AgentStatus status) {
        this.status = status;
    }
}
