package com.engine.battlefield;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.users.Allie;
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
    private boolean isGameStarted;



    public Battlefield(String name){
        this.name = name;
        this.id = UUID.randomUUID();
        this.machine = new MachineImp();
        this.teams = new ArrayList<>();
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

    public synchronized void addNewAllie(Allie allie){
        teams.add(allie);
    }
    public synchronized void removeAllie(Allie allie){
        teams.remove(allie);
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public void freeAllies(){
        teams.forEach(Allie::exitGame);
    }

}
