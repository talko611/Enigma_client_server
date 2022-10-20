package com.engine.users;

import com.engine.enums.AgentStatus;
import com.enigma.dtos.dataObjects.DecryptionTaskData;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Agent extends User{
    private UUID AllieId;
    private int numOfThreads;
    private int numOfTaskAccepted;
    private int numOfTaskAssigned;
    private int numOfCandidatesProduced;
    private AgentStatus status;
    private final BlockingQueue<DecryptionTaskData> tasksToPreform;
    public Agent(String name){
        super(name);
        this.status = AgentStatus.AWAITING;
        this.tasksToPreform = new ArrayBlockingQueue<>(1000);
    }

    public BlockingQueue<DecryptionTaskData> getTasksToPreform() {
        return tasksToPreform;
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

    public synchronized int getNumOfTaskAccepted() {
        return numOfTaskAccepted;
    }

    public synchronized int getNumOfTaskAssigned() {
        return numOfTaskAssigned;
    }

    public synchronized int getNumOfCandidatesProduced() {
        return numOfCandidatesProduced;
    }

    public synchronized void setNumOfTaskAccepted(int numOfTaskAccepted) {
        this.numOfTaskAccepted = numOfTaskAccepted;
    }

    public synchronized void setNumOfTaskAssigned(int numOfTaskAssigned) {
        this.numOfTaskAssigned = numOfTaskAssigned;
    }

    public synchronized void setNumOfCandidatesProduced(int numOfCandidatesProduced) {
        this.numOfCandidatesProduced = numOfCandidatesProduced;
    }
    public synchronized void addOneToTaskAccepted(){
        this.numOfTaskAccepted += 1;
    }
}
