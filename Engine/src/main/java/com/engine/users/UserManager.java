package com.engine.users;

import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.Enums.GameStatus;

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

    public Allie addNewAllie(String name){
        Allie allie = new Allie(name);
        synchronized (alliesLock){
            allies.put(allie.getId(), allie);
        }
        return allie;
    }

    public Agent addNewAgent(String name, int numOfTasksCanAccept, UUID allieId){
        Agent agent = new Agent(name, allieId,numOfTasksCanAccept);
        synchronized (agentsLock){
            agents.put(agent.getId(), agent);
        }
        return agent;
    }

    public Uboat addNewUBoat(String name){
        Uboat uboat = new Uboat(name);
        synchronized (uBoatsLock){
            uBoats.put(uboat.getId(), uboat);
        }
        return uboat;
    }

    public Uboat getUBoatById(UUID id){
        synchronized (uBoatsLock){
            return uBoats.get(id);
        }
    }

    public Allie getAllieById(UUID id){
        synchronized (alliesLock){
            return allies.get(id);
        }
    }

    public Agent getAgentById(UUID id){
        synchronized (agentsLock){
            return agents.get(id);
        }
    }

    public Battlefield getBattlefieldById(UUID id){
        synchronized (battlefieldLock){
            return battlefields.get(id);
        }
    }

    public UUID addNewBattlefield(String battlefieldName){
        synchronized (battlefieldLock){
            Battlefield battlefield = new Battlefield(battlefieldName);
            this.battlefields.put(battlefield.getId(), battlefield);
            return battlefield.getId();
        }
    }

    //Todo - check if this function is relevant
    public List<Battlefield> getAllAvailableBattlefields(){
        List<Battlefield> battlefieldList = new ArrayList<>();
        synchronized (battlefieldLock){
            battlefields.values().forEach(battlefield -> {
                if(battlefield.getGameStatus() != GameStatus.RUNNING && battlefield.getGameStatus() != GameStatus.ENDING)
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
            battlefields.remove(battlefieldId);
        }
    }

    public void removeUBoat(UUID uBoatId){
        synchronized (uBoatsLock){
            uBoats.remove(uBoatId);
        }
    }

    public Map<UUID, Allie> getAllies() {
        synchronized (alliesLock){
            return allies;
        }
    }

    public Map<UUID, Battlefield> getBattlefields() {
        synchronized (battlefieldLock){
            return battlefields;
        }
    }

    public static Object getBattlefieldLock(){return battlefieldLock;}
    public static Object getAlliesLock(){
        return alliesLock;
    }

    public static Object getAgentsLock(){return agentsLock;}

    public static Object getuBoatsLock(){
        return uBoatsLock;
    }
}
