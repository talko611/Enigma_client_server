package com.engine.users;

import java.util.*;

public class UserManager {
    private Map<UUID,Uboat> uBoats;
    private Map<UUID, Allie> allies;
    private Map<UUID, Agent> agents;

    public UserManager(){
        this.uBoats = new HashMap<>();
        this.agents = new HashMap<>();
        this.allies = new HashMap<>();
    }

    public boolean isUBoatExists(String userName){
        for(Uboat uboat : uBoats.values()){
            if(uboat.getName().equals(userName)){
                return true;
            }
        }
        return false;
    }

    public boolean isAllieExists(String username){
        for(Allie allie : allies.values()){
            if(allie.getName().equals(username)){
                return true;
            }
        }
        return false;
    }

    public boolean isAgentExists(String username){
        for (Agent agent : agents.values()){
            if(agent.getName().equals(username)){
                return true;
            }
        }
        return false;
    }

    public UUID addNewAllie(String name){
        Allie allie = new Allie(name);
        allies.put(allie.getId(), allie);
        return allie.getId();
    }

    public UUID addNewAgent(String name){
        Agent agent = new Agent(name);
        agents.put(agent.getId(), agent);
        return agent.getId();
    }

    public UUID addNewUBoat(String name){
        Uboat uboat = new Uboat(name);
        uBoats.put(uboat.getId(), uboat);
        return uboat.getId();
    }


}
