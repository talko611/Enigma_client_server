package com.engine;

import com.engine.DM_operations.utils.DmService;
import com.engine.configuration.Configurator;
import com.engine.enigmaParts.EnigmaParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.enums.DecryptionDifficulty;
import com.engine.users.Agent;
import com.engine.xmlReader.XmlReader;
import com.enigma.dtos.EngineAnswers.CalculationOperationAnswer;
import com.enigma.dtos.dataObjects.DecryptionTaskData;
import com.enigma.dtos.dataObjects.EncryptMessageData;
import com.enigma.dtos.EngineAnswers.InputOperationAnswer;
import com.enigma.dtos.ServletAnswers.MachineDetailsAnswer;
import com.enigma.machine.Machine;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class EngineImp implements Engine{
    private final XmlReader xmlReader;
    private final Configurator configurator;
    private final DmService dmService;


    public EngineImp(){
        this.xmlReader = new XmlReader();
        this.configurator = new Configurator();
        this.dmService = new DmService();
    }

    @Override
    public EnigmaParts loadGame( InputStream fileData) throws JAXBException, InputMismatchException, IOException {
        synchronized (xmlReader) {
           return xmlReader.load(fileData);
        }
    }

    @Override
    public InputOperationAnswer manualConfigRotors(Machine machine, MachineParts machineParts, String rotorConfigLine){
        if(machine.getRotors()!= null)  machine.reset();
        return this.configurator.manualConfigRotors(rotorConfigLine,machineParts, machine );
    }

    @Override
    public InputOperationAnswer manualConfigOffsets(Machine machine, String offsetsConfigLine){
        return this.configurator.manualConfigRotorsOffsets(offsetsConfigLine,machine);
    }

    @Override
    public InputOperationAnswer manualConfigReflector(Machine machine,MachineParts machineParts, String reflectorNum){
        return this.configurator.manualConfigReflector(reflectorNum,machine,machineParts);
    }

    @Override
    public InputOperationAnswer manualConfigPlugBoard(Machine machine, String plugBoardConfigLine){
        return this.configurator.manualConfigPlugBoard(plugBoardConfigLine, machine);
    }

    public InputOperationAnswer autoConfig(Machine machine, MachineParts machineParts){
        if (machine.getRotors() != null) machine.reset();
        return configurator.autoConfigMachine(machineParts, machine);
    }
    @Override
    public EncryptMessageData encryptDecrypt(EncryptMessageData src, Machine machine){
        String sourceMessage = src.getSource().toUpperCase();
        StringBuilder builder = new StringBuilder();
        EncryptMessageData encryptedData = new EncryptMessageData();
        for(int i = 0; i < sourceMessage.length(); ++i ){
            builder.append(machine.encryptDecrypt(String.valueOf(sourceMessage.charAt(i))));
        }
        machine.updateConfiguration();
        encryptedData.setEncrypted(builder.toString());
        encryptedData.setCurrentMachineConfiguration(machine.getCurrentConfiguration());
        return encryptedData;
    }

    @Override
    public MachineDetailsAnswer getDetails(Machine machine,MachineParts machineParts){
        MachineDetailsAnswer answer = new MachineDetailsAnswer();
        if(machine.getRotors() != null){
            answer.setConfig(true);
            answer.setCurrentConfig(machine.getCurrentConfiguration());
            answer.setInitialConfig(machine.getInitialConfiguration());
        }else{
            answer.setInitialConfig("unavailable");
            answer.setCurrentConfig("unavailable");
            answer.setConfig(false);
        }
        answer.setReflectorsNum(String.valueOf(machineParts.getAllReflectors().size()));
        String usedRotors = machine.getRotors() == null ? "0" : String.valueOf(machine.getRotors().size());
        answer.setUsedVsAvailRotors(usedRotors + "/" + machineParts.getAllRotors().size());
        return answer;
    }

    @Override
    public CalculationOperationAnswer calculateNumberOfTasks(DecryptionDifficulty difficulty, long taskSize, MachineParts machineParts, int rotorCount){
        CalculationOperationAnswer<Long> answer = new CalculationOperationAnswer<>();
        try{
            answer.setData(this.dmService.calculateNumberOfTasks(difficulty, taskSize, machineParts,rotorCount));
            answer.setSuccess(true);
            answer.setMessage("Operation passed");
        }catch (InputMismatchException e){
            answer.setSuccess(false);
            answer.setMessage(e.getMessage());
        }
        return answer;
    }

    @Override
    public Thread launchTaskProducer(String teamName, DecryptionDifficulty difficulty, List<Agent> agents, int taskSize, MachineParts machineParts, List<Integer> rotorsId, int reflectorId){
        return dmService.launchProducer(teamName, difficulty, agents, taskSize, machineParts, rotorsId, reflectorId);
    }


}
