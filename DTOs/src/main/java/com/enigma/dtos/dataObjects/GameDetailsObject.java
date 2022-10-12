package com.enigma.dtos.dataObjects;

import java.util.UUID;

public class GameDetailsObject {
    private String battlefieldName;
    private String uBoatName;
    private String decryptionLevel;
    private String gameStatus;
    private String participantsStatus;

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public String getuBoatName() {
        return uBoatName;
    }

    public String getDecryptionLevel() {
        return decryptionLevel;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getParticipantsStatus() {
        return participantsStatus;
    }

    public void setBattlefieldName(String battlefieldName) {
        this.battlefieldName = battlefieldName;
    }

    public void setuBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }

    public void setDecryptionLevel(String decryptionLevel) {
        this.decryptionLevel = decryptionLevel;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setParticipantsStatus(String participantsStatus) {
        this.participantsStatus = participantsStatus;
    }
}
