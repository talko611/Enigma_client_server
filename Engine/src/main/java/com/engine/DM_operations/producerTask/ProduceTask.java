package com.engine.DM_operations.producerTask;

import com.engine.DM_operations.utils.CalculationsUtils;
import com.engine.enigmaParts.machineParts.MachineParts;
import com.engine.enums.DecryptionDifficulty;
import com.engine.users.Agent;
import com.enigma.dtos.dataObjects.DecryptionTaskData;

import java.util.*;

public class ProduceTask implements Runnable{
    private final DecryptionDifficulty difficulty;
    private final List<Agent> agents;
    private final int keyboardSize;
    private final int taskSize;
    private final MachineParts machineParts;
    private final List<Integer> rotorsIds;
    private final int reflectorId;
    private final String encryptedMessage;


    public ProduceTask(DecryptionDifficulty difficulty, List<Agent> agents, MachineParts machineParts, int taskSize, List<Integer> rotorsId, int reflectorId, String encryptedMessage) {
        this.difficulty = difficulty;
        this.agents = agents;
        this.machineParts = machineParts;
        this.keyboardSize = machineParts.getKeyboard().getKeyboardSize();
        this.taskSize = taskSize;
        this.rotorsIds = rotorsId;
        this.reflectorId = reflectorId;

        this.encryptedMessage = encryptedMessage;
    }

    public ProduceTask(DecryptionDifficulty difficulty, List<Agent> agents, MachineParts machineParts, int taskSize, List<Integer> rotorsIds, String encryptedMessage) {
        this.difficulty = difficulty;
        this.agents = agents;
        this.machineParts = machineParts;
        this.keyboardSize = machineParts.getKeyboard().getKeyboardSize();
        this.taskSize = taskSize;
        this.rotorsIds = rotorsIds;
        this.encryptedMessage = encryptedMessage;
        this.reflectorId = -1;
    }

    public ProduceTask(DecryptionDifficulty difficulty, List<Agent> agents, MachineParts machineParts, int taskSize, String encryptedMessage) {
        this.difficulty = difficulty;
        this.agents = agents;
        this.machineParts = machineParts;
        this.keyboardSize = machineParts.getKeyboard().getKeyboardSize();
        this.taskSize = taskSize;
        this.encryptedMessage = encryptedMessage;
        this.rotorsIds = null;
        this.reflectorId = -1;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start producing ");
        try{
            switch (this.difficulty){
                case EASY:
                    createTasksForEasyState(this.rotorsIds, this.reflectorId);
                    break;
                case MEDIUM:
                    createTasksForMediumState(this.rotorsIds);
                    break;
                case HARD:
                    createTasksForHardState(this.rotorsIds);
                    break;
                case IMPOSSIBLE:
                    createTasksForImpossibleState();
                    break;
            }
        }catch (InterruptedException e){
            System.out.println(Thread.currentThread().getName() + " is going down before finishing producing");
        }
        System.out.println(Thread.currentThread().getName() + " finish producing");
    }

    private void createTasksForEasyState(List<Integer> rotorsIds, int reflectorId) throws InterruptedException {
        long offsetPermutationNum = (long) Math.pow(this.keyboardSize, rotorsIds.size());
        List<Integer> offsetsList = new ArrayList<>(Collections.nCopies(rotorsIds.size(), 0));
        long counter = 0;
        int currentAgent = 0;
        int nextTaskSize;
        while(counter < offsetPermutationNum){
            nextTaskSize = offsetPermutationNum - counter < this.taskSize ? (int) (offsetPermutationNum - counter) : this.taskSize;
            addTask(rotorsIds, offsetsList, reflectorId, nextTaskSize, currentAgent);
            moveToNextConfiguration(offsetsList, nextTaskSize);
            counter += nextTaskSize;
            currentAgent = Math.floorMod(currentAgent + 1, agents.size());
        }
    }

    private void createTasksForMediumState(List<Integer> rotorsIds) throws InterruptedException {
        for(int reflectorId : machineParts.getAllReflectors().keySet()){
            createTasksForEasyState(rotorsIds, reflectorId);
        }
    }

    private void createTasksForHardState(List<Integer> rotorsIds) throws InterruptedException {
        List<List<Integer>> idsPermutations = CalculationsUtils.allPermutationOfNElements(rotorsIds);
        for(List<Integer> permutation : idsPermutations){
            createTasksForMediumState(permutation);
        }
    }

    private void createTasksForImpossibleState() throws InterruptedException {
        Set<Integer> rotorsIds = machineParts.getAllRotors().keySet();
        Set<Set<Integer>> allGroupsInSizeK;
        for(int i = machineParts.getRotorCount(); i <= Math.min(machineParts.getAllRotors().size(), 99); ++i){
            allGroupsInSizeK = CalculationsUtils.get_All_Sub_Groups_SizeK_Out_Of_N_Elements( rotorsIds, i);
            for(Set<Integer> chosenIds : allGroupsInSizeK){
                ArrayList<Integer> currentGroup = new ArrayList<>(chosenIds);
                createTasksForHardState(currentGroup);
            }
        }
    }

    private void addTask(List<Integer> rotorsIds, List<Integer> offsetList, int reflectorId, int taskSize, int agentNum) throws InterruptedException {
        DecryptionTaskData newTask = new DecryptionTaskData();
        newTask.setTaskSize(taskSize);
        newTask.setRotorsId(new ArrayList<>(rotorsIds));
        newTask.setOffsets(new ArrayList<>(offsetList));
        newTask.setReflectorId(reflectorId);
        newTask.setEncryptedMessage(encryptedMessage);
        Agent currentAgent = this.agents.get(agentNum);
        currentAgent.getTasksToPreform().put(newTask);
        currentAgent.addOneToTaskAssigned();
    }

    private void moveToNextConfiguration(List<Integer> offsetsConfiguration, int taskSize){
        for(int i = 0; i < taskSize; ++i){
            boolean moveNext = true;
            for(int j = 0; j < offsetsConfiguration.size() && moveNext; ++j){
                offsetsConfiguration.set(j, Math.floorMod(offsetsConfiguration.get(j) + 1, this.keyboardSize));
                moveNext = offsetsConfiguration.get(j) == 0;
            }
        }
    }
}
