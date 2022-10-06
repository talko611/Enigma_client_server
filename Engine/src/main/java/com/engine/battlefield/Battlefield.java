package com.engine.battlefield;

import com.engine.enigmaParts.EnigmaParts;
import com.enigma.machine.Machine;
import com.enigma.machine.MachineImp;

import java.util.UUID;

public class Battlefield {
    private final String name;
    private final UUID id;
    private EnigmaParts enigmaParts;
    private Machine machine;
    private String encryptedMessage;
    private String decryptedMessage;
    private String messageInitialConfiguration;



    public Battlefield(String name){
        this.name = name;
        this.id = UUID.randomUUID();
        this.machine = new MachineImp();
    }

    public void setDecryptedMessage(String decryptedMessage) {
        this.decryptedMessage = decryptedMessage;
    }

    public String getDecryptedMessage() {
        return decryptedMessage;
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
    public String getMessageInitialConfiguration() {
        return messageInitialConfiguration;
    }
    public void setMessageInitialConfiguration(String messageInitialConfiguration) {
        this.messageInitialConfiguration = messageInitialConfiguration;
    }
}
