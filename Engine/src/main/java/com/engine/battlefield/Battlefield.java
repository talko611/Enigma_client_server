package com.engine.battlefield;

import com.engine.enigmaParts.EnigmaParts;
import com.enigma.machine.Machine;

import java.util.UUID;

public class Battlefield {
    private final String name;
    private final UUID id;
    EnigmaParts enigmaParts;
    Machine machine;
    String encryptedMessage;


    public Battlefield(String name){
        this.name = name;
        this.id = UUID.randomUUID();
    }

}
