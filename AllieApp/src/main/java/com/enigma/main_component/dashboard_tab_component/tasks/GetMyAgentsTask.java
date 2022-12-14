package com.enigma.main_component.dashboard_tab_component.tasks;

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

public class GetMyAgentsTask implements Runnable{
    private final Consumer<List<String>> updateAgents;
    private final SimpleBooleanProperty isReady;

    public GetMyAgentsTask(Consumer<List<String>> updateAgents, SimpleBooleanProperty isReady) {
        this.updateAgents = updateAgents;
        this.isReady = isReady;
    }

    @Override
    public void run() {
        System.out.println("Allie App(" + Thread.currentThread().getName() + ") -> is up");
        while(!isReady.get()){
            try {
                Thread.sleep(2000);
                getAgents();
            } catch (InterruptedException e) {
                System.out.println("Allie App(" + Thread.currentThread().getName() + ") -> was interrupted");
            } catch (IOException e) {
                System.out.println("Allie App(" + Thread.currentThread().getName() + ") -> could not close response");
            }
        }
        System.out.println("Allie App(" + Thread.currentThread().getName() + ") -> is going down");
    }

    private void getAgents() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_AGENTS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id",AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            List<String> agents = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(List.class, String.class).getType());
            Platform.runLater(()-> updateAgents.accept(agents));
        } catch (IOException e) {
            System.out.println("Allie App(" + Thread.currentThread().getName() + ") -> request failed");
        }finally {
            if(response != null)
                response.body().close();
        }
    }
}
