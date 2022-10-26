package com.enigma.machine;

import com.enigma.machine.enums.RotorDirection;
import com.enigma.machine.parts.keyboard.Keyboard;
import com.enigma.machine.parts.plugBoard.PlugBoard;
import com.enigma.machine.parts.reflector.Reflector;
import com.enigma.machine.parts.rotor.Rotor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MachineImp implements Machine{
    private List<Rotor> rotors;
    private Reflector reflector;
    private PlugBoard plugBoard;
    private Keyboard keyboard;

    private String initialConfiguration;
    private String currentConfiguration;

    public MachineImp(){
        this.rotors = null;
        this.keyboard = null;
        this.plugBoard = null;
        this.reflector = null;
    }

    @Override
    public String encryptDecrypt(String input){
        input = input.toUpperCase();
        moveRotors();
        int entryPoint = keyboard.getEntryPointByKey(plugBoard.switchLetters(input));
        for(Rotor rotor : rotors){
            entryPoint = rotor.scramble(entryPoint, RotorDirection.In);
        }
        entryPoint = reflector.reflect(entryPoint);
        for(int i = rotors.size() -1; i >= 0; --i){
            entryPoint = rotors.get(i).scramble(entryPoint, RotorDirection.Out);
        }
        return plugBoard.switchLetters(keyboard.getEntryMatchKey(entryPoint));
    }

    private void moveRotors(){
        boolean canMove = true;
        for(Rotor rotor : rotors) {
            canMove = rotor.move(canMove);
        }
    }

    @Override
    public void reset(){
        this.rotors.forEach(Rotor::reset);
        updateConfiguration();
    }

    @Override
    public void setRotors(List<Rotor> rotors) {
        this.rotors = rotors;
    }

    @Override
    public List<Rotor> getRotors() {
        return rotors;
    }

    @Override
    public void setReflector(Reflector reflector) {
        this.reflector = reflector;
    }

    public Reflector getReflector() {
        return reflector;
    }

    @Override
    public void setPlugBord(PlugBoard plugBord) {
        this.plugBoard = plugBord;
    }

    public PlugBoard getPlugBord(){return plugBoard;}

    @Override
    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public Keyboard getKeyboard(){return this.keyboard;}

    @Override
    public int getKeyboardSize(){
        return this.keyboard.getKeyboardSize();
    }

    @Override
    public void setConfiguration(){
        this.initialConfiguration = this.currentConfiguration = buildConfiguration();
    }

    @Override
    public void updateConfiguration(){
        this.currentConfiguration = buildConfiguration();
    }

    @Override
    public String getCurrentConfiguration() {
        return currentConfiguration;
    }

    @Override
    public String getInitialConfiguration() {
        return initialConfiguration;
    }

    private String buildConfiguration(){
        StringBuilder builder = new StringBuilder();
        buildRotorsConfigurationPart(builder);
        buildOffsetsConfigurationPart(builder);
        builder.append("<").append(reflector.getIdSymbol()).append(">");
        buildPlugsConfigurationPart(builder);
        return builder.toString();
    }

    private void buildRotorsConfigurationPart(StringBuilder builder){
        builder.append("<");
        for(int i = rotors.size() -1 ; i >=0 ;--i){
            builder.append(rotors.get(i).getId()).append(",");
        }
        builder.deleteCharAt(builder.toString().length() -1);
        builder.append(">");
    }

    private void buildOffsetsConfigurationPart(StringBuilder builder){
        builder.append("<");
        for(int i = rotors.size() -1; i >= 0; --i){
            Rotor current = rotors.get(i);
            builder.append(current.getCurrentOffset())
                    .append("(")
                    .append(current.getNotchStepsToZero())
                    .append("),");
        }
        builder.deleteCharAt(builder.toString().length() -1);
        builder.append(">");
    }

    private void buildPlugsConfigurationPart(StringBuilder builder){
        if(!this.plugBoard.getAllPlugs().isEmpty()){
            Map<String, Boolean> isLetterAdded = this.plugBoard
                    .getAllPlugs()
                    .keySet()
                    .stream()
                    .collect(Collectors
                            .toMap(i -> i, i->false));
            builder.append("<");
            String first, second;
            for (Map.Entry<String, String> entry : this.plugBoard.getAllPlugs().entrySet()){
                first = entry.getKey();
                second = entry.getValue();
                if(!isLetterAdded.get(first) && !isLetterAdded.get(second)){
                    builder.append(first).append("|").append(second).append(",");
                    isLetterAdded.replace(first,true);
                    isLetterAdded.replace(second,true);
                }
            }
            builder.deleteCharAt(builder.toString().length() -1).append(">");
        }
    }

}

