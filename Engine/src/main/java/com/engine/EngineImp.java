package com.engine;

import com.engine.battlefield.Battlefield;
import com.engine.users.UserManager;
import com.engine.xmlReader.XmlReader;
import com.enigma.dtos.LogInAnswer;

import java.util.HashMap;
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
    public synchronized LogInAnswer uBoatLogIn(String name){
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
    public synchronized  LogInAnswer agentLogIn(String name){
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
    public synchronized  LogInAnswer allieLogIn(String name){
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




}
