package com.engine.enigmaParts.machineParts;

import com.engine.enums.ReflectorGreekNums;
import com.engine.generated.*;
import com.enigma.machine.parts.keyboard.Keyboard;
import com.enigma.machine.parts.keyboard.KeyboardImp;
import com.enigma.machine.parts.reflector.Reflector;
import com.enigma.machine.parts.reflector.ReflectorImp;
import com.enigma.machine.parts.rotor.Rotor;
import com.enigma.machine.parts.rotor.RotorImp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MachineParts {
    private Map<Integer, Rotor> idToRotor;
    private Map<Integer, Reflector> idToReflector;
    private Keyboard keyboard;
    private int minRotorCount;

    public void saveMachineParts(CTEMachine cteMachine){
        this.keyboard = saveKeyboard(cteMachine.getABC());
        this.idToRotor = saveRotors(cteMachine.getCTERotors());
        this.idToReflector = saveReflectors(cteMachine.getCTEReflectors());
        this.minRotorCount = cteMachine.getRotorsCount();
    }

    private Keyboard saveKeyboard(String abc){
        List<String> listOfAbc = Arrays.asList(abc.trim().toUpperCase().split(""));
        return new KeyboardImp(listOfAbc);
    }

    private Map<Integer,Rotor> saveRotors(CTERotors cteRotors){
        Map<Integer, Rotor> idToRotor = new HashMap<>();
        cteRotors.getCTERotor().forEach(i ->idToRotor.put(i.getId(), saveRotor(i)));
        return idToRotor;
    }

    private Rotor saveRotor(CTERotor cteRotor){
        List<String> right = cteRotor.
                getCTEPositioning().
                stream().
                map(CTEPositioning::getRight).
                collect(Collectors.toList());

        List<String> left = cteRotor
                .getCTEPositioning()
                .stream()
                .map(CTEPositioning::getLeft)
                .collect(Collectors.toList());
        Map<Integer, Integer> inputOutput = right.stream().collect(Collectors.toMap(right::indexOf, left::indexOf));
        Map<String, Integer> letterToInput = right.stream().collect(Collectors.toMap(i->i, right::indexOf));

        return new RotorImp(cteRotor.getId(), cteRotor.getNotch() -1, inputOutput, letterToInput,0);
    }

    private Map<Integer, Reflector> saveReflectors(CTEReflectors cteReflectors){
        Map<Integer, Reflector> idToReflector = new HashMap<>();
        cteReflectors.getCTEReflector().forEach(i-> idToReflector.put(getReflectorIntId(i.getId()), saveReflector(i)));
        return idToReflector;
    }

    private Reflector saveReflector(CTEReflector cteReflector){
        Map<Integer, Integer>inputOutPut = new HashMap<>();
        cteReflector.getCTEReflect().forEach((i)->{
            int input = i.getInput() - 1;
            int output = i.getOutput() - 1;
            inputOutPut.put(input,output);
            inputOutPut.put(output, input);
        });
        List<Integer> id = Arrays.stream(ReflectorGreekNums.values())
                .filter(i -> i.getSymbol().equals(cteReflector.getId()))
                .map(ReflectorGreekNums::getVal)
                .collect(Collectors.toList());

        return new ReflectorImp(inputOutPut, cteReflector.getId(), id.get(0));
    }

    private int getReflectorIntId(String greekNum){
        int res = -1;
        for(ReflectorGreekNums enumVal : ReflectorGreekNums.values()){
            if(enumVal.getSymbol().equals(greekNum)){
                res = enumVal.getVal();
                break;
            }
        }
        return res;
    }

    public Rotor getRotor(int id){
        return this.idToRotor.get(id);
    }

    public Map<Integer, Rotor> getAllRotors(){
        return this.idToRotor;
    }

    public int getRotorCount(){
        return this.minRotorCount;
    }

    public Map<Integer, Reflector> getAllReflectors(){
        return this.idToReflector;
    }

    public Reflector getReflector(int id){
        return this.idToReflector.get(id);
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }
}
