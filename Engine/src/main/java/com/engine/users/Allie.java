package com.engine.users;

import com.enigma.dtos.dataObjects.Candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Allie extends User{
    private UUID battlefieldId;
    private final List<Agent> activeAgents;
    private final List<Agent> waitingAgents;
    private long taskSize;
    private long numOfTasks;
    private Thread producer;
    private BlockingQueue<Candidate> candidates;

    public Allie(String name){
        super(name);
        this.activeAgents = new ArrayList<>();
        this.waitingAgents = new ArrayList<>();
        this.candidates = new LinkedBlockingQueue<>();
    }

    public void setBattlefieldId(UUID battlefieldId) {
        this.battlefieldId = battlefieldId;
    }

    public synchronized boolean addAgent(Agent agent){
        if(this.isReadyToPlay()){
            waitingAgents.add(agent);
            return false;
        }
        else{
            activeAgents.add(agent);
            return true;
        }
    }

    public void exitGame(){
        battlefieldId = null;
        super.setReadyToPlay(false);
        taskSize = 0;
        numOfTasks = 0;
    }

    public  List<Agent> getActiveAgents() {
        return activeAgents;
    }

    public synchronized void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }

    public UUID getBattlefieldId() {
        return battlefieldId;
    }

    public synchronized long getTaskSize() {
        return taskSize;
    }

    public synchronized long getNumOfTasks() {
        return numOfTasks;
    }

    public synchronized void setNumOfTasks(long numOfTasks) {
        this.numOfTasks = numOfTasks;
    }

    public void setProducer(Thread producer) {
        this.producer = producer;
    }
    public void stopProducing(){
        this.producer.interrupt();
    }

    public boolean isProducerStillRunning(){
        return this.producer.isAlive();
    }

    public BlockingQueue<Candidate> getCandidates() {
        return candidates;
    }
}
