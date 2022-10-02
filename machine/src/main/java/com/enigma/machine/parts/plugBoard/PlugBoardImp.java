package com.enigma.machine.parts.plugBoard;

import java.util.Map;

public class PlugBoardImp implements PlugBoard {
    private final Map<String, String> lettersPairs;

    public PlugBoardImp(Map<String,String> lettersPairs){
        this.lettersPairs = lettersPairs;
    }

    @Override
    public String switchLetters(String letter) {
        if(lettersPairs.containsKey(letter)){
            return lettersPairs.get(letter);
        }
        return letter;
    }

    @Override
    public Map<String, String> getAllPlugs() {
        return this.lettersPairs;
    }

    @Override
    public String toString(){
        return lettersPairs.toString();
    }
}

