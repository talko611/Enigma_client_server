package com.enigma.main_component.tasks;

import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.utiles.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.function.Consumer;

public class GetBattlefieldsTask implements Runnable {

    private Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable;
    private SimpleBooleanProperty isActiveGame;

    public GetBattlefieldsTask(Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable, SimpleBooleanProperty isActiveGame){
        this.updateBattlefieldTable = updateBattlefieldTable;
        this.isActiveGame = isActiveGame;
    }

    @Override
    public void run() {
        System.out.println("Allie app: get battlefields thread is up");
        while(!isActiveGame.get()){
            try{
                Thread.sleep(2000);
                getAllBattlefields();
            }catch (InterruptedException e){
                System.out.println("Allie app: get battlefield thread interrupted");
            }
        }
        System.out.println("Allie app: get battlefield thread is going down");
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
