package com.engine.DM_operations.utils;

import com.engine.DM_operations.producerTask.ProduceTask;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.enums.DecryptionDifficulty;
import com.engine.users.Agent;
import com.enigma.dtos.dataObjects.DecryptionTaskData;

import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

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

    public Thread launchProducer(String teamName, DecryptionDifficulty difficulty, List<Agent> agents, int taskSize, MachineParts machineParts, List<Integer> rotorsId, int reflectorId){
        ProduceTask produceTask = null;
        switch (difficulty){
            case EASY:
                produceTask = new ProduceTask(difficulty, agents,machineParts, taskSize, rotorsId, reflectorId);
                break;
            case MEDIUM:
            case HARD:
                produceTask = new ProduceTask(difficulty, agents,machineParts, taskSize, rotorsId);
                break;
            case IMPOSSIBLE:
                produceTask =  new ProduceTask(difficulty, agents,machineParts, taskSize);
                break;
        }
        Thread producer = new Thread(produceTask);
        producer.setName(teamName + " producer");
        producer.start();
        return producer;
    }
}
