package com.enigma.machine.parts.keyboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.SerializationUtils;

public class KeyboardImp implements Keyboard{
    private final Map<String, Integer> alphabetToEntryNum;

    public KeyboardImp(List<String> keyToEntryPoint){
        alphabetToEntryNum = IntStream
                .range(0, keyToEntryPoint.size())
                .boxed()
                .collect(Collectors.toMap(keyToEntryPoint::get, i->i));
    }

    public KeyboardImp (KeyboardImp copyFrom){
        this.alphabetToEntryNum = SerializationUtils.clone(new HashMap<>(copyFrom.alphabetToEntryNum));
    }

    @Override
    public boolean isKeyExists(String key) {
        return alphabetToEntryNum.containsKey(key);
    }

    @Override
    public int getEntryPointByKey(String key) {
        return alphabetToEntryNum.get(key);
    }

    @Override
    public String getEntryMatchKey(int entry) {
        for(Map.Entry<String, Integer> pair : alphabetToEntryNum.entrySet()){
            if(pair.getValue() == entry){
                return pair.getKey();
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return this.alphabetToEntryNum.toString();
    }

    @Override
    public int getKeyboardSize(){
        return this.alphabetToEntryNum.size();
    }

}


