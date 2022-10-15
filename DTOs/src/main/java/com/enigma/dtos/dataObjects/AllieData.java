package com.enigma.dtos.dataObjects;

public class AllieData {
    private String alliName;
    private int numOfAgents;
    private long taskSize;

    public void setAlliName(String alliName) {
        this.alliName = alliName;
    }

    public void setNumOfAgents(int numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }

    public String getAlliName() {
        return alliName;
    }

    public int getNumOfAgents() {
        return numOfAgents;
    }

    public long getTaskSize() {
        return taskSize;
    }
}
