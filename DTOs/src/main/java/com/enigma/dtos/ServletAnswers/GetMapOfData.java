package com.enigma.dtos.ServletAnswers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class GetMapOfData<T> {
    private Map<UUID,T> listOfUsers;
    private String message;

    public GetMapOfData(){
        listOfUsers = new HashMap<>();
    }

    public Map<UUID, ?> getListOfUsers() {
        return listOfUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addUser(UUID id, T user){
        listOfUsers.put(id, user);
    }

    public void setListOfUsers(Map<UUID, T> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }
}
