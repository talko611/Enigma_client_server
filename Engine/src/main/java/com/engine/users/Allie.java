package com.engine.users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Allie extends User{
    private UUID battlefieldId;
    private final List<Agent> agentList;
    private long taskSize;
    private long numberOfTasks;
    public Allie(String name){
        super(name);
        this.agentList = new ArrayList<>();
    }

    public void setBattlefieldId(UUID battlefieldId) {
        this.battlefieldId = battlefieldId;
    }

    public synchronized void addAgent(Agent agent){
        agentList.add(agent);
    }

    public void exitGame(){
        battlefieldId = null;
        super.setReadyToPlay(false);
    }

    public List<Agent> getAgentList() {
        return agentList;
    }

    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }

    public UUID getBattlefieldId() {
        return battlefieldId;
    }

    public long getTaskSize() {
        return taskSize;
    }

    public long getNumberOfTasks() {
        return numberOfTasks;
    }

    public void setNumberOfTasks(long numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }
}
