package com.enigma.main_screen.contest_component.tasks;

import com.enigma.Utils.AppUtils;
import com.enigma.dtos.dataObjects.AllieData;
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

public class GetParticipantsTask implements Runnable{
    private final Consumer<List<AllieData>> updateTeams;
    private final SimpleBooleanProperty isInActiveGame;

    public GetParticipantsTask(Consumer<List<AllieData>> updateTeams, SimpleBooleanProperty isInActiveGame) {
        this.updateTeams = updateTeams;
        this.isInActiveGame = isInActiveGame;
    }

    @Override
    public void run() {
        System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> is up");
        while(!isInActiveGame.get()){
            try{
                Thread.sleep(2000);
                getParticipants();
            }catch (InterruptedException e){
                System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> was interrupted");
            }
        }
        System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> is going down");
    }

    private void getParticipants(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_ALLIES_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        try {
            Response response = call.execute();
            List<AllieData> allieDataList = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(List.class, AllieData.class).getType());
            Platform.runLater(()->{
                updateTeams.accept(allieDataList);
            });
        } catch (IOException e) {
            System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> could not fulfill request");
        }
    }
}
