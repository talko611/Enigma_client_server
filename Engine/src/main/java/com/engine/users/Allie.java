package com.engine.users;

import com.engine.enums.AgentStatus;
import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.dataObjects.Candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Allie extends User{
    private UUID battlefieldId;
    private final List<Agent> activeAgents;
    private final List<Agent> waitingAgents;
    private long taskSize;
    private long numOfTasks;
    private Thread producer;
    private final BlockingQueue<Candidate> candidates;

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

    public synchronized void exitGame(UserManager userManager){
        Battlefield battlefield = userManager.getBattlefieldById(this.battlefieldId);
        if(battlefield != null){
            battlefield.removeAllie(this);
        }
        battlefieldId = null;
        super.setReadyToPlay(false);
        super.setInActiveGame(false);
        taskSize = 0;
        numOfTasks = 0;
        this.candidates.clear();
        resetActiveAgents();
        activateWaitingAgents();
    }

    public void resetActiveAgents(){
        this.activeAgents.forEach(agent -> {
            agent.setNumOfTaskAccepted(0);
            agent.setNumOfTaskAssigned(0);
            agent.setNumOfCandidatesProduced(0);
            agent.getTasksToPreform().clear();
        });
    }

    private void activateWaitingAgents(){
        this.waitingAgents.forEach(agent -> agent.setStatus(AgentStatus.ACTIVE));
        this.activeAgents.addAll(waitingAgents);
        waitingAgents.clear();
    }

    public void stopProducer(){
        this.producer.interrupt();
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

    public boolean isProducerStillRunning(){
        return this.producer.isAlive();
    }

    public BlockingQueue<Candidate> getCandidates() {
        return candidates;
    }
}
