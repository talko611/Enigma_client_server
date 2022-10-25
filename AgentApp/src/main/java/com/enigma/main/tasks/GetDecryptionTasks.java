package com.enigma.main.tasks;

import com.engine.enigmaParts.machineParts.MachineParts;
import com.enigma.dtos.dataObjects.Candidate;
import com.enigma.dtos.dataObjects.DecryptionTaskData;
import com.enigma.machine.Machine;
import com.enigma.machine.MachineImp;
import com.enigma.machine.parts.keyboard.KeyboardImp;
import com.enigma.machine.parts.plugBoard.PlugBoardImp;
import com.enigma.machine.parts.reflector.ReflectorImp;
import com.enigma.machine.parts.rotor.Rotor;
import com.enigma.machine.parts.rotor.RotorImp;
import com.enigma.utils.AppUtils;
import com.google.common.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GetDecryptionTasks implements Runnable{
    private final SimpleBooleanProperty isGameEnded;
    private final SimpleLongProperty tasksPreformed;
    private final SimpleLongProperty tasksAssigned;
    private final Consumer<Long> updateProgress;
    private  final Consumer<Candidate> reportCandidateFound;
    private final String teamName;
    private final Set<String> dictionary;
    private final MachineParts machineParts;
    private final ExecutorService executors;
    private final AtomicInteger numOfTasksToExecute;



    public GetDecryptionTasks(SimpleBooleanProperty isGameEnded, SimpleLongProperty tasksPreformed, SimpleLongProperty tasksAssigned, Consumer<Long> updateProgress, Consumer<Candidate> reportCandidateFound, String teamName, Set<String> dictionary, MachineParts machineParts, ExecutorService executors) {
        this.isGameEnded = isGameEnded;
        this.tasksPreformed = tasksPreformed;
        this.tasksAssigned = tasksAssigned;
        this.updateProgress = updateProgress;
        this.reportCandidateFound = reportCandidateFound;
        this.teamName = teamName;
        this.dictionary = dictionary;
        this.machineParts = machineParts;
        this.executors = executors;
        this.numOfTasksToExecute = new AtomicInteger(0);
    }

    @Override
    public void run() {
        System.out.println("Agent App: get tasks thread is up");
        boolean finishProducing = false;
        try {
            Thread.sleep(2000);//Let producer time to start producing in server
        } catch (InterruptedException e) {
            System.out.println("Agent App: get tasks thread was interrupted");
        }
        while (!isGameEnded.get() && !finishProducing){
            try {
                Thread.sleep(500);
                if(numOfTasksToExecute.get() == 0){
                    finishProducing = getTasks();
                }
            } catch (InterruptedException e) {
                System.out.println("Agent App: get tasks thread was interrupted");
            }
        }
        System.out.println("Agent App: get tasks thread is going down");
    }

    private boolean getTasks(){
        HttpUrl.Builder urlBuildr = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_TASKS_RESOURCE).newBuilder();
        urlBuildr.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuildr.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        int tasksSize = 0;
        try {
            Response response = call.execute();
            if(response.code() == 200){
                List<DecryptionTaskData> tasks = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), new TypeToken<List<DecryptionTaskData>>(){}.getType());
                if(tasks != null){
                    this.numOfTasksToExecute.accumulateAndGet(tasks.size(), Integer::sum);
                    launchTasksToExecutes(tasks);
                    tasksSize = tasks.size();
                    int finalTasksSize1 = tasksSize;
                    Platform.runLater(()->{
                        tasksAssigned.set(tasksAssigned.get() + finalTasksSize1);
                        updateProgress.accept(tasksAssigned.get());
                    });
                }
            } else if (response.code() == 206) {
                response.body().close();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Agent App: get tasks thread request failed");
        }catch (RejectedExecutionException e){
            int finalTasksSize = tasksSize;
            Platform.runLater(()->{
                tasksAssigned.set(tasksAssigned.get() + finalTasksSize);
            });
        }
        return false;
    }

    private void launchTasksToExecutes(List<DecryptionTaskData> decryptionTaskDataList){
        List<DecryptionTask> decryptionTasks = decryptionTaskDataList.stream()
                .map(this::createDecryptionTask)
                .collect(Collectors.toList());
        decryptionTasks.forEach(executors::execute);
    }

    private DecryptionTask createDecryptionTask(DecryptionTaskData data){
        List<Rotor> rotorList = data.getRotorsId().stream()
                .map(id -> new RotorImp((RotorImp) machineParts.getRotor(id)))
                .collect(Collectors.toList());
        Machine machine = new MachineImp();
        machine.setKeyboard(new KeyboardImp((KeyboardImp) machineParts.getKeyboard()));
        machine.setRotors(rotorList);
        machine.setReflector(new ReflectorImp((ReflectorImp) machineParts.getReflector(data.getReflectorId())));
        machine.setPlugBord(new PlugBoardImp(new HashMap<>()));
        return new DecryptionTask(machine,
                data.getEncryptedMessage(),
                (int) data.getTaskSize(),
                data.getOffsets(),
                this.dictionary,
                this.updateProgress,
                this.teamName,
                this.tasksPreformed,
                this.reportCandidateFound,
                this.numOfTasksToExecute);
    }
}
