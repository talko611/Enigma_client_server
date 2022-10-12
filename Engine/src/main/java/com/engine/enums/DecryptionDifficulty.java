package com.engine.enums;

public enum DecryptionDifficulty {
    EASY("Easy"), MEDIUM("Medium"), HARD("Hard"), IMPOSSIBLE("Impossible");

    private final String strVal;
    DecryptionDifficulty(String toString){
        strVal = toString;
    }

    public String toString(){
        return strVal;
    }
}
