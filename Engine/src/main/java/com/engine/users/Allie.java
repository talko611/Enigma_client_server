package com.engine.users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Allie extends User{
    private UUID battlefieldId;
    private final List<Agent> agentList;
    public Allie(String name){
        super(name);
        this.agentList = new ArrayList<>();
    }

    public void setBattlefieldId(UUID battlefieldId) {
        this.battlefieldId = battlefieldId;
    }

    public synchronized void addAgent(Agent agent){
        agentList.add(agent);
    }

    public void exitGame(){
        battlefieldId = null;
        super.setBelongToBattlefield(false);
        super.setReadyToPlay(false);
    }
}
