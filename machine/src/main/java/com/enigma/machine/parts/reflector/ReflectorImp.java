package com.enigma.machine.parts.reflector;


import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;
import java.util.Map;

public class ReflectorImp implements Reflector {
    private final Map<Integer, Integer> entryToExit;
    private final String symbol;
    private final int id;


    public ReflectorImp(Map<Integer,Integer> inputOutPut, String symbol, int id){
        this.id = id;
        this.symbol = symbol;
        this.entryToExit = inputOutPut;
    }

    public ReflectorImp(ReflectorImp copyFrom){
        id = copyFrom.id;
        symbol = copyFrom.symbol;
        entryToExit = SerializationUtils.clone(new HashMap<>(copyFrom.entryToExit));
    }

    @Override
    public int reflect(int input) {
        return entryToExit.get(input);
    }

    @Override
    public String getIdSymbol() {
        return symbol;
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        return "Id: " + this.symbol
                +"\nMapping: "+ this.entryToExit;
    }
}

