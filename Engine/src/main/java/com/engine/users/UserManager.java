package com.engine.users;

import com.engine.battlefield.Battlefield;

import java.util.*;

public class UserManager {
    private Map<UUID,Uboat> uBoats;
    private Map<UUID, Allie> allies;
    private Map<UUID, Agent> agents;
    private Map<UUID, Battlefield> battlefields;

    private static final Object uBoatsLock = new Object();
    private static final Object alliesLock = new Object();
    private static final Object agentsLock =new Object();
    private static final Object battlefieldLock = new Object();

    public UserManager(){
        this.uBoats = new HashMap<>();
        this.agents = new HashMap<>();
        this.allies = new HashMap<>();
        this.battlefields = new HashMap<>();
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

    public Battlefield getBattlefieldById(UUID id){
        return battlefields.get(id);
    }

    public UUID addNewBattlefield(String battlefieldName){
        synchronized (battlefieldLock){
            Battlefield battlefield = new Battlefield(battlefieldName);
            this.battlefields.put(battlefield.getId(), battlefield);
            return battlefield.getId();
        }
    }

    public List<Battlefield> getAllAvailableBattlefields(){
        List<Battlefield> battlefieldList = new ArrayList<>();
        synchronized (battlefieldLock){
            battlefields.values().forEach(battlefield -> {
                if(!battlefield.isGameStarted())
                    battlefieldList.add(battlefield);
            });
        }
        return battlefieldList;
    }

    public boolean isBattlefieldExists(String name){
        synchronized (battlefieldLock){
            for(Battlefield battlefield : battlefields.values()){
                if(battlefield.getName().equals(name)){
                    return true;
                }
            }
            return false;
        }
    }

    public void removeBattlefieldById(UUID battlefieldId){
        synchronized (battlefieldLock){
            battlefields.get(battlefieldId).freeAllies();
            battlefields.remove(battlefieldId);
        }
    }

    public Map<UUID, Allie> getAllies() {
        synchronized (alliesLock){
            return allies;
        }
    }

    public Map<UUID, Battlefield> getBattlefields() {
        return battlefields;
    }

    public static Object getBattlefieldLock(){return battlefieldLock;}
}
