package com.engine.users;

import java.util.*;

public class Uboat extends User{
    private List<Allie> allies;
    private UUID battlefieldId;

    public Uboat(String name){
        super(name);
        this.allies = new ArrayList<>();
    }

    public void setBattlefieldId(UUID battlefieldId) {
        this.battlefieldId = battlefieldId;
    }

    public UUID getBattlefieldId(){
        return this.battlefieldId;
    }

    public List<Allie> getAllies() {
        return allies;
    }

    public void addAllie(Allie allie){
        allies.add(allie);
    }
}
