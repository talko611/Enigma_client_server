package com.enigma.main_component.contestComponent.tasks;

import com.enigma.dtos.dataObjects.AllieData;
import com.enigma.utiles.AppUtils;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GetParticipantsTask implements Runnable{
    private final SimpleBooleanProperty isGameActive;
    private final Consumer<List<AllieData>> updateParticipantsList;

    public GetParticipantsTask(SimpleBooleanProperty isGameActive, Consumer<List<AllieData>> updateParticipantsList) {
        this.isGameActive = isGameActive;
        this.updateParticipantsList = updateParticipantsList;
    }

    @Override
    public void run() {
        System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> is up");
        while (!isGameActive.get()){
            try {
                launchGetGameParticipantsRequest();
                Thread.sleep(1500);
            } catch (IOException e) {
                System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> failed to close response");
            } catch (InterruptedException e) {
                System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> was interrupted");
            }
        }
        System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> is going down");
    }

    private void launchGetGameParticipantsRequest() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_PARTICIPANTS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        Response response = null;
        try{
            response = call.execute();
            List<AllieData> allieDataList = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(List.class, AllieData.class).getType());
            Platform.runLater(()->updateParticipantsList.accept(allieDataList));
        }catch (IOException e){
            System.out.println("Allie app(" + Thread.currentThread().getName() + ") -> request failed");
        }finally {
            if(response != null)
                response.body().close();
        }

    }
}
