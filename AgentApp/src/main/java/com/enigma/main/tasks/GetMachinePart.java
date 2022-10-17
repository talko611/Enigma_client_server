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
import java.io.InputStream;
import java.util.function.Consumer;

public class GetMachinePart implements Runnable{
    private XmlReader reader;
    private Consumer<EnigmaParts> setEnigmaParts;
    private Consumer<Boolean> updateIsReady;

    public GetMachinePart(Consumer<EnigmaParts> setEnigmaParts, Consumer<Boolean>updateIsReady) {
        this.setEnigmaParts = setEnigmaParts;
        this.updateIsReady = updateIsReady;
        this.reader = new XmlReader();
    }

    @Override
    public void run() {
        System.out.println("Get machine parts task is up");
        while(!getEnigmaParts()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Get machine parts task: was interrupted");
            }
        }
        while (!setReady()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Get machine parts task: was interrupted");
            }
        }
        System.out.println("Get machine parts task: is going down");
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
                Platform.runLater(()->{
                    setEnigmaParts.accept(parts);
                });
                isSuccess = true;
            }
        } catch (IOException e) {
            System.out.println("Get machine parts task: Get enigma parts request has failed to complete");

        } catch (JAXBException e) {
            System.out.println("Get machine parts task: Reader couldn't load file");
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
                Platform.runLater(()->{
                    updateIsReady.accept(true);
                });
                return true;
            }
        } catch (IOException e) {
            System.out.println("Get machine parts task: Set ready req has failed to complete");
        }
        return false;
    }
}
