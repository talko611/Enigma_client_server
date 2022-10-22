package com.enigma.dtos.dataObjects;

public class Candidate {
    private String decryption;
    private String configuration;
    private String teamName;
    private String agentName;

    public Candidate(String decryption, String configuration, String teamName) {
        this.decryption = decryption;
        this.configuration = configuration;
        this.teamName = teamName;
    }

    public Candidate(Candidate copyFrom){
        this.decryption = copyFrom.decryption;
        this.configuration = copyFrom.configuration;
        this.teamName = copyFrom.teamName;
        this.agentName = copyFrom.agentName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getDecryption() {
        return decryption;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setDecryption(String decryption) {
        this.decryption = decryption;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
