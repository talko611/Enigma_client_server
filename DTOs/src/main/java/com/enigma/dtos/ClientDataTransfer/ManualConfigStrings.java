package com.enigma.dtos.ClientDataTransfer;

public class ManualConfigStrings {
    private String rotorConfigLine;
    private String offsetConfigLine;
    private String reflectorConfigLine;
    private String plugBoardConfigLine;

    public String getRotorConfigLine() {
        return rotorConfigLine;
    }

    public String getOffsetConfigLine() {
        return offsetConfigLine;
    }

    public String getReflectorConfigLine() {
        return reflectorConfigLine;
    }

    public String getPlugBoardConfigLine() {
        return plugBoardConfigLine;
    }

    public void setRotorConfigLine(String rotorConfigLine) {
        this.rotorConfigLine = rotorConfigLine;
    }

    public void setOffsetConfigLine(String offsetConfigLine) {
        this.offsetConfigLine = offsetConfigLine;
    }

    public void setReflectorConfigLine(String reflectorConfigLine) {
        this.reflectorConfigLine = reflectorConfigLine;
    }

    public void setPlugBoardConfigLine(String plugBoardConfigLine) {
        this.plugBoardConfigLine = plugBoardConfigLine;
    }
}
