package com.enigma.dtos.ServletAnswers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class GetMapOfData<T> {
    private Map<UUID,T> data;
    private String message;

    public GetMapOfData(){
        data = new HashMap<>();
    }

    public Map<UUID, ?> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addUser(UUID id, T user){
        data.put(id, user);
    }

    public void setData(Map<UUID, T> data) {
        this.data = data;
    }
}
