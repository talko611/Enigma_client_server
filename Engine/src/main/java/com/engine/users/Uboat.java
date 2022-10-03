package com.engine.users;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Uboat extends User{
    Map<UUID, Allie> allies;

    public Uboat(String name){
        super(name);
        this.allies = new HashMap<>();
    }
}
