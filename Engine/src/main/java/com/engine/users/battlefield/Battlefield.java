package com.engine.users.battlefield;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.Enums.GameStatus;
import com.enigma.machine.Machine;
import com.enigma.machine.MachineImp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Battlefield {
    private final String name;
    private final UUID id;
    private UUID UBoatId;
    private List<Allie> teams;
    private EnigmaParts enigmaParts;
    private Machine machine;
    private String encryptedMessage;
    private String messageConfiguration;
    private String fileContent;
    private String winners;
    private GameStatus gameStatus;

    public Battlefield(String name){
        this.name = name;
        this.id = UUID.randomUUID();
        this.machine = new MachineImp();
        this.teams = new ArrayList<>();
        this.gameStatus = GameStatus.AWAITING;
    }

    public void setMessageConfiguration(String messageConfiguration) {
        this.messageConfiguration = messageConfiguration;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getMessageConfiguration() {
        return messageConfiguration;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setEnigmaParts(EnigmaParts enigmaParts) {
        this.enigmaParts = enigmaParts;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public EnigmaParts getEnigmaParts() {
        return enigmaParts;
    }

    public Machine getMachine() {
        return machine;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setUBoatId(UUID UBoatId) {
        this.UBoatId = UBoatId;
    }

    public void setTeams(List<Allie> teams) {
        this.teams = teams;
    }

    public UUID getUBoatId() {
        return UBoatId;
    }

    public List<Allie> getTeams() {
        return teams;
    }

    public void addNewAllie(Allie allie){
        teams.add(allie);
    }
    public synchronized void removeAllie(Allie allie){
        teams.remove(allie);
    }

    public void freeAllies(){
        teams.forEach(Allie::exitGame);
    }

    public void updateActivateGame(UserManager userManager){
        boolean status = userManager.getUBoatById(UBoatId).isReadyToPlay();
        if(status){
            for(Allie allie : teams){
                if(!allie.isReadyToPlay()){
                    status = false;
                    break;
                }
            }
        }
        this.gameStatus = GameStatus.RUNNING;
    }

    public String getWinners() {
        return winners;
    }

    public void setWinners(String winners) {
        this.winners = winners;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
