package com.enigma.dtos.ServletAnswers;

import java.util.ArrayList;
import java.util.List;

public class MachinePartsAnswer {
    private  List<Integer> rotorIds;
    private  List<Integer> reflectorIds;
    private  List<String> keyboardChars;
    private int rotorsCount;


    public List<Integer> getRotorIds() {
        return rotorIds;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public List<Integer> getReflectorIds() {
        return reflectorIds;
    }

    public List<String> getKeyboardChars() {
        return keyboardChars;
    }

    public void setRotorIds(List<Integer> rotorIds) {
        this.rotorIds = rotorIds;
    }

    public void setReflectorIds(List<Integer> reflectorIds) {
        this.reflectorIds = reflectorIds;
    }

    public void setKeyboardChars(List<String> keyboardChars) {
        this.keyboardChars = keyboardChars;
    }
}
