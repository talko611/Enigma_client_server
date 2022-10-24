package com.enigma.main_component.contestComponent.tasks;

import com.enigma.dtos.dataObjects.AgentProgressObject;
import com.enigma.utiles.AppUtils;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GetAgentProgressTask implements Runnable{
    private final SimpleBooleanProperty isGameEnded;
    private final Consumer<List<AgentProgressObject>> updateAgentProgress;

    public GetAgentProgressTask(SimpleBooleanProperty isGameEnded, Consumer<List<AgentProgressObject>> updateAgentProgress) {
        this.isGameEnded = isGameEnded;
        this.updateAgentProgress = updateAgentProgress;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "(Allie app)-> is up");
        while(!isGameEnded.get()){
            try {
                launchGetAgentProgressRequest();
                Thread.sleep(500);
            } catch (IOException e) {
                System.out.println(Thread.currentThread().getName() + "(Allie app)-> request failed");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "(Allie app)-> was interrupted");
            }
        }
        System.out.println(Thread.currentThread().getName() + "(Allie app)-> is going down");
    }

    private void launchGetAgentProgressRequest() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_AGENT_PROGRESS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            List<AgentProgressObject> agentProgressObjectList = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(List.class, AgentProgressObject.class).getType());
            if(!agentProgressObjectList.isEmpty()){
                Platform.runLater(()->updateAgentProgress.accept(agentProgressObjectList));
            }
        } catch (IOException e) {
            assert response != null;
            response.body().close();

        }
    }
}
