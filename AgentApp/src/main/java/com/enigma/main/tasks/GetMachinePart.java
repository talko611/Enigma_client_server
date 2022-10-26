package com.enigma.main.tasks;

import com.engine.enigmaParts.EnigmaParts;
import com.engine.xmlReader.XmlReader;
import com.enigma.utils.AppUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class GetMachinePart implements Runnable{
    private final XmlReader reader;
    private final Consumer<EnigmaParts> setEnigmaParts;
    private final Consumer<Boolean> updateIsReady;

    public GetMachinePart(Consumer<EnigmaParts> setEnigmaParts, Consumer<Boolean>updateIsReady) {
        this.setEnigmaParts = setEnigmaParts;
        this.updateIsReady = updateIsReady;
        this.reader = new XmlReader();
    }

    @Override
    public void run() {
        System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> is up");
        while(!getEnigmaParts()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> was interrupted");
            }
        }
        while (!setReady()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> was interrupted");
            }
        }
        System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> is going down");
    }

    private boolean getEnigmaParts(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.GET_ENIGMA_PARTS_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        boolean isSuccess = false;
        try {
            Response response = call.execute();
            if(response.code() == 200){
                String fileData = response.body().string();
                EnigmaParts parts = reader.load(new ByteArrayInputStream(fileData.getBytes()));
                Platform.runLater(()-> setEnigmaParts.accept(parts));
                isSuccess = true;
            }
        } catch (IOException e) {
            System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> has failed to get enigma part data from server");

        } catch (JAXBException e) {
            System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> could not load the enigma pars from file");
        }
        return isSuccess;
    }

    private boolean setReady(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(AppUtils.APP_URL + AppUtils.SET_READY_RESOURCE).newBuilder();
        urlBuilder.addQueryParameter("id", AppUtils.CLIENT_ID.toString());
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        Call call = AppUtils.CLIENT.newCall(request);
        try {
            Response response = call.execute();
            if(response.code() == 200){
                Platform.runLater(()-> updateIsReady.accept(true));
                return true;
            }
        } catch (IOException e) {
            System.out.println("Agent app(" + Thread.currentThread().getName() + ") -> set ready request has failed");
        }
        return false;
    }
}
