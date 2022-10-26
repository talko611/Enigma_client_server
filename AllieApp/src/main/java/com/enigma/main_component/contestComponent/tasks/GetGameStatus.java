package com.enigma.main_component.contestComponent.tasks;

import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.utiles.AppUtils;
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
    private final SimpleBooleanProperty isGameEnded;
    private final SimpleBooleanProperty isReady;

    public GetGameStatus(Consumer<GameDetailsObject> updateGameStatus, SimpleBooleanProperty isReady, SimpleBooleanProperty isGameEnded) {
        this.updateGameStatus = updateGameStatus;
        this.isReady = isReady;
        this.isGameEnded = isGameEnded;
    }

    @Override
    public void run() {
        System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> is up");
        while (isReady.get() && !isGameEnded.get()){
            try {
                getGameStatusRequest();
                Thread.sleep(1000);
            } catch (IOException e) {
                System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> failed to close response");
            } catch (InterruptedException e) {
                System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> was interrupted");
            }
        }
        System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> is going down");
    }

    private void getGameStatusRequest() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_GAME_STATUS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        Response response = null;
        try{
            response = call.execute();
            GameDetailsObject gameStatus = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), GameDetailsObject.class);
            Platform.runLater(()->updateGameStatus.accept(gameStatus));
        }catch (IOException e){
            System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> request failed");
        }finally {
            if(response != null)
                response.body().close();
        }
    }
}
