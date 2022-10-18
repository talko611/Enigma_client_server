package com.enigma.dtos.Enums;

public enum GameStatus {
    AWAITING("Awaiting"), RUNNING("Running"), ENDING("Ending");
    private final String val;
    GameStatus(String status){
        this.val = status;
    }

    @Override
    public String toString() {
        return val;
    }
}
