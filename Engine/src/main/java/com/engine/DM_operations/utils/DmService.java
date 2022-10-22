package com.engine.DM_operations.utils;

import com.engine.DM_operations.producerTask.ProduceTask;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.enums.DecryptionDifficulty;
import com.engine.users.Agent;

import java.util.InputMismatchException;
import java.util.List;

public class DmService {

    public long calculateNumberOfTasks(DecryptionDifficulty difficulty, Long taskSize, MachineParts machineParts, int rotorCount) throws InputMismatchException{
        long numberOfTasks = 0;
        switch (difficulty){
            case EASY:
                numberOfTasks = (long) Math.pow(machineParts.getKeyboard().getKeyboardSize(), rotorCount);
                break;
            case MEDIUM:
                numberOfTasks = machineParts.getAllReflectors().size();
                numberOfTasks *= (long) Math.pow(machineParts.getKeyboard().getKeyboardSize(), rotorCount);
                break;
            case HARD:
                numberOfTasks = CalculationsUtils.factorial(rotorCount);
                numberOfTasks *= machineParts.getAllReflectors().size();
                numberOfTasks *= (long) Math.pow(machineParts.getKeyboard().getKeyboardSize(), rotorCount);
                break;
            case IMPOSSIBLE:
                int possibleRotors = machineParts.getAllRotors().size();
                for(int i = machineParts.getRotorCount(); i <= Math.min(possibleRotors, 99); ++i){
                    numberOfTasks += (long) (CalculationsUtils.NumOfPermutations_k_of_n(possibleRotors, i) *
                            Math.pow(machineParts.getKeyboard().getKeyboardSize(), i)) *
                            machineParts.getAllReflectors().size();
                }
        }
        if(taskSize > numberOfTasks){
            throw new InputMismatchException("Task size is bigger than amount of work");
        }
        numberOfTasks /= taskSize;
        return numberOfTasks;
    }

    public Thread launchProducer(String teamName, DecryptionDifficulty difficulty, List<Agent> agents, int taskSize, MachineParts machineParts, List<Integer> rotorsId, int reflectorId, String encryptedMessage){
        ProduceTask produceTask = null;
        switch (difficulty){
            case EASY:
                produceTask = new ProduceTask(difficulty, agents,machineParts, taskSize, rotorsId, reflectorId, encryptedMessage);
                break;
            case MEDIUM:
            case HARD:
                produceTask = new ProduceTask(difficulty, agents,machineParts, taskSize, rotorsId, encryptedMessage);
                break;
            case IMPOSSIBLE:
                produceTask =  new ProduceTask(difficulty, agents,machineParts, taskSize, encryptedMessage);
                break;
        }
        Thread producer = new Thread(produceTask);
        producer.setName(teamName + " producer");
        producer.start();
        return producer;
    }
}
