package com.enigma.main.tasks;

import com.enigma.dtos.dataObjects.Candidate;
import com.enigma.machine.Machine;
import com.enigma.machine.parts.keyboard.Keyboard;
import com.enigma.machine.parts.rotor.Rotor;
import com.enigma.utils.AppUtils;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class DecryptionTask implements Runnable{
    private final Machine machine;
    private final String encryptedMessage;
    private final String teamName;
    private final int taskSize;
    private final List<Integer> offsetPositions;
    private final Set<String> dictionary;
    private final Consumer<Long> updateProgress;
    private final SimpleLongProperty taskPreformed;
    private final Consumer<Candidate> reportFoundCandidates;
    private final List<Candidate> candidatesToReportServer;

    public DecryptionTask(Machine machine, String encryptedMessage, int taskSize, List<Integer> initialOffsetsPos, Set<String> dictionary, Consumer<Long> updateProgress, String teamName, SimpleLongProperty taskPreformed, Consumer<Candidate> reportFoundCandidates) {
        this.machine = machine;
        this.encryptedMessage = encryptedMessage;
        this.taskSize = taskSize;
        this.offsetPositions = initialOffsetsPos;
        this.dictionary = dictionary;
        this.updateProgress = updateProgress;
        this.teamName = teamName;
        this.taskPreformed = taskPreformed;
        this.reportFoundCandidates = reportFoundCandidates;
        this.candidatesToReportServer = new ArrayList<>();
    }

    @Override
    public void run() {
        for(int i = 0; i < this.taskSize; ++i){
            setOffsets();
            String currentDecryption = decrypt();
            if(isOptionalDecryption(currentDecryption)){
                String initialConfiguration = this.machine.getInitialConfiguration();
                candidatesToReportServer.add(new Candidate(currentDecryption, initialConfiguration, this.teamName));
                Platform.runLater(()->reportFoundCandidates.accept(new Candidate(currentDecryption, initialConfiguration, this.teamName)));
            }
            moveToNextConfiguration();
        }

        Platform.runLater(()->{
            taskPreformed.set(taskPreformed.get() + 1);
            updateProgress.accept(taskPreformed.get());
        });
        updateCandidates();
    }

    private String decrypt(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < this.encryptedMessage.length(); ++i){
            builder.append(machine.encryptDecrypt(String.valueOf(encryptedMessage.charAt(i))));
        }
        machine.updateConfiguration();
        return builder.toString();
    }

    private void setOffsets(){
        machine.reset();
        List<Rotor> rotors = this.machine.getRotors();
        Keyboard keyboard = this.machine.getKeyboard();
        for(int i = 0; i < this.offsetPositions.size(); ++i){
            rotors.get(i).setOffset(keyboard.getEntryMatchKey(this.offsetPositions.get(i)));
        }
        machine.setConfiguration();
    }

    private boolean isOptionalDecryption(String decryption){
        String[] words = decryption.split(" ");
        if(words.length == 0){
            return false;
        }
        for(String word : words){
            if(!this.dictionary.contains(word)){
                return false;
            }
        }
        return true;
    }

    private void moveToNextConfiguration(){
        int base = this.machine.getKeyboardSize();
        boolean moveNext = true;
        for(int i = 0; i < this.offsetPositions.size() && moveNext; ++i){
            this.offsetPositions.set(i, Math.floorMod(this.offsetPositions.get(i) + 1, base));
            moveNext = this.offsetPositions.get(i) == 0;
        }
    }

    private void updateCandidates(){
        if(!candidatesToReportServer.isEmpty()){
            HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.REPORT_OF_CANDIDATES).newBuilder();
            urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
            RequestBody requestBody;
            requestBody = RequestBody.create(MediaType.parse("application/json"), AppUtils.GSON_SERVICE.toJson(this.candidatesToReportServer));
            Request request = new Request.Builder().url(urlBuilder.build()).method("POST", requestBody).build();
            Call call = AppUtils.CLIENT.newCall(request);
            try {
                call.execute();
            } catch (IOException e) {
                System.out.println("Report Candidates was failed");
            }
        }
    }
}
