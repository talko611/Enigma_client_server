package com.engine.users;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Uboat extends User{
    private Map<UUID, Allie> allies;
    private UUID battlefieldId;

    public Uboat(String name){
        super(name);
        this.allies = new HashMap<>();
    }

    public void setBattlefieldId(UUID battlefieldId) {
        this.battlefieldId = battlefieldId;
    }

    public UUID getBattlefieldId(){
        return this.battlefieldId;
    }
}
