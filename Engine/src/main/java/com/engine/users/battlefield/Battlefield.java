package com.engine.users.battlefield;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.users.Allie;
import com.engine.users.UserManager;
import com.enigma.dtos.Enums.GameStatus;
import com.enigma.dtos.dataObjects.Candidate;
import com.enigma.machine.Machine;
import com.enigma.machine.MachineImp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Battlefield {
    private final String name;
    private final UUID id;
    private UUID UBoatId;
    private List<Allie> teams;
    private EnigmaParts enigmaParts;
    private Machine machine;
    private String encryptedMessage;
    private String decryptedMessage;
    private String messageConfiguration;
    private String fileContent;
    private String winners;
    private GameStatus gameStatus;
    private BlockingQueue<Candidate> candidates;

    public Battlefield(String name){
        this.name = name;
        this.id = UUID.randomUUID();
        this.machine = new MachineImp();
        this.teams = new ArrayList<>();
        this.gameStatus = GameStatus.AWAITING;
        this.candidates = new LinkedBlockingQueue<>();
    }

    public void setDecryptedMessage(String decryptedMessage) {
        this.decryptedMessage = decryptedMessage;
    }

    public String getDecryptedMessage() {
        return decryptedMessage;
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
    public void removeAllie(Allie allie){
        teams.remove(allie);
    }

    public void freeAllies(){
        teams.forEach(Allie::exitGame);
    }

    public void updateActivateGame(UserManager userManager){
        if(teams.size() == enigmaParts.getBattlefieldParts().getNumOfAllies()){
            if(userManager.getUBoatById(UBoatId).isReadyToPlay()){
                if(isAllAlliesReady()){
                    this.gameStatus = GameStatus.RUNNING;
                }
            }
        }
    }

    private boolean isAllAlliesReady(){
        boolean answer = true;
        for(Allie allie : teams){
            if(!allie.isReadyToPlay()){
                answer = false;
                break;
            }
        }
        return answer;
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

    public BlockingQueue<Candidate> getCandidates() {
        return candidates;
    }
}
