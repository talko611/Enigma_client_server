package com.enigma.dtos.dataObjects;

public class AgentProgressObject {
    private final long tasksAssigned;
    private final long tasksAccepted;
    private final long candidatesProduced;
    private final String agentName;

    public AgentProgressObject(long tasksAssigned, long tasksAccepted, long candidatesProduced, String agentName) {
        this.tasksAssigned = tasksAssigned;
        this.tasksAccepted = tasksAccepted;
        this.candidatesProduced = candidatesProduced;
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
    }

    public long getTasksAssigned() {
        return tasksAssigned;
    }

    public long getTasksAccepted() {
        return tasksAccepted;
    }

    public long getCandidatesProduced() {
        return candidatesProduced;
    }
}
