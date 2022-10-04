package com.engine;

import com.engine.battlefield.Battlefield;
import com.engine.enigmaParts.EnigmaParts;
import com.engine.enums.UserType;
import com.engine.users.Uboat;
import com.engine.users.UserManager;
import com.engine.xmlReader.XmlReader;
import com.enigma.dtos.LoadFileAnswer;
import com.enigma.dtos.LogInAnswer;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.UUID;

public class EngineImp implements Engine{
    private XmlReader xmlReader;
    private final UserManager userManager;
    private final Map<UUID, Battlefield> battlefields;

    public EngineImp(){
        this.xmlReader = new XmlReader();
        this.userManager = new UserManager();
        this.battlefields = new HashMap<>();

    }

    @Override
    public LogInAnswer uBoatLogIn(String name){
        LogInAnswer answer = new LogInAnswer();

        if(this.userManager.isUBoatExists(name)){
            answer.setSuccess(false);
            answer.setMessage("User name is already taken, please select a different name");
        }else{
            answer.setSuccess(true);
            answer.setId(this.userManager.addNewUBoat(name));
            answer.setMessage("User is logged in");
        }
        return answer;
    }

    @Override
    public LogInAnswer agentLogIn(String name){
        LogInAnswer answer = new LogInAnswer();
        if(this.userManager.isAgentExists(name)){
            answer.setSuccess(false);
            answer.setMessage("Username is already taken. Please select different name");
        }else{
            answer.setSuccess(true);
            answer.setId(this.userManager.addNewAgent(name));
            answer.setMessage("User is logged in");
        }
        return answer;
    }

    @Override
    public LogInAnswer allieLogIn(String name){
        LogInAnswer answer = new LogInAnswer();
        if(this.userManager.isAllieExists(name)){
            answer.setSuccess(false);
            answer.setMessage("Username is already taken. Please select different name");
        }else{
            answer.setSuccess(true);
            answer.setId(this.userManager.addNewAllie(name));
            answer.setMessage("User is logged in");
        }
        return answer;
    }

    public LoadFileAnswer loadGame(UUID uBoatId, InputStream fileData){
        LoadFileAnswer answer = new LoadFileAnswer();
        synchronized (xmlReader){
            try{
                if(!isUserExists(uBoatId,UserType.U_BOAT)){
                    answer.setSuccess(false);
                    answer.setMessage("Error - Unknown User");
                    return answer;
                }
                Uboat uboat = this.userManager.getUBoatById(uBoatId);
                EnigmaParts parts = xmlReader.load(fileData);
                Battlefield battlefield = new Battlefield(parts.getBattlefieldParts().getName());
                battlefield.setEnigmaParts(parts);
                battlefields.put(battlefield.getId(), battlefield);
                uboat.setBattlefieldId(battlefield.getId());
                uboat.setBelongToBattlefield(true);
                answer.setSuccess(true);
                answer.setMessage("Battlefield is loaded");
            }catch (JAXBException | InputMismatchException e){
                answer.setSuccess(false);
                answer.setMessage(e.getMessage());
            }
        }
        return answer;
    }

    private boolean isUserExists(UUID id, UserType type){
        switch (type){
            case AGENT:
                return userManager.getAgentById(id) != null;
            case ALLIE:
                return userManager.getAllieById(id) != null;
            case U_BOAT:
                return userManager.getUBoatById(id) != null;
            default:
                return false;
        }
    }




}
