package com.enigma.main_component.dashboard_tab_component.tasks;

import com.enigma.dtos.dataObjects.GameDetailsObject;
import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.utiles.AppUtils;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.function.Consumer;

public class GetBattlefieldsTask implements Runnable {

    private final Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable;
    private final SimpleBooleanProperty isReady;

    public GetBattlefieldsTask(Consumer<GetMapOfData<GameDetailsObject>> updateBattlefieldTable, SimpleBooleanProperty isReady){
        this.updateBattlefieldTable = updateBattlefieldTable;
        this.isReady = isReady;
    }

    @Override
    public void run() {
        System.out.println("Allie app("+ Thread.currentThread().getName() + ") -> is up");
        while(!isReady.get()){
            try{
                Thread.sleep(2000);
                getAllBattlefields();
            }catch (InterruptedException e){
                System.out.println("Allie app("+ Thread.currentThread().getName() + ") -> was interrupted");
            }catch (IOException e){
                System.out.println("Allie app("+ Thread.currentThread().getName() + ") -> could not close response");
            }
        }
        System.out.println("Allie app("+ Thread.currentThread().getName() + ") -> is going down");
    }

    private void getAllBattlefields() throws IOException {
        HttpUrl.Builder urlBuildr = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_BATTLEFIELDS_RESOURCE).newBuilder();
        urlBuildr.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuildr.build()).build();
        Response response = null;
        try{
            response = AppUtils.CLIENT.newCall(request).execute();
            GetMapOfData<GameDetailsObject> answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(),  TypeToken.getParameterized(GetMapOfData.class, GameDetailsObject.class).getType());
            if(!answer.getData().isEmpty())
                Platform.runLater(()-> updateBattlefieldTable.accept(answer));

        }catch (IOException e){
            System.out.println("Allie app("+ Thread.currentThread().getName() + ") -> request failed");
        }finally {
            if(response != null)
                response.body().close();
        }



    }

}
