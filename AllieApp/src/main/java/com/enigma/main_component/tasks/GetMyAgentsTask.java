package com.enigma.main_component.tasks;

import com.enigma.dtos.ServletAnswers.GameDetailsObject;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.utiles.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import jdk.nashorn.internal.parser.TokenType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public class GetMyAgentsTask implements Runnable {

    private Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable;
    private SimpleBooleanProperty isActiveGame;

    public GetMyAgentsTask(Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable, SimpleBooleanProperty isActiveGame){
        this.updateBattlefieldTable = updateBattlefieldTable;
        this.isActiveGame = isActiveGame;
    }

    @Override
    public void run() {
        while(!isActiveGame.get()){
            try{
                Thread.sleep(1000);
                getAllBattlefields();
            }catch (InterruptedException e){
                System.out.println("Get battlefield thread exit");
            }
        }
    }

    private void getAllBattlefields(){
        HttpUrl.Builder urlBuildr = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_BATTLEFIELDS_RESOURCE).newBuilder();
        urlBuildr.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuildr.build()).build();
        try{
            Response response = AppUtils.CLIENT.newCall(request).execute();
            Gson gson = new Gson();
            GetMapOfData<GameDetailsObject> answer = gson.fromJson(response.body().charStream(),  TypeToken.getParameterized(GetMapOfData.class, GameDetailsObject.class).getType());
            Platform.runLater(()->{
                updateBattlefieldTable.accept(answer);
            });
        }catch (IOException e){
            System.out.println("Got exception from server");
        }



    }

}
