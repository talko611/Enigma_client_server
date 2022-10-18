package com.enigma.dtos.dataObjects;

import com.enigma.dtos.Enums.GameStatus;

public class GameDetailsObject {
    private String battlefieldName;
    private String uBoatName;
    private String decryptionLevel;
    private GameStatus gameStatus;
    private String participantsStatus;
    private String encryptedMessage;

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public String getuBoatName() {
        return uBoatName;
    }

    public String getDecryptionLevel() {
        return decryptionLevel;
    }

    public GameStatus getGameStatus() {
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

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setParticipantsStatus(String participantsStatus) {
        this.participantsStatus = participantsStatus;
    }
}
