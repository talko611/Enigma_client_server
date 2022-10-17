package com.enigma.main.tasks;

import com.enigma.utils.AppUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.util.function.Consumer;

public class SetAvailableTask implements Runnable{
    private Consumer<Boolean> updateActiveStatus;

    public SetAvailableTask(Consumer<Boolean> updateActiveStatus) {
        this.updateActiveStatus = updateActiveStatus;
    }

    @Override
    public void run() {
        System.out.println("Agent app: set activate thread is up");
        while (!isActivated()){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Agent app: set activate thread was interrupted");
            }
        }
        System.out.println("Agent app: set activate thread is going down");
    }

    private boolean isActivated(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.SET_AVAILABLE_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        try {
            Response response = call.execute();
            if(response.code() == 200){
                Platform.runLater(()->updateActiveStatus.accept(true));
                return true;
            }else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
}
