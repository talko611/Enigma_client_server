package com.engine.xmlReader;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.enums.ReflectorGreekNums;
import com.engine.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XmlReader {
    private static final int MAX_AGENTS = 50;

    public EnigmaParts load(InputStream fileData) throws JAXBException, InputMismatchException {
        //load and validate
        CTEEnigma cteEnigma = loadFile(fileData);
        CTEMachine cteMachine = cteEnigma.getCTEMachine();
        cteMachine.setABC(cteMachine.getABC().trim());
        isAbcEven(cteMachine.getABC());
        CTERotors cteRotors = cteMachine.getCTERotors();
        isRotorsCountValid(cteMachine.getRotorsCount(), cteRotors.getCTERotor().size());
        isAllRotorsValid(cteRotors, cteMachine.getABC().length());
        isReflectorsValid(cteMachine.getCTEReflectors(), cteMachine.getABC().length());
        //Save to my object
        EnigmaParts enigmaParts = new EnigmaParts();
        enigmaParts.saveEnigmaParts(cteEnigma);
        return enigmaParts;
    }

    private CTEEnigma loadFile(InputStream fileData) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CTEEnigma.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (CTEEnigma) unmarshaller.unmarshal(fileData);
    }

    private void isAbcEven(String abc) throws InputMismatchException {
        int abcLength = abc.length();
        if(abcLength != 0 && abcLength % 2 == 0 ){
            return;
        }
        throw new InputMismatchException("Error- File contains odd num of character. Enigma machine does not support odd num of charters");
    }

    private void isRotorsCountValid(int rotorCount, int numOfRotor)throws InputMismatchException{
        if(rotorCount < 2){
            throw new InputMismatchException("Error - File does not contain enough rotors(count < 2)");
        }
        if(numOfRotor < rotorCount){
            throw new InputMismatchException("Error - Number of rotors is not match the minimum of rotors number the machine needs");
        }
    }

    private void isAllRotorsValid(CTERotors rotors, int  abc) throws InputMismatchException{
        Map<Integer,Boolean> idToIsExist = IntStream
                .range(1, rotors.getCTERotor().size() + 1)
                .boxed()
                .collect(Collectors.toMap(i->i, i->false));

        for(CTERotor rotor : rotors.getCTERotor()){
            int rotorId = rotor.getId();
            if(!idToIsExist.containsKey(rotorId)){
                throw new InputMismatchException("Error - rotors ID's are not in range of rotors count(There is an id which is greater than amount of rotors");
            }
            if(idToIsExist.get(rotorId)){
                throw new InputMismatchException("Error - rotors ID's are not unique");
            }
            idToIsExist.put(rotorId,true);
            isNotchValid(rotor.getNotch(), abc);
            isRotorInputToOutPutCorrect(rotor, abc);
        }
    }

    private void isNotchValid(int notch, int abcSize) throws InputMismatchException{
        if(notch > abcSize){
            throw new InputMismatchException("Error - notch position is out of boundaries");
        }
    }

    private void isRotorInputToOutPutCorrect(CTERotor rotor, int abc) throws InputMismatchException{
        List<CTEPositioning> positions = rotor.getCTEPositioning();
        if(positions.size() != abc){
            throw new InputMismatchException("Error - number of entries in the rotors is not correspond to number of ABC");
        }
        Map<String, Integer> leftLettersToCount = new HashMap<>();
        Map<String, Integer> rightLettersToCount = new HashMap<>();

        for(CTEPositioning positioning : positions){
            leftLettersToCount.merge(positioning.getLeft(), 1, Integer::sum);
            rightLettersToCount.merge(positioning.getRight(), 1, Integer::sum);
        }

        int left = (int) leftLettersToCount.values().stream().filter(i -> i > 1).count();
        int right = (int) rightLettersToCount.values().stream().filter(i -> i > 1).count();
        if(!(right == 0 && left == 0)){
            throw new InputMismatchException("Error - Double mapping in rotor");
        }
    }

    private void isReflectorsValid(CTEReflectors reflectors, int abc) throws InputMismatchException{
        Map<String, Boolean> reflectorsSymbolToExist = Arrays.stream(ReflectorGreekNums
                        .values())
                .limit(reflectors.getCTEReflector().size())
                .collect(Collectors.toMap(ReflectorGreekNums::getSymbol, i -> false));
        for(CTEReflector reflector : reflectors.getCTEReflector()){
            String reflectorId = reflector.getId();
            if(!reflectorsSymbolToExist.containsKey(reflectorId)){
                throw new InputMismatchException("Error - invalid reflector number (Can get only values between I - V in greek numbers in ascending order)");
            }
            if(reflectorsSymbolToExist.get(reflectorId)){
                throw new InputMismatchException("Error - reflectors ids are not unique");
            }
            isReflectorMappingValid(reflector,abc);
            reflectorsSymbolToExist.put(reflectorId, true);
        }
    }

    private void isReflectorMappingValid(CTEReflector reflector, int abc){
        if(reflector.getCTEReflect().size() != (abc / 2)){
            throw new InputMismatchException("Error - Reflector entries are not mapped to pairs");
        }
        Map<Integer, Integer> reflectorMapping = new HashMap<>();
        int input, output;
        for(CTEReflect entry : reflector.getCTEReflect()){
            input = entry.getInput();
            output = entry.getOutput();
            if(input == output){
                throw new InputMismatchException("Error - Reflector mapping input to same output");
            }
            if(reflectorMapping.containsKey(input) || reflectorMapping.containsKey(output)){
                throw new InputMismatchException("Error - Reflector has double mapping");
            }
            reflectorMapping.put(input, output);
            reflectorMapping.put(output, input);
        }
    }
}
