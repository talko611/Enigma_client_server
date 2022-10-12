package com.engine.DM_operations.utils;

import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.enums.DecryptionDifficulty;

import java.util.InputMismatchException;

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
}
