package com.engine.configuration;

import com.engine.enigmaParts.machineParts.MachineParts;
import com.enigma.dtos.EngineAnswers.InputOperationAnswer;
import com.enigma.machine.Machine;
import com.enigma.machine.parts.keyboard.Keyboard;
import com.enigma.machine.parts.plugBoard.PlugBoardImp;
import com.enigma.machine.parts.reflector.Reflector;
import com.enigma.machine.parts.rotor.Rotor;

import java.util.*;
import java.util.stream.Collectors;

public class Configurator {
    static final int MAX_ROTORS_NUM =99;

    public InputOperationAnswer manualConfigRotors(String configLine, MachineParts machineParts, Machine machine) {
        configLine = configLine.replaceAll(",", " ");
        if (!configLine.matches("[\\d ]+")) {
            return new InputOperationAnswer(false, "Error - please enter only numbers seperated by comma/space. you need to enter at least " + machineParts.getRotorCount() + " numbers (example: 12 23 1)");
        }
        List<Integer> rotorsIds = getRotorsNumbers(configLine);
        if (rotorsIds.size() > MAX_ROTORS_NUM || rotorsIds.size() < machineParts.getRotorCount()) {
            return new InputOperationAnswer(false, "Error - Number of ids is out of boundaries./n Machine rotor capacity is between " + machineParts.getRotorCount() + "-" + Math.min(MAX_ROTORS_NUM, machineParts.getAllRotors().size()));
        }
        if (!isRotorsIdsInOfBoundary(rotorsIds, machineParts)) {
            return new InputOperationAnswer(false, "Error - Rotor id is out of boundary. please enter only values between 1 -" + machineParts.getAllRotors().size());
        }
        if (!isAllRotorsUnique(rotorsIds)) {
            return new InputOperationAnswer(false, "Error- can not enter same rotor twice. Please get each rotor id once");
        }
        Map<Integer, Rotor> idToRotor = machineParts.getAllRotors();
        List<Rotor> rotorsInMachine = rotorsIds.
                stream().
                map(idToRotor::get).
                collect(Collectors.toList());
        machine.setRotors(rotorsInMachine);
        return new InputOperationAnswer(true, "Rotors config successfully");
    }

    private List<Integer> getRotorsNumbers(String rotorsConfigLine){
        Scanner scanner = new Scanner(rotorsConfigLine);
        List<Integer> rotorsIds = new ArrayList<>();
        while (scanner.hasNextBigInteger()){
            rotorsIds.add(scanner.nextInt());
        }
        return rotorsIds;
    }

    private boolean isRotorsIdsInOfBoundary(List<Integer> rotorsId, MachineParts machineParts){
        int idsOutOfBoundary = (int) rotorsId.stream()
                .filter(id -> !machineParts.getAllRotors().containsKey(id))
                .count();
        return idsOutOfBoundary == 0;
    }

    private boolean isAllRotorsUnique(List<Integer> rotorsIds){
        Map<Integer,Integer> idsToCount = new HashMap<>();
        rotorsIds.forEach(id -> idsToCount.merge(id, 1, Integer::sum));
        int idsWithMoreThenOneOccurrence = (int) idsToCount
                .values()
                .stream()
                .filter(i-> i > 1)
                .count();

        return idsWithMoreThenOneOccurrence == 0;
    }

    public InputOperationAnswer manualConfigRotorsOffsets(String configurationString, Machine machine){
        configurationString = configurationString.toUpperCase();
        List<Rotor> rotors = machine.getRotors();
        if(configurationString.length() != rotors.size()){
            return new InputOperationAnswer(false, "Error - offset configuration characters are not correspond to amount of rotors.\nPlease enter " + rotors.size() + " characters");
        }
        Keyboard keyboard = machine.getKeyboard();
        if(!isContainValidOffsets(configurationString, keyboard)){
            return new InputOperationAnswer(false, "Error - please enter only offsets from the define abc");
        }
        setRotorsOffsets(configurationString, machine);
        return new InputOperationAnswer(true, "Rotors offsets config successfully!");
    }

    private boolean isContainValidOffsets(String configurationString, Keyboard keyboard){
        boolean res = true;
        for (int i = 0; i < configurationString.length(); ++i){
            if(!keyboard.isKeyExists(String.valueOf(configurationString.charAt(i)))){
                res = false;
                break;
            }
        }
        return res;
    }

    private void setRotorsOffsets(String configurationString, Machine machine){
        List<Rotor> rotors = machine.getRotors();
        for(int i = 0; i < configurationString.length(); ++i){
            rotors.get(i).setOffset(String.valueOf(configurationString.charAt(i)));
        }
    }

    public InputOperationAnswer manualConfigReflector(String configStr, Machine machine, MachineParts parts){
        Scanner scanner = new Scanner(configStr);
        int reflectorNum = 0;
        if(scanner.hasNextBigInteger()) reflectorNum = scanner.nextInt();
        if(!configStr.matches("\\d") || !(reflectorNum >=1 && reflectorNum <= parts.getAllReflectors().size())){
            return new InputOperationAnswer(false, "Error - incorrect reflector number.");
        }
        Reflector reflector = parts.getReflector(reflectorNum);
        machine.setReflector(reflector);
        return new InputOperationAnswer(true, "Config reflector successfully!");
    }

    public InputOperationAnswer manualConfigPlugBoard(String configLine, Machine machine) {
        configLine = configLine.toUpperCase();
        Map<String, String> lettersPairs = new HashMap<>();

        if(configLine.length() % 2 != 0){
            return new InputOperationAnswer(false, "Error - Cannot config odd num of characters.\n please enter even number of characters");
        }
        if(configLine.length() / 2 > (machine.getKeyboardSize() / 2)){
            return new InputOperationAnswer(false, "Error - too many pairs of letter to config in plug board.\nCan config up to "+ machine.getKeyboardSize() /2 + " num of pairs");
        } else if (configLine.length() == 0) {
            machine.setPlugBord(new PlugBoardImp(lettersPairs));
        } else if (!allCharsAreValid(machine.getKeyboard(), configLine)) {
            return new InputOperationAnswer(false, "Error - Configuration line contains letters that not exists in keyBoard");
        } else {
            try {
                List<String> letters = Arrays.asList(configLine.split(""));
                machine.setPlugBord(new PlugBoardImp(buildPlugBoardMap(letters)));
            } catch (InputMismatchException e) {
                return new InputOperationAnswer(false, e.getMessage());
            }
        }
        return new InputOperationAnswer(true, "plugBoard configuration passed successfully!");
    }

    private boolean allCharsAreValid(Keyboard keyboard, String configLine){
        for(int i = 0; i < configLine.length(); ++i){
            if(!keyboard.isKeyExists(String.valueOf(configLine.charAt(i)))){
                return false;
            }
        }
        return true;
    }

    private Map<String, String> buildPlugBoardMap(List<String> letters) throws InputMismatchException{
        Map<String,String> lettersPairs = new HashMap<>();

        for(int i = 0; i < letters.size() -1; i += 2){
            String firstCurrLetter= letters.get(i);
            String secondCurrLetter = letters.get(i + 1);
            if(lettersPairs.containsKey(firstCurrLetter) || lettersPairs.containsKey(secondCurrLetter)){
                throw new InputMismatchException("Error - tried to connect two different letters to same letter");
            }
            if(firstCurrLetter.equals(secondCurrLetter)){
                throw new InputMismatchException("Error cannot plug a letter to itself.\nplease enter pairs of 2 different letters");
            }
            lettersPairs.put(firstCurrLetter, secondCurrLetter);
            lettersPairs.put(secondCurrLetter, firstCurrLetter);
        }
        return lettersPairs;
    }

    public InputOperationAnswer autoConfigMachine(MachineParts machineParts, Machine machine){
        Random random = new Random(System.currentTimeMillis());
        autoConfigRotors(machineParts,machine,random);
        autoConfigOffsets(machine,random);
        autoConfigReflector(machine,machineParts,random);
        machine.setPlugBord(new PlugBoardImp(new HashMap<>()));
        machine.setConfiguration();
        return new InputOperationAnswer(true, "Auto config machine successfully!");
    }

    private void autoConfigRotors(MachineParts enigmaParts, Machine machine,Random random){
        int maxRotorsInMachine = Math.min(enigmaParts.getAllRotors().size(), MAX_ROTORS_NUM);
        int amountOfRotors = random.nextInt(maxRotorsInMachine) + 1;
        while(amountOfRotors< enigmaParts.getRotorCount() || amountOfRotors > maxRotorsInMachine){
            amountOfRotors = random.nextInt(maxRotorsInMachine) + 1;
        }

        Set<Integer>chosenIds = new LinkedHashSet<>();
        while (chosenIds.size() != amountOfRotors){
            chosenIds.add(random.nextInt(amountOfRotors) + 1);
        }
        StringBuilder builder = new StringBuilder();
        chosenIds.forEach(i -> builder.append(i).append(" "));
        manualConfigRotors(builder.toString(), enigmaParts, machine);
    }

    private void autoConfigOffsets(Machine machine, Random random){
        StringBuilder builder = new StringBuilder();
        int amountOfValues = machine.getKeyboard().getKeyboardSize();
        machine.getRotors().forEach((i) -> {
            int offset = random.nextInt(amountOfValues);
            builder.append(machine.getKeyboard().getEntryMatchKey(offset));
        });
        manualConfigRotorsOffsets(builder.toString(),machine);
    }

    private void autoConfigReflector(Machine machine, MachineParts machineParts, Random random){
        int reflectorNum = random.nextInt(machineParts.getAllReflectors().size()) + 1;
        Reflector reflector = machineParts.getReflector(reflectorNum);
        machine.setReflector(reflector);
    }

}
