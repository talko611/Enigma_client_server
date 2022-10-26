package com.enigma.main_screen.contest_component.tasks;

import com.enigma.Utils.AppUtils;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.function.Consumer;

public class GetGameStatusTask implements Runnable{
    private final SimpleBooleanProperty isGameEnded;
    private final Consumer<GameDetailsObject> updateGameStatus;

    public GetGameStatusTask(SimpleBooleanProperty isGameEnded, Consumer<GameDetailsObject> updateGameStatus) {
        this.isGameEnded = isGameEnded;
        this.updateGameStatus = updateGameStatus;
    }

    @Override
    public void run() {
        System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> is up");
        while(!isGameEnded.get()){
            try {
                launchGetGameStatusRequest();
                Thread.sleep(1500);
            } catch (IOException e) {
                System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> was interrupted");
            } catch (InterruptedException e) {
                System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> could not fulfill request");
            }
        }
        System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> is going down");
    }

    private void launchGetGameStatusRequest() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_GAME_STATUS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        GameDetailsObject gameStatus = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), GameDetailsObject.class);
        Platform.runLater(()->updateGameStatus.accept(gameStatus));
    }
}
