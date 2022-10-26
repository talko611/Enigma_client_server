package com.enigma.login.tasks;

import com.enigma.dtos.ServletAnswers.GetMapOfData;
import com.enigma.utils.AppUtils;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.function.Consumer;

public class GetActiveAllies implements Runnable{

    private final Consumer<GetMapOfData<String>> updateAllies;
    private final SimpleBooleanProperty isLoggedIn;

    public GetActiveAllies(Consumer<GetMapOfData<String>> updateAllies, SimpleBooleanProperty isLoggedIn) {
        this.updateAllies = updateAllies;
        this.isLoggedIn = isLoggedIn;
    }

    @Override
    public void run() {
        System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> is up");
        while (!isLoggedIn.get()){
            try{
                Thread.sleep(1000);
                getAllies();
            }catch (InterruptedException e){
                System.out.println("Agent app("+ Thread.currentThread().getName() + ") -> was interrupted");
            }catch (IOException e){
                System.out.println("Agent app("+ Thread.currentThread().getName() + ") -> could not close response body");
            }
        }
        System.out.println("Agent app("+ Thread.currentThread().getName() + ") -> is going down");
    }
    private void getAllies() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_ALLIES_RESOURCE ).newBuilder();
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            GetMapOfData<String> answer = AppUtils.GSON_SERVICE.fromJson(response.body().charStream(), TypeToken.getParameterized(GetMapOfData.class, String.class).getType());
            Platform.runLater(()-> updateAllies.accept(answer));

        } catch (IOException e) {
            System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> failed to get allie names");
        }finally {
            if(response != null)
                response.body().close();;

        }
    }
}
