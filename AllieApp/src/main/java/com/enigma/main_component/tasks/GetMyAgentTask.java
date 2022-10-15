package com.enigma.main_component.tasks;

import com.enigma.utiles.AppUtils;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import jdk.nashorn.internal.parser.TokenType;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GetMyAgentTask implements Runnable{
    private Consumer<List<String>> updateAgents;
    private SimpleBooleanProperty isInActiveGame;

    public GetMyAgentTask(Consumer<List<String>> updateAgents, SimpleBooleanProperty isInActiveGame) {
        this.updateAgents = updateAgents;
        this.isInActiveGame = isInActiveGame;
    }

    @Override
    public void run() {
        System.out.println("Allie App: Get my agent thread is up");
        while(!isInActiveGame.get()){
            try {
                Thread.sleep(2000);
                getAgents();
            } catch (InterruptedException e) {
                System.out.println("Allie app: Get My Agent thread is interrupted");
            }
        }
        System.out.println("Allie app: Get my agents thread is going down");
    }

    private void getAgents(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_AGENTS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id",AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        try {
            Response response = call.execute();
            List<String> agents = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(List.class, String.class).getType());
            Platform.runLater(()->{
                updateAgents.accept(agents);
            });
        } catch (IOException e) {
            System.out.println("Get Agent request failed");
        }
    }
}
