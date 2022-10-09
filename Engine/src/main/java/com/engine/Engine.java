package com.engine;

import com.engine.battlefield.Battlefield;
import com.engine.enigmaParts.EnigmaParts;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.enigma.dtos.ClientDataTransfer.EncryptMessageData;
import com.enigma.dtos.EngineAnswers.InputOperationAnswer;
import com.enigma.dtos.ServletAnswers.MachineDetailsAnswer;
import com.enigma.machine.Machine;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.InputMismatchException;

public interface Engine {
    EnigmaParts loadGame( InputStream fileData) throws JAXBException, InputMismatchException;

    InputOperationAnswer manualConfigRotors(Machine machine, MachineParts machineParts, String rotorConfigLine);

    InputOperationAnswer manualConfigPlugBoard(Machine machine, String plugBoardConfigLine);

    InputOperationAnswer manualConfigReflector(Machine machine,MachineParts machineParts, String reflectorNum);

    InputOperationAnswer manualConfigOffsets(Machine machine, String offsetsConfigLine);

    InputOperationAnswer autoConfig(Machine machine, MachineParts machineParts);

    EncryptMessageData encryptDecrypt(EncryptMessageData src, Machine machine);

    MachineDetailsAnswer getDetails(Machine machine, MachineParts machineParts);

}
