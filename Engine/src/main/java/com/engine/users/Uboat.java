package com.engine.users;

import java.util.*;

public class Uboat extends User{

    private UUID battlefieldId;

    public Uboat(String name){
        super(name);
    }

    public void setBattlefieldId(UUID battlefieldId) {
        this.battlefieldId = battlefieldId;
    }

    public UUID getBattlefieldId(){
        return this.battlefieldId;
    }

}
