package com.engine;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.enums.DecryptionDifficulty;
import com.engine.users.Agent;
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

public interface Engine {
    EnigmaParts loadGame( InputStream fileData) throws JAXBException, InputMismatchException, IOException;

    InputOperationAnswer manualConfigRotors(Machine machine, MachineParts machineParts, String rotorConfigLine);

    InputOperationAnswer manualConfigPlugBoard(Machine machine, String plugBoardConfigLine);

    InputOperationAnswer manualConfigReflector(Machine machine,MachineParts machineParts, String reflectorNum);

    InputOperationAnswer manualConfigOffsets(Machine machine, String offsetsConfigLine);

    InputOperationAnswer autoConfig(Machine machine, MachineParts machineParts);

    EncryptMessageData encryptDecrypt(EncryptMessageData src, Machine machine);

    MachineDetailsAnswer getDetails(Machine machine, MachineParts machineParts);

    CalculationOperationAnswer calculateNumberOfTasks(DecryptionDifficulty difficulty, long taskSize, MachineParts machineParts, int rotorCount);

    Thread launchTaskProducer(String teamName, DecryptionDifficulty difficulty, List<Agent> agents, int taskSize, MachineParts machineParts, List<Integer> rotorsId, int reflectorId, String EncryptedMessage);

}
