package com.enigma.main.tasks;

import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.utils.AppUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.function.Consumer;

public class GetGameStatus implements Runnable{
    private final Consumer<GameDetailsObject> updateGameStatus;
    private final SimpleBooleanProperty isReady;

    public GetGameStatus(Consumer<GameDetailsObject> updateGameStatus, SimpleBooleanProperty isReady) {
        this.updateGameStatus = updateGameStatus;
        this.isReady = isReady;
    }

    @Override
    public void run() {
        System.out.println("Agent app: get details app is started");
        while(isReady.get()){
            getGameStatus();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println("Agent app: get details app was interrupted");
            }catch (RuntimeException e){
                System.out.println("Agent app: get details app got exception from server");
            }
        }
        System.out.println("Agent app: get details app is going down");
    }

    private void getGameStatus(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_GAME_STATUS).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        try {
            Response response = call.execute();
            if(response.code() == 200){
                GameDetailsObject gameDetails = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), GameDetailsObject.class);
                Platform.runLater(()->updateGameStatus.accept(gameDetails));
            }else if(response.code() == 206){
                Platform.runLater(()->isReady.set(false));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
