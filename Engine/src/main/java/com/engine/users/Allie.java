package com.engine.users;

import java.util.List;
import java.util.UUID;

public class Allie extends User{
    private UUID uBoatId;
    private UUID battlefieldId;
    private List<Agent> agentList;
    public Allie(String name){
        super(name);
    }

    public void setUBoatId(UUID uBoatId) {
        this.uBoatId = uBoatId;
    }

    public void setBattlefieldId(UUID battlefieldId) {
        this.battlefieldId = battlefieldId;
    }
}
