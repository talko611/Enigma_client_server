package com.enigma.dtos.ServletAnswers;

public class MachineDetailsAnswer {
    private boolean isConfig;
    private String usedVsAvailRotors;
    private String reflectorsNum;
    private String initialConfig;
    private String currentConfig;

    public boolean isConfig() {
        return isConfig;
    }

    public String getUsedVsAvailRotors() {
        return usedVsAvailRotors;
    }

    public String getReflectorsNum() {
        return reflectorsNum;
    }

    public String getInitialConfig() {
        return initialConfig;
    }

    public String getCurrentConfig() {
        return currentConfig;
    }

    public void setConfig(boolean config) {
        isConfig = config;
    }

    public void setUsedVsAvailRotors(String usedVsAvailRotors) {
        this.usedVsAvailRotors = usedVsAvailRotors;
    }

    public void setReflectorsNum(String reflectorsNum) {
        this.reflectorsNum = reflectorsNum;
    }

    public void setInitialConfig(String initialConfig) {
        this.initialConfig = initialConfig;
    }

    public void setCurrentConfig(String currentConfig) {
        this.currentConfig = currentConfig;
    }
}
