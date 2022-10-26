package com.enigma.main_screen.contest_component.tasks;

import com.enigma.Utils.AppUtils;
import com.enigma.dtos.dataObjects.Candidate;
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

public class GetCandidatesTask implements Runnable{
    private final SimpleBooleanProperty isGameEnded;
    private final Consumer<List<Candidate>> updateCandidates;

    public GetCandidatesTask(SimpleBooleanProperty isGameEnded, Consumer<List<Candidate>> updateCandidates) {
        this.isGameEnded = isGameEnded;
        this.updateCandidates = updateCandidates;
    }

    @Override
    public void run() {
        System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> is up");
        while (!isGameEnded.get()){
            try {
                launchGetCandidateRequest();
                Thread.sleep(500);
            } catch (IOException e) {
                System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> could not fulfill request");
            } catch (InterruptedException e) {
                System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> was interrupted");
            }
        }
        System.out.println("UBoat app (" + Thread.currentThread().getName() + ") -> is going down");
    }

    private void launchGetCandidateRequest() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_CANDIDATES_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        List<Candidate> candidateList = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(List.class, Candidate.class).getType());
        if(!candidateList.isEmpty()){
            Platform.runLater(()->updateCandidates.accept(candidateList));
        }
    }
}
