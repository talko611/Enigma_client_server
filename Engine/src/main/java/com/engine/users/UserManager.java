package com.engine.users;

import java.util.*;

public class UserManager {
    private Map<UUID,Uboat> uBoats;
    private Map<UUID, Allie> allies;
    private Map<UUID, Agent> agents;

    private static final Object uBoatsLock = new Object();
    private static final Object alliesLock = new Object();
    private static final Object agentsLock =new Object();

    public UserManager(){
        this.uBoats = new HashMap<>();
        this.agents = new HashMap<>();
        this.allies = new HashMap<>();
    }

    public boolean isUBoatExists(String userName){
        synchronized (uBoatsLock){
            for(Uboat uboat : uBoats.values()){
                if(uboat.getName().equals(userName)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAllieExists(String username){
        synchronized (alliesLock){
            for(Allie allie : allies.values()){
                if(allie.getName().equals(username)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAgentExists(String username){
        synchronized (agentsLock){
            for (Agent agent : agents.values()){
                if(agent.getName().equals(username)){
                    return true;
                }
            }
        }
        return false;
    }

    public UUID addNewAllie(String name){
        synchronized (alliesLock){
            Allie allie = new Allie(name);
            allies.put(allie.getId(), allie);
            return allie.getId();
        }
    }

    public UUID addNewAgent(String name){
        synchronized (agentsLock){
            Agent agent = new Agent(name);
            agents.put(agent.getId(), agent);
            return agent.getId();
        }
    }

    public UUID addNewUBoat(String name){
        synchronized (uBoatsLock){
            Uboat uboat = new Uboat(name);
            uBoats.put(uboat.getId(), uboat);
            return uboat.getId();
        }
    }

    public Uboat getUBoatById(UUID id){
        return uBoats.get(id);
    }

    public Allie getAllieById(UUID id){
        return allies.get(id);
    }

    public Agent getAgentById(UUID id){
        return agents.get(id);
    }


}
