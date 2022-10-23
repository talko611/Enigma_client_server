package com.engine.users;

import com.engine.enums.AgentStatus;
import com.enigma.dtos.dataObjects.DecryptionTaskData;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Agent extends User{
    private UUID allieId;
    private int numOfTaskCanAccept;
    private long numOfTaskAccepted;
    private long numOfTaskAssigned;
    private int numOfCandidatesProduced;
    private AgentStatus status;
    private final BlockingQueue<DecryptionTaskData> tasksToPreform;
    public Agent(String name, UUID allieId, int numOfTaskCanAccept){
        super(name);
        this.allieId = allieId;
        this.numOfTaskCanAccept = numOfTaskCanAccept;
        this.status = AgentStatus.AWAITING;
        this.tasksToPreform = new ArrayBlockingQueue<>(numOfTaskCanAccept);
    }

    public BlockingQueue<DecryptionTaskData> getTasksToPreform() {
        return tasksToPreform;
    }

    public UUID getAllieId() {
        return allieId;
    }

    public int getNumOfTaskCanAccept() {
        return numOfTaskCanAccept;
    }

    public synchronized void setNumOfTaskCanAccept(int numOfTaskCanAccept) {
        this.numOfTaskCanAccept = numOfTaskCanAccept;
    }

    public synchronized AgentStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(AgentStatus status) {
        this.status = status;
    }

    public synchronized long getNumOfTaskAccepted() {
        return numOfTaskAccepted;
    }

    public synchronized int getNumOfCandidatesProduced() {
        return numOfCandidatesProduced;
    }

    public synchronized void setNumOfTaskAccepted(int numOfTaskAccepted) {
        this.numOfTaskAccepted = numOfTaskAccepted;
    }

    public synchronized void setNumOfCandidatesProduced(int numOfCandidatesProduced) {
        this.numOfCandidatesProduced = numOfCandidatesProduced;
    }
    public synchronized void addOneToTaskAssigned(){
        this.numOfTaskAssigned += 1;
    }

    public synchronized void addToTasksAccepted(int num){
        this.numOfTaskAccepted += num;
    }

    public synchronized long getNumOfTaskAssigned() {
        return numOfTaskAssigned;
    }
}
